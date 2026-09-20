import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { ICustRequestAttachment } from '../cust-request-attachment.model';
import { CustRequestAttachmentService } from '../service/cust-request-attachment.service';

import { CustRequestAttachmentFormService } from './cust-request-attachment-form.service';
import { CustRequestAttachmentUpdate } from './cust-request-attachment-update';

describe('CustRequestAttachment Management Update Component', () => {
  let comp: CustRequestAttachmentUpdate;
  let fixture: ComponentFixture<CustRequestAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let custRequestAttachmentFormService: CustRequestAttachmentFormService;
  let custRequestAttachmentService: CustRequestAttachmentService;
  let custRequestService: CustRequestService;
  let attachmentService: AttachmentService;

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

    fixture = TestBed.createComponent(CustRequestAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    custRequestAttachmentFormService = TestBed.inject(CustRequestAttachmentFormService);
    custRequestAttachmentService = TestBed.inject(CustRequestAttachmentService);
    custRequestService = TestBed.inject(CustRequestService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CustRequest query and add missing value', () => {
      const custRequestAttachment: ICustRequestAttachment = { id: 30483 };
      const custRequest: ICustRequest = { id: 17760 };
      custRequestAttachment.custRequest = custRequest;

      const custRequestCollection: ICustRequest[] = [{ id: 17760 }];
      vi.spyOn(custRequestService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestCollection })));
      const additionalCustRequests = [custRequest];
      const expectedCollection: ICustRequest[] = [...additionalCustRequests, ...custRequestCollection];
      vi.spyOn(custRequestService, 'addCustRequestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestAttachment });
      comp.ngOnInit();

      expect(custRequestService.query).toHaveBeenCalled();
      expect(custRequestService.addCustRequestToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestCollection,
        ...additionalCustRequests.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const custRequestAttachment: ICustRequestAttachment = { id: 30483 };
      const attachment: IAttachment = { id: 8078 };
      custRequestAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const custRequestAttachment: ICustRequestAttachment = { id: 30483 };
      const custRequest: ICustRequest = { id: 17760 };
      custRequestAttachment.custRequest = custRequest;
      const attachment: IAttachment = { id: 8078 };
      custRequestAttachment.attachment = attachment;

      activatedRoute.data = of({ custRequestAttachment });
      comp.ngOnInit();

      expect(comp.custRequestsSharedCollection()).toContainEqual(custRequest);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.custRequestAttachment).toEqual(custRequestAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestAttachment>();
      const custRequestAttachment = { id: 31720 };
      vi.spyOn(custRequestAttachmentFormService, 'getCustRequestAttachment').mockReturnValue(custRequestAttachment);
      vi.spyOn(custRequestAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestAttachment);
      saveSubject.complete();

      // THEN
      expect(custRequestAttachmentFormService.getCustRequestAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(custRequestAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(custRequestAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestAttachment>();
      const custRequestAttachment = { id: 31720 };
      vi.spyOn(custRequestAttachmentFormService, 'getCustRequestAttachment').mockReturnValue({ id: null });
      vi.spyOn(custRequestAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestAttachment);
      saveSubject.complete();

      // THEN
      expect(custRequestAttachmentFormService.getCustRequestAttachment).toHaveBeenCalled();
      expect(custRequestAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestAttachment>();
      const custRequestAttachment = { id: 31720 };
      vi.spyOn(custRequestAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(custRequestAttachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustRequest', () => {
      it('should forward to custRequestService', () => {
        const entity = { id: 17760 };
        const entity2 = { id: 10199 };
        vi.spyOn(custRequestService, 'compareCustRequest');
        comp.compareCustRequest(entity, entity2);
        expect(custRequestService.compareCustRequest).toHaveBeenCalledWith(entity, entity2);
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
  });
});
