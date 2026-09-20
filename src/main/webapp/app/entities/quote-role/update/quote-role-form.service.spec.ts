import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../quote-role.test-samples';

import { QuoteRoleFormService } from './quote-role-form.service';

describe('QuoteRole Form Service', () => {
  let service: QuoteRoleFormService;

  beforeEach(() => {
    service = TestBed.inject(QuoteRoleFormService);
  });

  describe('Service methods', () => {
    describe('createQuoteRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuoteRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quote: expect.any(Object),
            quoteItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IQuoteRole should create a new form with FormGroup', () => {
        const formGroup = service.createQuoteRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quote: expect.any(Object),
            quoteItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getQuoteRole', () => {
      it('should return NewQuoteRole for default QuoteRole initial value', () => {
        const formGroup = service.createQuoteRoleFormGroup(sampleWithNewData);

        const quoteRole = service.getQuoteRole(formGroup);

        expect(quoteRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuoteRole for empty QuoteRole initial value', () => {
        const formGroup = service.createQuoteRoleFormGroup();

        const quoteRole = service.getQuoteRole(formGroup);

        expect(quoteRole).toMatchObject({});
      });

      it('should return IQuoteRole', () => {
        const formGroup = service.createQuoteRoleFormGroup(sampleWithRequiredData);

        const quoteRole = service.getQuoteRole(formGroup);

        expect(quoteRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuoteRole should not enable id FormControl', () => {
        const formGroup = service.createQuoteRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuoteRole should disable id FormControl', () => {
        const formGroup = service.createQuoteRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
