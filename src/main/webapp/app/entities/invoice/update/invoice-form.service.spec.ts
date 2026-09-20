import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../invoice.test-samples';

import { InvoiceFormService } from './invoice-form.service';

describe('Invoice Form Service', () => {
  let service: InvoiceFormService;

  beforeEach(() => {
    service = TestBed.inject(InvoiceFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceDate: expect.any(Object),
            dueDate: expect.any(Object),
            paidDate: expect.any(Object),
            referenceNumber: expect.any(Object),
            invoiceMessage: expect.any(Object),
            externalId: expect.any(Object),
            entryDate: expect.any(Object),
            grandTotal: expect.any(Object),
            projectName: expect.any(Object),
            description: expect.any(Object),
            remarks: expect.any(Object),
            paymentTerms: expect.any(Object),
            deliveryTerms: expect.any(Object),
            info: expect.any(Object),
            invoiceType: expect.any(Object),
            order: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            roleType: expect.any(Object),
            status: expect.any(Object),
            contactMech: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });

      it('passing IInvoice should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceDate: expect.any(Object),
            dueDate: expect.any(Object),
            paidDate: expect.any(Object),
            referenceNumber: expect.any(Object),
            invoiceMessage: expect.any(Object),
            externalId: expect.any(Object),
            entryDate: expect.any(Object),
            grandTotal: expect.any(Object),
            projectName: expect.any(Object),
            description: expect.any(Object),
            remarks: expect.any(Object),
            paymentTerms: expect.any(Object),
            deliveryTerms: expect.any(Object),
            info: expect.any(Object),
            invoiceType: expect.any(Object),
            order: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
            roleType: expect.any(Object),
            status: expect.any(Object),
            contactMech: expect.any(Object),
            currencyUom: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoice', () => {
      it('should return NewInvoice for default Invoice initial value', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithNewData);

        const invoice = service.getInvoice(formGroup);

        expect(invoice).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoice for empty Invoice initial value', () => {
        const formGroup = service.createInvoiceFormGroup();

        const invoice = service.getInvoice(formGroup);

        expect(invoice).toMatchObject({});
      });

      it('should return IInvoice', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);

        const invoice = service.getInvoice(formGroup);

        expect(invoice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoice should not enable id FormControl', () => {
        const formGroup = service.createInvoiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoice should disable id FormControl', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
