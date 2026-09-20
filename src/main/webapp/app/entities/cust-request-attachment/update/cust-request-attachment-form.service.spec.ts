import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cust-request-attachment.test-samples';

import { CustRequestAttachmentFormService } from './cust-request-attachment-form.service';

describe('CustRequestAttachment Form Service', () => {
  let service: CustRequestAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(CustRequestAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createCustRequestAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustRequestAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            custRequest: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing ICustRequestAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createCustRequestAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            custRequest: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustRequestAttachment', () => {
      it('should return NewCustRequestAttachment for default CustRequestAttachment initial value', () => {
        const formGroup = service.createCustRequestAttachmentFormGroup(sampleWithNewData);

        const custRequestAttachment = service.getCustRequestAttachment(formGroup);

        expect(custRequestAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustRequestAttachment for empty CustRequestAttachment initial value', () => {
        const formGroup = service.createCustRequestAttachmentFormGroup();

        const custRequestAttachment = service.getCustRequestAttachment(formGroup);

        expect(custRequestAttachment).toMatchObject({});
      });

      it('should return ICustRequestAttachment', () => {
        const formGroup = service.createCustRequestAttachmentFormGroup(sampleWithRequiredData);

        const custRequestAttachment = service.getCustRequestAttachment(formGroup);

        expect(custRequestAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustRequestAttachment should not enable id FormControl', () => {
        const formGroup = service.createCustRequestAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustRequestAttachment should disable id FormControl', () => {
        const formGroup = service.createCustRequestAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
