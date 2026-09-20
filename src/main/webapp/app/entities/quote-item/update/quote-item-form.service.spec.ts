import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quote-item.test-samples';

import { QuoteItemFormService } from './quote-item-form.service';

describe('QuoteItem Form Service', () => {
  let service: QuoteItemFormService;

  beforeEach(() => {
    service = TestBed.inject(QuoteItemFormService);
  });

  describe('Service methods', () => {
    describe('createQuoteItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuoteItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            selectedAmount: expect.any(Object),
            quoteUnitPrice: expect.any(Object),
            totalAmount: expect.any(Object),
            estimatedDeliveryDate: expect.any(Object),
            leadTimeDays: expect.any(Object),
            comments: expect.any(Object),
            quote: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            uom: expect.any(Object),
            status: expect.any(Object),
            custRequest: expect.any(Object),
            custRequestItem: expect.any(Object),
          }),
        );
      });

      it('passing IQuoteItem should create a new form with FormGroup', () => {
        const formGroup = service.createQuoteItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            selectedAmount: expect.any(Object),
            quoteUnitPrice: expect.any(Object),
            totalAmount: expect.any(Object),
            estimatedDeliveryDate: expect.any(Object),
            leadTimeDays: expect.any(Object),
            comments: expect.any(Object),
            quote: expect.any(Object),
            product: expect.any(Object),
            productFeature: expect.any(Object),
            uom: expect.any(Object),
            status: expect.any(Object),
            custRequest: expect.any(Object),
            custRequestItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuoteItem', () => {
      it('should return NewQuoteItem for default QuoteItem initial value', () => {
        const formGroup = service.createQuoteItemFormGroup(sampleWithNewData);

        const quoteItem = service.getQuoteItem(formGroup);

        expect(quoteItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuoteItem for empty QuoteItem initial value', () => {
        const formGroup = service.createQuoteItemFormGroup();

        const quoteItem = service.getQuoteItem(formGroup);

        expect(quoteItem).toMatchObject({});
      });

      it('should return IQuoteItem', () => {
        const formGroup = service.createQuoteItemFormGroup(sampleWithRequiredData);

        const quoteItem = service.getQuoteItem(formGroup);

        expect(quoteItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuoteItem should not enable id FormControl', () => {
        const formGroup = service.createQuoteItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuoteItem should disable id FormControl', () => {
        const formGroup = service.createQuoteItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
