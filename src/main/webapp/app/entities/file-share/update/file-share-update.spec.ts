import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IFileFolder } from 'app/entities/file-folder/file-folder.model';
import { FileFolderService } from 'app/entities/file-folder/service/file-folder.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IFileShare } from '../file-share.model';
import { FileShareService } from '../service/file-share.service';

import { FileShareFormService } from './file-share-form.service';
import { FileShareUpdate } from './file-share-update';

describe('FileShare Management Update Component', () => {
  let comp: FileShareUpdate;
  let fixture: ComponentFixture<FileShareUpdate>;
  let activatedRoute: ActivatedRoute;
  let fileShareFormService: FileShareFormService;
  let fileShareService: FileShareService;
  let fileFolderService: FileFolderService;
  let attachmentService: AttachmentService;
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

    fixture = TestBed.createComponent(FileShareUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileShareFormService = TestBed.inject(FileShareFormService);
    fileShareService = TestBed.inject(FileShareService);
    fileFolderService = TestBed.inject(FileFolderService);
    attachmentService = TestBed.inject(AttachmentService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call FileFolder query and add missing value', () => {
      const fileShare: IFileShare = { id: 31237 };
      const folder: IFileFolder = { id: 26348 };
      fileShare.folder = folder;

      const fileFolderCollection: IFileFolder[] = [{ id: 26348 }];
      vi.spyOn(fileFolderService, 'query').mockReturnValue(of(new HttpResponse({ body: fileFolderCollection })));
      const additionalFileFolders = [folder];
      const expectedCollection: IFileFolder[] = [...additionalFileFolders, ...fileFolderCollection];
      vi.spyOn(fileFolderService, 'addFileFolderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileShare });
      comp.ngOnInit();

      expect(fileFolderService.query).toHaveBeenCalled();
      expect(fileFolderService.addFileFolderToCollectionIfMissing).toHaveBeenCalledWith(
        fileFolderCollection,
        ...additionalFileFolders.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.fileFoldersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const fileShare: IFileShare = { id: 31237 };
      const attachment: IAttachment = { id: 8078 };
      fileShare.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileShare });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const fileShare: IFileShare = { id: 31237 };
      const sharedWith: IParty = { id: 24527 };
      fileShare.sharedWith = sharedWith;
      const sharedBy: IParty = { id: 24527 };
      fileShare.sharedBy = sharedBy;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [sharedWith, sharedBy];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fileShare });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const fileShare: IFileShare = { id: 31237 };
      const folder: IFileFolder = { id: 26348 };
      fileShare.folder = folder;
      const attachment: IAttachment = { id: 8078 };
      fileShare.attachment = attachment;
      const sharedWith: IParty = { id: 24527 };
      fileShare.sharedWith = sharedWith;
      const sharedBy: IParty = { id: 24527 };
      fileShare.sharedBy = sharedBy;

      activatedRoute.data = of({ fileShare });
      comp.ngOnInit();

      expect(comp.fileFoldersSharedCollection()).toContainEqual(folder);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.partiesSharedCollection()).toContainEqual(sharedWith);
      expect(comp.partiesSharedCollection()).toContainEqual(sharedBy);
      expect(comp.fileShare).toEqual(fileShare);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFileShare>();
      const fileShare = { id: 11663 };
      vi.spyOn(fileShareFormService, 'getFileShare').mockReturnValue(fileShare);
      vi.spyOn(fileShareService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileShare });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fileShare);
      saveSubject.complete();

      // THEN
      expect(fileShareFormService.getFileShare).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileShareService.update).toHaveBeenCalledWith(expect.objectContaining(fileShare));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFileShare>();
      const fileShare = { id: 11663 };
      vi.spyOn(fileShareFormService, 'getFileShare').mockReturnValue({ id: null });
      vi.spyOn(fileShareService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileShare: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fileShare);
      saveSubject.complete();

      // THEN
      expect(fileShareFormService.getFileShare).toHaveBeenCalled();
      expect(fileShareService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFileShare>();
      const fileShare = { id: 11663 };
      vi.spyOn(fileShareService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileShare });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileShareService.update).toHaveBeenCalled();
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

    describe('compareAttachment', () => {
      it('should forward to attachmentService', () => {
        const entity = { id: 8078 };
        const entity2 = { id: 16801 };
        vi.spyOn(attachmentService, 'compareAttachment');
        comp.compareAttachment(entity, entity2);
        expect(attachmentService.compareAttachment).toHaveBeenCalledWith(entity, entity2);
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
