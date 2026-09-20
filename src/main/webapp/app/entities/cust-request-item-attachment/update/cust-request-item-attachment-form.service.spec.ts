import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cust-request-item-attachment.test-samples';

import { CustRequestItemAttachmentFormService } from './cust-request-item-attachment-form.service';

describe('CustRequestItemAttachment Form Service', () => {
  let service: CustRequestItemAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(CustRequestItemAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createCustRequestItemAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustRequestItemAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            custRequestItem: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing ICustRequestItemAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createCustRequestItemAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            custRequestItem: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustRequestItemAttachment', () => {
      it('should return NewCustRequestItemAttachment for default CustRequestItemAttachment initial value', () => {
        const formGroup = service.createCustRequestItemAttachmentFormGroup(sampleWithNewData);

        const custRequestItemAttachment = service.getCustRequestItemAttachment(formGroup);

        expect(custRequestItemAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustRequestItemAttachment for empty CustRequestItemAttachment initial value', () => {
        const formGroup = service.createCustRequestItemAttachmentFormGroup();

        const custRequestItemAttachment = service.getCustRequestItemAttachment(formGroup);

        expect(custRequestItemAttachment).toMatchObject({});
      });

      it('should return ICustRequestItemAttachment', () => {
        const formGroup = service.createCustRequestItemAttachmentFormGroup(sampleWithRequiredData);

        const custRequestItemAttachment = service.getCustRequestItemAttachment(formGroup);

        expect(custRequestItemAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustRequestItemAttachment should not enable id FormControl', () => {
        const formGroup = service.createCustRequestItemAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustRequestItemAttachment should disable id FormControl', () => {
        const formGroup = service.createCustRequestItemAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
