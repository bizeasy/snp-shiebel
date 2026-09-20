import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quote-attachment.test-samples';

import { QuoteAttachmentFormService } from './quote-attachment-form.service';

describe('QuoteAttachment Form Service', () => {
  let service: QuoteAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(QuoteAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createQuoteAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuoteAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            quote: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing IQuoteAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createQuoteAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            quote: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuoteAttachment', () => {
      it('should return NewQuoteAttachment for default QuoteAttachment initial value', () => {
        const formGroup = service.createQuoteAttachmentFormGroup(sampleWithNewData);

        const quoteAttachment = service.getQuoteAttachment(formGroup);

        expect(quoteAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuoteAttachment for empty QuoteAttachment initial value', () => {
        const formGroup = service.createQuoteAttachmentFormGroup();

        const quoteAttachment = service.getQuoteAttachment(formGroup);

        expect(quoteAttachment).toMatchObject({});
      });

      it('should return IQuoteAttachment', () => {
        const formGroup = service.createQuoteAttachmentFormGroup(sampleWithRequiredData);

        const quoteAttachment = service.getQuoteAttachment(formGroup);

        expect(quoteAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuoteAttachment should not enable id FormControl', () => {
        const formGroup = service.createQuoteAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuoteAttachment should disable id FormControl', () => {
        const formGroup = service.createQuoteAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
