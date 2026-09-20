import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quote-type.test-samples';

import { QuoteTypeFormService } from './quote-type-form.service';

describe('QuoteType Form Service', () => {
  let service: QuoteTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(QuoteTypeFormService);
  });

  describe('Service methods', () => {
    describe('createQuoteTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuoteTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IQuoteType should create a new form with FormGroup', () => {
        const formGroup = service.createQuoteTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuoteType', () => {
      it('should return NewQuoteType for default QuoteType initial value', () => {
        const formGroup = service.createQuoteTypeFormGroup(sampleWithNewData);

        const quoteType = service.getQuoteType(formGroup);

        expect(quoteType).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuoteType for empty QuoteType initial value', () => {
        const formGroup = service.createQuoteTypeFormGroup();

        const quoteType = service.getQuoteType(formGroup);

        expect(quoteType).toMatchObject({});
      });

      it('should return IQuoteType', () => {
        const formGroup = service.createQuoteTypeFormGroup(sampleWithRequiredData);

        const quoteType = service.getQuoteType(formGroup);

        expect(quoteType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuoteType should not enable id FormControl', () => {
        const formGroup = service.createQuoteTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuoteType should disable id FormControl', () => {
        const formGroup = service.createQuoteTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
