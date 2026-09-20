import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cash-book-attachment.test-samples';

import { CashBookAttachmentFormService } from './cash-book-attachment-form.service';

describe('CashBookAttachment Form Service', () => {
  let service: CashBookAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(CashBookAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createCashBookAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCashBookAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cashBook: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing ICashBookAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createCashBookAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cashBook: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getCashBookAttachment', () => {
      it('should return NewCashBookAttachment for default CashBookAttachment initial value', () => {
        const formGroup = service.createCashBookAttachmentFormGroup(sampleWithNewData);

        const cashBookAttachment = service.getCashBookAttachment(formGroup);

        expect(cashBookAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewCashBookAttachment for empty CashBookAttachment initial value', () => {
        const formGroup = service.createCashBookAttachmentFormGroup();

        const cashBookAttachment = service.getCashBookAttachment(formGroup);

        expect(cashBookAttachment).toMatchObject({});
      });

      it('should return ICashBookAttachment', () => {
        const formGroup = service.createCashBookAttachmentFormGroup(sampleWithRequiredData);

        const cashBookAttachment = service.getCashBookAttachment(formGroup);

        expect(cashBookAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICashBookAttachment should not enable id FormControl', () => {
        const formGroup = service.createCashBookAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCashBookAttachment should disable id FormControl', () => {
        const formGroup = service.createCashBookAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
