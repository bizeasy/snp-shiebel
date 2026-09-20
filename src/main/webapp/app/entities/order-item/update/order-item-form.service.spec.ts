import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-item.test-samples';

import { OrderItemFormService } from './order-item-form.service';

describe('OrderItem Form Service', () => {
  let service: OrderItemFormService;

  beforeEach(() => {
    service = TestBed.inject(OrderItemFormService);
  });

  describe('Service methods', () => {
    describe('createOrderItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
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
            order: expect.any(Object),
            orderItemType: expect.any(Object),
            product: expect.any(Object),
            uom: expect.any(Object),
            supplierProduct: expect.any(Object),
            supplier: expect.any(Object),
            productFeature: expect.any(Object),
            productFacility: expect.any(Object),
            fromInventoryItem: expect.any(Object),
            shoppingListItem: expect.any(Object),
            quoteItem: expect.any(Object),
            status: expect.any(Object),
            taxAuthRate: expect.any(Object),
          }),
        );
      });

      it('passing IOrderItem should create a new form with FormGroup', () => {
        const formGroup = service.createOrderItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
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
            order: expect.any(Object),
            orderItemType: expect.any(Object),
            product: expect.any(Object),
            uom: expect.any(Object),
            supplierProduct: expect.any(Object),
            supplier: expect.any(Object),
            productFeature: expect.any(Object),
            productFacility: expect.any(Object),
            fromInventoryItem: expect.any(Object),
            shoppingListItem: expect.any(Object),
            quoteItem: expect.any(Object),
            status: expect.any(Object),
            taxAuthRate: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderItem', () => {
      it('should return NewOrderItem for default OrderItem initial value', () => {
        const formGroup = service.createOrderItemFormGroup(sampleWithNewData);

        const orderItem = service.getOrderItem(formGroup);

        expect(orderItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderItem for empty OrderItem initial value', () => {
        const formGroup = service.createOrderItemFormGroup();

        const orderItem = service.getOrderItem(formGroup);

        expect(orderItem).toMatchObject({});
      });

      it('should return IOrderItem', () => {
        const formGroup = service.createOrderItemFormGroup(sampleWithRequiredData);

        const orderItem = service.getOrderItem(formGroup);

        expect(orderItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderItem should not enable id FormControl', () => {
        const formGroup = service.createOrderItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderItem should disable id FormControl', () => {
        const formGroup = service.createOrderItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
