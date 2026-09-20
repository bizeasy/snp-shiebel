import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { ICashBook } from 'app/entities/cash-book/cash-book.model';
import { CashBookService } from 'app/entities/cash-book/service/cash-book.service';
import { ICashBookAttachment } from '../cash-book-attachment.model';
import { CashBookAttachmentService } from '../service/cash-book-attachment.service';

import { CashBookAttachmentFormService } from './cash-book-attachment-form.service';
import { CashBookAttachmentUpdate } from './cash-book-attachment-update';

describe('CashBookAttachment Management Update Component', () => {
  let comp: CashBookAttachmentUpdate;
  let fixture: ComponentFixture<CashBookAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let cashBookAttachmentFormService: CashBookAttachmentFormService;
  let cashBookAttachmentService: CashBookAttachmentService;
  let cashBookService: CashBookService;
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

    fixture = TestBed.createComponent(CashBookAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cashBookAttachmentFormService = TestBed.inject(CashBookAttachmentFormService);
    cashBookAttachmentService = TestBed.inject(CashBookAttachmentService);
    cashBookService = TestBed.inject(CashBookService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CashBook query and add missing value', () => {
      const cashBookAttachment: ICashBookAttachment = { id: 11865 };
      const cashBook: ICashBook = { id: 22220 };
      cashBookAttachment.cashBook = cashBook;

      const cashBookCollection: ICashBook[] = [{ id: 22220 }];
      vi.spyOn(cashBookService, 'query').mockReturnValue(of(new HttpResponse({ body: cashBookCollection })));
      const additionalCashBooks = [cashBook];
      const expectedCollection: ICashBook[] = [...additionalCashBooks, ...cashBookCollection];
      vi.spyOn(cashBookService, 'addCashBookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBookAttachment });
      comp.ngOnInit();

      expect(cashBookService.query).toHaveBeenCalled();
      expect(cashBookService.addCashBookToCollectionIfMissing).toHaveBeenCalledWith(
        cashBookCollection,
        ...additionalCashBooks.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.cashBooksSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const cashBookAttachment: ICashBookAttachment = { id: 11865 };
      const attachment: IAttachment = { id: 8078 };
      cashBookAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBookAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cashBookAttachment: ICashBookAttachment = { id: 11865 };
      const cashBook: ICashBook = { id: 22220 };
      cashBookAttachment.cashBook = cashBook;
      const attachment: IAttachment = { id: 8078 };
      cashBookAttachment.attachment = attachment;

      activatedRoute.data = of({ cashBookAttachment });
      comp.ngOnInit();

      expect(comp.cashBooksSharedCollection()).toContainEqual(cashBook);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.cashBookAttachment).toEqual(cashBookAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICashBookAttachment>();
      const cashBookAttachment = { id: 24340 };
      vi.spyOn(cashBookAttachmentFormService, 'getCashBookAttachment').mockReturnValue(cashBookAttachment);
      vi.spyOn(cashBookAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBookAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cashBookAttachment);
      saveSubject.complete();

      // THEN
      expect(cashBookAttachmentFormService.getCashBookAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cashBookAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(cashBookAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICashBookAttachment>();
      const cashBookAttachment = { id: 24340 };
      vi.spyOn(cashBookAttachmentFormService, 'getCashBookAttachment').mockReturnValue({ id: null });
      vi.spyOn(cashBookAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBookAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cashBookAttachment);
      saveSubject.complete();

      // THEN
      expect(cashBookAttachmentFormService.getCashBookAttachment).toHaveBeenCalled();
      expect(cashBookAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICashBookAttachment>();
      const cashBookAttachment = { id: 24340 };
      vi.spyOn(cashBookAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBookAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cashBookAttachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCashBook', () => {
      it('should forward to cashBookService', () => {
        const entity = { id: 22220 };
        const entity2 = { id: 26481 };
        vi.spyOn(cashBookService, 'compareCashBook');
        comp.compareCashBook(entity, entity2);
        expect(cashBookService.compareCashBook).toHaveBeenCalledWith(entity, entity2);
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
