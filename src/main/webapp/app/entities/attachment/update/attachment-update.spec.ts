import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContentType } from 'app/entities/content-type/content-type.model';
import { ContentTypeService } from 'app/entities/content-type/service/content-type.service';
import { IFileFolder } from 'app/entities/file-folder/file-folder.model';
import { FileFolderService } from 'app/entities/file-folder/service/file-folder.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IAttachment } from '../attachment.model';
import { AttachmentService } from '../service/attachment.service';

import { AttachmentFormService } from './attachment-form.service';
import { AttachmentUpdate } from './attachment-update';

describe('Attachment Management Update Component', () => {
  let comp: AttachmentUpdate;
  let fixture: ComponentFixture<AttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let attachmentFormService: AttachmentFormService;
  let attachmentService: AttachmentService;
  let contentTypeService: ContentTypeService;
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

    fixture = TestBed.createComponent(AttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attachmentFormService = TestBed.inject(AttachmentFormService);
    attachmentService = TestBed.inject(AttachmentService);
    contentTypeService = TestBed.inject(ContentTypeService);
    fileFolderService = TestBed.inject(FileFolderService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ContentType query and add missing value', () => {
      const attachment: IAttachment = { id: 16801 };
      const contentType: IContentType = { id: 8896 };
      attachment.contentType = contentType;

      const contentTypeCollection: IContentType[] = [{ id: 8896 }];
      vi.spyOn(contentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: contentTypeCollection })));
      const additionalContentTypes = [contentType];
      const expectedCollection: IContentType[] = [...additionalContentTypes, ...contentTypeCollection];
      vi.spyOn(contentTypeService, 'addContentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attachment });
      comp.ngOnInit();

      expect(contentTypeService.query).toHaveBeenCalled();
      expect(contentTypeService.addContentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        contentTypeCollection,
        ...additionalContentTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contentTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FileFolder query and add missing value', () => {
      const attachment: IAttachment = { id: 16801 };
      const folder: IFileFolder = { id: 26348 };
      attachment.folder = folder;

      const fileFolderCollection: IFileFolder[] = [{ id: 26348 }];
      vi.spyOn(fileFolderService, 'query').mockReturnValue(of(new HttpResponse({ body: fileFolderCollection })));
      const additionalFileFolders = [folder];
      const expectedCollection: IFileFolder[] = [...additionalFileFolders, ...fileFolderCollection];
      vi.spyOn(fileFolderService, 'addFileFolderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attachment });
      comp.ngOnInit();

      expect(fileFolderService.query).toHaveBeenCalled();
      expect(fileFolderService.addFileFolderToCollectionIfMissing).toHaveBeenCalledWith(
        fileFolderCollection,
        ...additionalFileFolders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.fileFoldersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const attachment: IAttachment = { id: 16801 };
      const owner: IParty = { id: 24527 };
      attachment.owner = owner;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [owner];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attachment });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const attachment: IAttachment = { id: 16801 };
      const contentType: IContentType = { id: 8896 };
      attachment.contentType = contentType;
      const folder: IFileFolder = { id: 26348 };
      attachment.folder = folder;
      const owner: IParty = { id: 24527 };
      attachment.owner = owner;

      activatedRoute.data = of({ attachment });
      comp.ngOnInit();

      expect(comp.contentTypesSharedCollection()).toContainEqual(contentType);
      expect(comp.fileFoldersSharedCollection()).toContainEqual(folder);
      expect(comp.partiesSharedCollection()).toContainEqual(owner);
      expect(comp.attachment).toEqual(attachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAttachment>();
      const attachment = { id: 8078 };
      vi.spyOn(attachmentFormService, 'getAttachment').mockReturnValue(attachment);
      vi.spyOn(attachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(attachment);
      saveSubject.complete();

      // THEN
      expect(attachmentFormService.getAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attachmentService.update).toHaveBeenCalledWith(expect.objectContaining(attachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAttachment>();
      const attachment = { id: 8078 };
      vi.spyOn(attachmentFormService, 'getAttachment').mockReturnValue({ id: null });
      vi.spyOn(attachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(attachment);
      saveSubject.complete();

      // THEN
      expect(attachmentFormService.getAttachment).toHaveBeenCalled();
      expect(attachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAttachment>();
      const attachment = { id: 8078 };
      vi.spyOn(attachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContentType', () => {
      it('should forward to contentTypeService', () => {
        const entity = { id: 8896 };
        const entity2 = { id: 14829 };
        vi.spyOn(contentTypeService, 'compareContentType');
        comp.compareContentType(entity, entity2);
        expect(contentTypeService.compareContentType).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
