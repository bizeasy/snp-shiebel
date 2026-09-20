import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../invoice-item-type-map.test-samples';

import { InvoiceItemTypeMapFormService } from './invoice-item-type-map-form.service';

describe('InvoiceItemTypeMap Form Service', () => {
  let service: InvoiceItemTypeMapFormService;

  beforeEach(() => {
    service = TestBed.inject(InvoiceItemTypeMapFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceItemTypeMapFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceItemTypeMapFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceItemMapKey: expect.any(Object),
            invoiceType: expect.any(Object),
            invoiceItemType: expect.any(Object),
          }),
        );
      });

      it('passing IInvoiceItemTypeMap should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceItemTypeMapFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceItemMapKey: expect.any(Object),
            invoiceType: expect.any(Object),
            invoiceItemType: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoiceItemTypeMap', () => {
      it('should return NewInvoiceItemTypeMap for default InvoiceItemTypeMap initial value', () => {
        const formGroup = service.createInvoiceItemTypeMapFormGroup(sampleWithNewData);

        const invoiceItemTypeMap = service.getInvoiceItemTypeMap(formGroup);

        expect(invoiceItemTypeMap).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceItemTypeMap for empty InvoiceItemTypeMap initial value', () => {
        const formGroup = service.createInvoiceItemTypeMapFormGroup();

        const invoiceItemTypeMap = service.getInvoiceItemTypeMap(formGroup);

        expect(invoiceItemTypeMap).toMatchObject({});
      });

      it('should return IInvoiceItemTypeMap', () => {
        const formGroup = service.createInvoiceItemTypeMapFormGroup(sampleWithRequiredData);

        const invoiceItemTypeMap = service.getInvoiceItemTypeMap(formGroup);

        expect(invoiceItemTypeMap).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceItemTypeMap should not enable id FormControl', () => {
        const formGroup = service.createInvoiceItemTypeMapFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceItemTypeMap should disable id FormControl', () => {
        const formGroup = service.createInvoiceItemTypeMapFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
