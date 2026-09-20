import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-attachment.test-samples';

import { OrderAttachmentFormService } from './order-attachment-form.service';

describe('OrderAttachment Form Service', () => {
  let service: OrderAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createOrderAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            order: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing IOrderAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createOrderAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            order: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderAttachment', () => {
      it('should return NewOrderAttachment for default OrderAttachment initial value', () => {
        const formGroup = service.createOrderAttachmentFormGroup(sampleWithNewData);

        const orderAttachment = service.getOrderAttachment(formGroup);

        expect(orderAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderAttachment for empty OrderAttachment initial value', () => {
        const formGroup = service.createOrderAttachmentFormGroup();

        const orderAttachment = service.getOrderAttachment(formGroup);

        expect(orderAttachment).toMatchObject({});
      });

      it('should return IOrderAttachment', () => {
        const formGroup = service.createOrderAttachmentFormGroup(sampleWithRequiredData);

        const orderAttachment = service.getOrderAttachment(formGroup);

        expect(orderAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderAttachment should not enable id FormControl', () => {
        const formGroup = service.createOrderAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderAttachment should disable id FormControl', () => {
        const formGroup = service.createOrderAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
