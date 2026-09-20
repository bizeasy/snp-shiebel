import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../invoice-type.test-samples';

import { InvoiceTypeFormService } from './invoice-type-form.service';

describe('InvoiceType Form Service', () => {
  let service: InvoiceTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(InvoiceTypeFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IInvoiceType should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoiceType', () => {
      it('should return NewInvoiceType for default InvoiceType initial value', () => {
        const formGroup = service.createInvoiceTypeFormGroup(sampleWithNewData);

        const invoiceType = service.getInvoiceType(formGroup);

        expect(invoiceType).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceType for empty InvoiceType initial value', () => {
        const formGroup = service.createInvoiceTypeFormGroup();

        const invoiceType = service.getInvoiceType(formGroup);

        expect(invoiceType).toMatchObject({});
      });

      it('should return IInvoiceType', () => {
        const formGroup = service.createInvoiceTypeFormGroup(sampleWithRequiredData);

        const invoiceType = service.getInvoiceType(formGroup);

        expect(invoiceType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceType should not enable id FormControl', () => {
        const formGroup = service.createInvoiceTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceType should disable id FormControl', () => {
        const formGroup = service.createInvoiceTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
