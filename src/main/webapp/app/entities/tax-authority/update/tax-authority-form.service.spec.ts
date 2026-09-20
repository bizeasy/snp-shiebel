import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tax-authority.test-samples';

import { TaxAuthorityFormService } from './tax-authority-form.service';

describe('TaxAuthority Form Service', () => {
  let service: TaxAuthorityFormService;

  beforeEach(() => {
    service = TestBed.inject(TaxAuthorityFormService);
  });

  describe('Service methods', () => {
    describe('createTaxAuthorityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTaxAuthorityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            geo: expect.any(Object),
          }),
        );
      });

      it('passing ITaxAuthority should create a new form with FormGroup', () => {
        const formGroup = service.createTaxAuthorityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            geo: expect.any(Object),
          }),
        );
      });
    });

    describe('getTaxAuthority', () => {
      it('should return NewTaxAuthority for default TaxAuthority initial value', () => {
        const formGroup = service.createTaxAuthorityFormGroup(sampleWithNewData);

        const taxAuthority = service.getTaxAuthority(formGroup);

        expect(taxAuthority).toMatchObject(sampleWithNewData);
      });

      it('should return NewTaxAuthority for empty TaxAuthority initial value', () => {
        const formGroup = service.createTaxAuthorityFormGroup();

        const taxAuthority = service.getTaxAuthority(formGroup);

        expect(taxAuthority).toMatchObject({});
      });

      it('should return ITaxAuthority', () => {
        const formGroup = service.createTaxAuthorityFormGroup(sampleWithRequiredData);

        const taxAuthority = service.getTaxAuthority(formGroup);

        expect(taxAuthority).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITaxAuthority should not enable id FormControl', () => {
        const formGroup = service.createTaxAuthorityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTaxAuthority should disable id FormControl', () => {
        const formGroup = service.createTaxAuthorityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
