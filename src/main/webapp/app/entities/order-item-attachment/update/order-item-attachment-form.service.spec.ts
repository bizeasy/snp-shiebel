import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-item-attachment.test-samples';

import { OrderItemAttachmentFormService } from './order-item-attachment-form.service';

describe('OrderItemAttachment Form Service', () => {
  let service: OrderItemAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderItemAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createOrderItemAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderItemAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            orderItem: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing IOrderItemAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createOrderItemAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            orderItem: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderItemAttachment', () => {
      it('should return NewOrderItemAttachment for default OrderItemAttachment initial value', () => {
        const formGroup = service.createOrderItemAttachmentFormGroup(sampleWithNewData);

        const orderItemAttachment = service.getOrderItemAttachment(formGroup);

        expect(orderItemAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderItemAttachment for empty OrderItemAttachment initial value', () => {
        const formGroup = service.createOrderItemAttachmentFormGroup();

        const orderItemAttachment = service.getOrderItemAttachment(formGroup);

        expect(orderItemAttachment).toMatchObject({});
      });

      it('should return IOrderItemAttachment', () => {
        const formGroup = service.createOrderItemAttachmentFormGroup(sampleWithRequiredData);

        const orderItemAttachment = service.getOrderItemAttachment(formGroup);

        expect(orderItemAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderItemAttachment should not enable id FormControl', () => {
        const formGroup = service.createOrderItemAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderItemAttachment should disable id FormControl', () => {
        const formGroup = service.createOrderItemAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
