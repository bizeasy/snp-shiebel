import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../invoice-item.test-samples';

import { InvoiceItemFormService } from './invoice-item-form.service';

describe('InvoiceItem Form Service', () => {
  let service: InvoiceItemFormService;

  beforeEach(() => {
    service = TestBed.inject(InvoiceItemFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            amount: expect.any(Object),
            cancelQuantity: expect.any(Object),
            selectedAmount: expect.any(Object),
            unitPrice: expect.any(Object),
            unitListPrice: expect.any(Object),
            isModifiedPrice: expect.any(Object),
            estimatedShipDate: expect.any(Object),
            estimatedDeliveryDate: expect.any(Object),
            shipBeforeDate: expect.any(Object),
            shipAfterDate: expect.any(Object),
            entryDate: expect.any(Object),
            attr1: expect.any(Object),
            attr2: expect.any(Object),
            attr3: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItemType: expect.any(Object),
            product: expect.any(Object),
            uom: expect.any(Object),
            productFeature: expect.any(Object),
            productFacility: expect.any(Object),
            fromInventoryItem: expect.any(Object),
            taxAuthRate: expect.any(Object),
            parentInvoice: expect.any(Object),
            parentInvoiceItem: expect.any(Object),
          }),
        );
      });

      it('passing IInvoiceItem should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            amount: expect.any(Object),
            cancelQuantity: expect.any(Object),
            selectedAmount: expect.any(Object),
            unitPrice: expect.any(Object),
            unitListPrice: expect.any(Object),
            isModifiedPrice: expect.any(Object),
            estimatedShipDate: expect.any(Object),
            estimatedDeliveryDate: expect.any(Object),
            shipBeforeDate: expect.any(Object),
            shipAfterDate: expect.any(Object),
            entryDate: expect.any(Object),
            attr1: expect.any(Object),
            attr2: expect.any(Object),
            attr3: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItemType: expect.any(Object),
            product: expect.any(Object),
            uom: expect.any(Object),
            productFeature: expect.any(Object),
            productFacility: expect.any(Object),
            fromInventoryItem: expect.any(Object),
            taxAuthRate: expect.any(Object),
            parentInvoice: expect.any(Object),
            parentInvoiceItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoiceItem', () => {
      it('should return NewInvoiceItem for default InvoiceItem initial value', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithNewData);

        const invoiceItem = service.getInvoiceItem(formGroup);

        expect(invoiceItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceItem for empty InvoiceItem initial value', () => {
        const formGroup = service.createInvoiceItemFormGroup();

        const invoiceItem = service.getInvoiceItem(formGroup);

        expect(invoiceItem).toMatchObject({});
      });

      it('should return IInvoiceItem', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithRequiredData);

        const invoiceItem = service.getInvoiceItem(formGroup);

        expect(invoiceItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceItem should not enable id FormControl', () => {
        const formGroup = service.createInvoiceItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceItem should disable id FormControl', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
