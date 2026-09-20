import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IFileFolder } from '../file-folder.model';
import { FileFolderService } from '../service/file-folder.service';

import { FileFolderFormService } from './file-folder-form.service';
import { FileFolderUpdate } from './file-folder-update';

describe('FileFolder Management Update Component', () => {
  let comp: FileFolderUpdate;
  let fixture: ComponentFixture<FileFolderUpdate>;
  let activatedRoute: ActivatedRoute;
  let fileFolderFormService: FileFolderFormService;
  let fileFolderService: FileFolderService;
  let partyService: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(FileFolderUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileFolderFormService = TestBed.inject(FileFolderFormService);
    fileFolderService = TestBed.inject(FileFolderService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call FileFolder query and add missing value', () => {
      const fileFolder: IFileFolder = { id: 8213 };
      const parent: IFileFolder = { id: 26348 };
      fileFolder.parent = parent;

      const fileFolderCollection: IFileFolder[] = [{ id: 26348 }];
      vi.spyOn(fileFolderService, 'query').mockReturnValue(of(new HttpResponse({ body: fileFolderCollection })));
      const additionalFileFolders = [parent];
      const expectedCollection: IFileFolder[] = [...additionalFileFolders, ...fileFolderCollection];
      vi.spyOn(fileFolderService, 'addFileFolderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileFolder });
      comp.ngOnInit();

      expect(fileFolderService.query).toHaveBeenCalled();
      expect(fileFolderService.addFileFolderToCollectionIfMissing).toHaveBeenCalledWith(
        fileFolderCollection,
        ...additionalFileFolders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.fileFoldersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const fileFolder: IFileFolder = { id: 8213 };
      const owner: IParty = { id: 24527 };
      fileFolder.owner = owner;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [owner];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileFolder });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const fileFolder: IFileFolder = { id: 8213 };
      const parent: IFileFolder = { id: 26348 };
      fileFolder.parent = parent;
      const owner: IParty = { id: 24527 };
      fileFolder.owner = owner;

      activatedRoute.data = of({ fileFolder });
      comp.ngOnInit();

      expect(comp.fileFoldersSharedCollection()).toContainEqual(parent);
      expect(comp.partiesSharedCollection()).toContainEqual(owner);
      expect(comp.fileFolder).toEqual(fileFolder);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFileFolder>();
      const fileFolder = { id: 26348 };
      vi.spyOn(fileFolderFormService, 'getFileFolder').mockReturnValue(fileFolder);
      vi.spyOn(fileFolderService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fileFolder);
      saveSubject.complete();

      // THEN
      expect(fileFolderFormService.getFileFolder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileFolderService.update).toHaveBeenCalledWith(expect.objectContaining(fileFolder));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFileFolder>();
      const fileFolder = { id: 26348 };
      vi.spyOn(fileFolderFormService, 'getFileFolder').mockReturnValue({ id: null });
      vi.spyOn(fileFolderService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileFolder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fileFolder);
      saveSubject.complete();

      // THEN
      expect(fileFolderFormService.getFileFolder).toHaveBeenCalled();
      expect(fileFolderService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFileFolder>();
      const fileFolder = { id: 26348 };
      vi.spyOn(fileFolderService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileFolderService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFileFolder', () => {
      it('should forward to fileFolderService', () => {
        const entity = { id: 26348 };
        const entity2 = { id: 8213 };
        vi.spyOn(fileFolderService, 'compareFileFolder');
        comp.compareFileFolder(entity, entity2);
        expect(fileFolderService.compareFileFolder).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
