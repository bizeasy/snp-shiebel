import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cash-book.test-samples';

import { CashBookFormService } from './cash-book-form.service';

describe('CashBook Form Service', () => {
  let service: CashBookFormService;

  beforeEach(() => {
    service = TestBed.inject(CashBookFormService);
  });

  describe('Service methods', () => {
    describe('createCashBookFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCashBookFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionDate: expect.any(Object),
            dueDate: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            vendorCustomerName: expect.any(Object),
            invoiceNo: expect.any(Object),
            invoiceDate: expect.any(Object),
            purpose: expect.any(Object),
            remarks: expect.any(Object),
            project: expect.any(Object),
            usageUnits: expect.any(Object),
            recurringTransaction: expect.any(Object),
            frequency: expect.any(Object),
            transactionType: expect.any(Object),
            transactionCategory: expect.any(Object),
            paymentMethodType: expect.any(Object),
            department: expect.any(Object),
            currencyUom: expect.any(Object),
            status: expect.any(Object),
            order: expect.any(Object),
            invoice: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
          }),
        );
      });

      it('passing ICashBook should create a new form with FormGroup', () => {
        const formGroup = service.createCashBookFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            transactionDate: expect.any(Object),
            dueDate: expect.any(Object),
            amount: expect.any(Object),
            description: expect.any(Object),
            vendorCustomerName: expect.any(Object),
            invoiceNo: expect.any(Object),
            invoiceDate: expect.any(Object),
            purpose: expect.any(Object),
            remarks: expect.any(Object),
            project: expect.any(Object),
            usageUnits: expect.any(Object),
            recurringTransaction: expect.any(Object),
            frequency: expect.any(Object),
            transactionType: expect.any(Object),
            transactionCategory: expect.any(Object),
            paymentMethodType: expect.any(Object),
            department: expect.any(Object),
            currencyUom: expect.any(Object),
            status: expect.any(Object),
            order: expect.any(Object),
            invoice: expect.any(Object),
            fromParty: expect.any(Object),
            toParty: expect.any(Object),
          }),
        );
      });
    });

    describe('getCashBook', () => {
      it('should return NewCashBook for default CashBook initial value', () => {
        const formGroup = service.createCashBookFormGroup(sampleWithNewData);

        const cashBook = service.getCashBook(formGroup);

        expect(cashBook).toMatchObject(sampleWithNewData);
      });

      it('should return NewCashBook for empty CashBook initial value', () => {
        const formGroup = service.createCashBookFormGroup();

        const cashBook = service.getCashBook(formGroup);

        expect(cashBook).toMatchObject({});
      });

      it('should return ICashBook', () => {
        const formGroup = service.createCashBookFormGroup(sampleWithRequiredData);

        const cashBook = service.getCashBook(formGroup);

        expect(cashBook).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICashBook should not enable id FormControl', () => {
        const formGroup = service.createCashBookFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCashBook should disable id FormControl', () => {
        const formGroup = service.createCashBookFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
