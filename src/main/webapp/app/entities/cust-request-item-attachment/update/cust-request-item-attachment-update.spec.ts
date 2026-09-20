import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { CustRequestItemService } from 'app/entities/cust-request-item/service/cust-request-item.service';
import { ICustRequestItemAttachment } from '../cust-request-item-attachment.model';
import { CustRequestItemAttachmentService } from '../service/cust-request-item-attachment.service';

import { CustRequestItemAttachmentFormService } from './cust-request-item-attachment-form.service';
import { CustRequestItemAttachmentUpdate } from './cust-request-item-attachment-update';

describe('CustRequestItemAttachment Management Update Component', () => {
  let comp: CustRequestItemAttachmentUpdate;
  let fixture: ComponentFixture<CustRequestItemAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let custRequestItemAttachmentFormService: CustRequestItemAttachmentFormService;
  let custRequestItemAttachmentService: CustRequestItemAttachmentService;
  let custRequestItemService: CustRequestItemService;
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

    fixture = TestBed.createComponent(CustRequestItemAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    custRequestItemAttachmentFormService = TestBed.inject(CustRequestItemAttachmentFormService);
    custRequestItemAttachmentService = TestBed.inject(CustRequestItemAttachmentService);
    custRequestItemService = TestBed.inject(CustRequestItemService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CustRequestItem query and add missing value', () => {
      const custRequestItemAttachment: ICustRequestItemAttachment = { id: 23584 };
      const custRequestItem: ICustRequestItem = { id: 17328 };
      custRequestItemAttachment.custRequestItem = custRequestItem;

      const custRequestItemCollection: ICustRequestItem[] = [{ id: 17328 }];
      vi.spyOn(custRequestItemService, 'query').mockReturnValue(of(new HttpResponse({ body: custRequestItemCollection })));
      const additionalCustRequestItems = [custRequestItem];
      const expectedCollection: ICustRequestItem[] = [...additionalCustRequestItems, ...custRequestItemCollection];
      vi.spyOn(custRequestItemService, 'addCustRequestItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestItemAttachment });
      comp.ngOnInit();

      expect(custRequestItemService.query).toHaveBeenCalled();
      expect(custRequestItemService.addCustRequestItemToCollectionIfMissing).toHaveBeenCalledWith(
        custRequestItemCollection,
        ...additionalCustRequestItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.custRequestItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const custRequestItemAttachment: ICustRequestItemAttachment = { id: 23584 };
      const attachment: IAttachment = { id: 8078 };
      custRequestItemAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ custRequestItemAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const custRequestItemAttachment: ICustRequestItemAttachment = { id: 23584 };
      const custRequestItem: ICustRequestItem = { id: 17328 };
      custRequestItemAttachment.custRequestItem = custRequestItem;
      const attachment: IAttachment = { id: 8078 };
      custRequestItemAttachment.attachment = attachment;

      activatedRoute.data = of({ custRequestItemAttachment });
      comp.ngOnInit();

      expect(comp.custRequestItemsSharedCollection()).toContainEqual(custRequestItem);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.custRequestItemAttachment).toEqual(custRequestItemAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestItemAttachment>();
      const custRequestItemAttachment = { id: 342 };
      vi.spyOn(custRequestItemAttachmentFormService, 'getCustRequestItemAttachment').mockReturnValue(custRequestItemAttachment);
      vi.spyOn(custRequestItemAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestItemAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestItemAttachment);
      saveSubject.complete();

      // THEN
      expect(custRequestItemAttachmentFormService.getCustRequestItemAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(custRequestItemAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(custRequestItemAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestItemAttachment>();
      const custRequestItemAttachment = { id: 342 };
      vi.spyOn(custRequestItemAttachmentFormService, 'getCustRequestItemAttachment').mockReturnValue({ id: null });
      vi.spyOn(custRequestItemAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestItemAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(custRequestItemAttachment);
      saveSubject.complete();

      // THEN
      expect(custRequestItemAttachmentFormService.getCustRequestItemAttachment).toHaveBeenCalled();
      expect(custRequestItemAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustRequestItemAttachment>();
      const custRequestItemAttachment = { id: 342 };
      vi.spyOn(custRequestItemAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ custRequestItemAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(custRequestItemAttachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustRequestItem', () => {
      it('should forward to custRequestItemService', () => {
        const entity = { id: 17328 };
        const entity2 = { id: 23718 };
        vi.spyOn(custRequestItemService, 'compareCustRequestItem');
        comp.compareCustRequestItem(entity, entity2);
        expect(custRequestItemService.compareCustRequestItem).toHaveBeenCalledWith(entity, entity2);
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
