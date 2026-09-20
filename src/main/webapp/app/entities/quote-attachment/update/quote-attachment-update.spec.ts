import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { AttachmentService } from 'app/entities/attachment/service/attachment.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { IQuoteAttachment } from '../quote-attachment.model';
import { QuoteAttachmentService } from '../service/quote-attachment.service';

import { QuoteAttachmentFormService } from './quote-attachment-form.service';
import { QuoteAttachmentUpdate } from './quote-attachment-update';

describe('QuoteAttachment Management Update Component', () => {
  let comp: QuoteAttachmentUpdate;
  let fixture: ComponentFixture<QuoteAttachmentUpdate>;
  let activatedRoute: ActivatedRoute;
  let quoteAttachmentFormService: QuoteAttachmentFormService;
  let quoteAttachmentService: QuoteAttachmentService;
  let quoteService: QuoteService;
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

    fixture = TestBed.createComponent(QuoteAttachmentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteAttachmentFormService = TestBed.inject(QuoteAttachmentFormService);
    quoteAttachmentService = TestBed.inject(QuoteAttachmentService);
    quoteService = TestBed.inject(QuoteService);
    attachmentService = TestBed.inject(AttachmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Quote query and add missing value', () => {
      const quoteAttachment: IQuoteAttachment = { id: 29771 };
      const quote: IQuote = { id: 10209 };
      quoteAttachment.quote = quote;

      const quoteCollection: IQuote[] = [{ id: 10209 }];
      vi.spyOn(quoteService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteCollection })));
      const additionalQuotes = [quote];
      const expectedCollection: IQuote[] = [...additionalQuotes, ...quoteCollection];
      vi.spyOn(quoteService, 'addQuoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAttachment });
      comp.ngOnInit();

      expect(quoteService.query).toHaveBeenCalled();
      expect(quoteService.addQuoteToCollectionIfMissing).toHaveBeenCalledWith(
        quoteCollection,
        ...additionalQuotes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Attachment query and add missing value', () => {
      const quoteAttachment: IQuoteAttachment = { id: 29771 };
      const attachment: IAttachment = { id: 8078 };
      quoteAttachment.attachment = attachment;

      const attachmentCollection: IAttachment[] = [{ id: 8078 }];
      vi.spyOn(attachmentService, 'query').mockReturnValue(of(new HttpResponse({ body: attachmentCollection })));
      const additionalAttachments = [attachment];
      const expectedCollection: IAttachment[] = [...additionalAttachments, ...attachmentCollection];
      vi.spyOn(attachmentService, 'addAttachmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteAttachment });
      comp.ngOnInit();

      expect(attachmentService.query).toHaveBeenCalled();
      expect(attachmentService.addAttachmentToCollectionIfMissing).toHaveBeenCalledWith(
        attachmentCollection,
        ...additionalAttachments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.attachmentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quoteAttachment: IQuoteAttachment = { id: 29771 };
      const quote: IQuote = { id: 10209 };
      quoteAttachment.quote = quote;
      const attachment: IAttachment = { id: 8078 };
      quoteAttachment.attachment = attachment;

      activatedRoute.data = of({ quoteAttachment });
      comp.ngOnInit();

      expect(comp.quotesSharedCollection()).toContainEqual(quote);
      expect(comp.attachmentsSharedCollection()).toContainEqual(attachment);
      expect(comp.quoteAttachment).toEqual(quoteAttachment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteAttachment>();
      const quoteAttachment = { id: 18038 };
      vi.spyOn(quoteAttachmentFormService, 'getQuoteAttachment').mockReturnValue(quoteAttachment);
      vi.spyOn(quoteAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteAttachment);
      saveSubject.complete();

      // THEN
      expect(quoteAttachmentFormService.getQuoteAttachment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteAttachmentService.update).toHaveBeenCalledWith(expect.objectContaining(quoteAttachment));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteAttachment>();
      const quoteAttachment = { id: 18038 };
      vi.spyOn(quoteAttachmentFormService, 'getQuoteAttachment').mockReturnValue({ id: null });
      vi.spyOn(quoteAttachmentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteAttachment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteAttachment);
      saveSubject.complete();

      // THEN
      expect(quoteAttachmentFormService.getQuoteAttachment).toHaveBeenCalled();
      expect(quoteAttachmentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteAttachment>();
      const quoteAttachment = { id: 18038 };
      vi.spyOn(quoteAttachmentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteAttachment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteAttachmentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuote', () => {
      it('should forward to quoteService', () => {
        const entity = { id: 10209 };
        const entity2 = { id: 23928 };
        vi.spyOn(quoteService, 'compareQuote');
        comp.compareQuote(entity, entity2);
        expect(quoteService.compareQuote).toHaveBeenCalledWith(entity, entity2);
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
