import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../invoice-role.test-samples';

import { InvoiceRoleFormService } from './invoice-role-form.service';

describe('InvoiceRole Form Service', () => {
  let service: InvoiceRoleFormService;

  beforeEach(() => {
    service = TestBed.inject(InvoiceRoleFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing IInvoiceRole should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItem: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoiceRole', () => {
      it('should return NewInvoiceRole for default InvoiceRole initial value', () => {
        const formGroup = service.createInvoiceRoleFormGroup(sampleWithNewData);

        const invoiceRole = service.getInvoiceRole(formGroup);

        expect(invoiceRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceRole for empty InvoiceRole initial value', () => {
        const formGroup = service.createInvoiceRoleFormGroup();

        const invoiceRole = service.getInvoiceRole(formGroup);

        expect(invoiceRole).toMatchObject({});
      });

      it('should return IInvoiceRole', () => {
        const formGroup = service.createInvoiceRoleFormGroup(sampleWithRequiredData);

        const invoiceRole = service.getInvoiceRole(formGroup);

        expect(invoiceRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceRole should not enable id FormControl', () => {
        const formGroup = service.createInvoiceRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceRole should disable id FormControl', () => {
        const formGroup = service.createInvoiceRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
