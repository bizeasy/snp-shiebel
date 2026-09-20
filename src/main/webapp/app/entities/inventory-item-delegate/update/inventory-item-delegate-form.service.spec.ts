import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inventory-item-delegate.test-samples';

import { InventoryItemDelegateFormService } from './inventory-item-delegate-form.service';

describe('InventoryItemDelegate Form Service', () => {
  let service: InventoryItemDelegateFormService;

  beforeEach(() => {
    service = TestBed.inject(InventoryItemDelegateFormService);
  });

  describe('Service methods', () => {
    describe('createInventoryItemDelegateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventoryItemDelegateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            effectiveDate: expect.any(Object),
            quantityOnHandDiff: expect.any(Object),
            availableToPromiseDiff: expect.any(Object),
            accountingQuantityDiff: expect.any(Object),
            unitCost: expect.any(Object),
            remarks: expect.any(Object),
            inventoryItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItem: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
            itemIssuance: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });

      it('passing IInventoryItemDelegate should create a new form with FormGroup', () => {
        const formGroup = service.createInventoryItemDelegateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            effectiveDate: expect.any(Object),
            quantityOnHandDiff: expect.any(Object),
            availableToPromiseDiff: expect.any(Object),
            accountingQuantityDiff: expect.any(Object),
            unitCost: expect.any(Object),
            remarks: expect.any(Object),
            inventoryItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            invoice: expect.any(Object),
            invoiceItem: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
            itemIssuance: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventoryItemDelegate', () => {
      it('should return NewInventoryItemDelegate for default InventoryItemDelegate initial value', () => {
        const formGroup = service.createInventoryItemDelegateFormGroup(sampleWithNewData);

        const inventoryItemDelegate = service.getInventoryItemDelegate(formGroup);

        expect(inventoryItemDelegate).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventoryItemDelegate for empty InventoryItemDelegate initial value', () => {
        const formGroup = service.createInventoryItemDelegateFormGroup();

        const inventoryItemDelegate = service.getInventoryItemDelegate(formGroup);

        expect(inventoryItemDelegate).toMatchObject({});
      });

      it('should return IInventoryItemDelegate', () => {
        const formGroup = service.createInventoryItemDelegateFormGroup(sampleWithRequiredData);

        const inventoryItemDelegate = service.getInventoryItemDelegate(formGroup);

        expect(inventoryItemDelegate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventoryItemDelegate should not enable id FormControl', () => {
        const formGroup = service.createInventoryItemDelegateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventoryItemDelegate should disable id FormControl', () => {
        const formGroup = service.createInventoryItemDelegateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
