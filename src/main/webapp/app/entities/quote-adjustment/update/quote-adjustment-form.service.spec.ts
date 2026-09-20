import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quote-adjustment.test-samples';

import { QuoteAdjustmentFormService } from './quote-adjustment-form.service';

describe('QuoteAdjustment Form Service', () => {
  let service: QuoteAdjustmentFormService;

  beforeEach(() => {
    service = TestBed.inject(QuoteAdjustmentFormService);
  });

  describe('Service methods', () => {
    describe('createQuoteAdjustmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuoteAdjustmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            comments: expect.any(Object),
            amount: expect.any(Object),
            sourcePercentage: expect.any(Object),
            sourceRefId: expect.any(Object),
            exemptAmount: expect.any(Object),
            includeInTax: expect.any(Object),
            includeInShipping: expect.any(Object),
            includedInPrice: expect.any(Object),
            entryDate: expect.any(Object),
            quote: expect.any(Object),
            quoteItem: expect.any(Object),
            adjustmentType: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            taxAuthority: expect.any(Object),
          }),
        );
      });

      it('passing IQuoteAdjustment should create a new form with FormGroup', () => {
        const formGroup = service.createQuoteAdjustmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            comments: expect.any(Object),
            amount: expect.any(Object),
            sourcePercentage: expect.any(Object),
            sourceRefId: expect.any(Object),
            exemptAmount: expect.any(Object),
            includeInTax: expect.any(Object),
            includeInShipping: expect.any(Object),
            includedInPrice: expect.any(Object),
            entryDate: expect.any(Object),
            quote: expect.any(Object),
            quoteItem: expect.any(Object),
            adjustmentType: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            taxAuthority: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuoteAdjustment', () => {
      it('should return NewQuoteAdjustment for default QuoteAdjustment initial value', () => {
        const formGroup = service.createQuoteAdjustmentFormGroup(sampleWithNewData);

        const quoteAdjustment = service.getQuoteAdjustment(formGroup);

        expect(quoteAdjustment).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuoteAdjustment for empty QuoteAdjustment initial value', () => {
        const formGroup = service.createQuoteAdjustmentFormGroup();

        const quoteAdjustment = service.getQuoteAdjustment(formGroup);

        expect(quoteAdjustment).toMatchObject({});
      });

      it('should return IQuoteAdjustment', () => {
        const formGroup = service.createQuoteAdjustmentFormGroup(sampleWithRequiredData);

        const quoteAdjustment = service.getQuoteAdjustment(formGroup);

        expect(quoteAdjustment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuoteAdjustment should not enable id FormControl', () => {
        const formGroup = service.createQuoteAdjustmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuoteAdjustment should disable id FormControl', () => {
        const formGroup = service.createQuoteAdjustmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
