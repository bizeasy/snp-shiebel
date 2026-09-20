import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../invoice-item-type.test-samples';

import { InvoiceItemTypeFormService } from './invoice-item-type-form.service';

describe('InvoiceItemType Form Service', () => {
  let service: InvoiceItemTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(InvoiceItemTypeFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceItemTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceItemTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IInvoiceItemType should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceItemTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoiceItemType', () => {
      it('should return NewInvoiceItemType for default InvoiceItemType initial value', () => {
        const formGroup = service.createInvoiceItemTypeFormGroup(sampleWithNewData);

        const invoiceItemType = service.getInvoiceItemType(formGroup);

        expect(invoiceItemType).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceItemType for empty InvoiceItemType initial value', () => {
        const formGroup = service.createInvoiceItemTypeFormGroup();

        const invoiceItemType = service.getInvoiceItemType(formGroup);

        expect(invoiceItemType).toMatchObject({});
      });

      it('should return IInvoiceItemType', () => {
        const formGroup = service.createInvoiceItemTypeFormGroup(sampleWithRequiredData);

        const invoiceItemType = service.getInvoiceItemType(formGroup);

        expect(invoiceItemType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceItemType should not enable id FormControl', () => {
        const formGroup = service.createInvoiceItemTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceItemType should disable id FormControl', () => {
        const formGroup = service.createInvoiceItemTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
