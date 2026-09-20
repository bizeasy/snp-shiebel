import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inventory-item.test-samples';

import { InventoryItemFormService } from './inventory-item-form.service';

describe('InventoryItem Form Service', () => {
  let service: InventoryItemFormService;

  beforeEach(() => {
    service = TestBed.inject(InventoryItemFormService);
  });

  describe('Service methods', () => {
    describe('createInventoryItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventoryItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            receivedDate: expect.any(Object),
            manufacturedDate: expect.any(Object),
            expiryDate: expect.any(Object),
            retestDate: expect.any(Object),
            containerId: expect.any(Object),
            batchNo: expect.any(Object),
            mfgBatchNo: expect.any(Object),
            lotNoStr: expect.any(Object),
            binNumber: expect.any(Object),
            comments: expect.any(Object),
            quantityOnHandTotal: expect.any(Object),
            availableToPromiseTotal: expect.any(Object),
            accountingQuantityTotal: expect.any(Object),
            oldQuantityOnHand: expect.any(Object),
            oldAvailableToPromise: expect.any(Object),
            serialNumber: expect.any(Object),
            softIdentifier: expect.any(Object),
            activationNumber: expect.any(Object),
            activationValidThru: expect.any(Object),
            unitCost: expect.any(Object),
            inventoryItemType: expect.any(Object),
            product: expect.any(Object),
            supplier: expect.any(Object),
            ownerParty: expect.any(Object),
            status: expect.any(Object),
            facility: expect.any(Object),
            facilityLocation: expect.any(Object),
            uom: expect.any(Object),
            currencyUom: expect.any(Object),
            lot: expect.any(Object),
          }),
        );
      });

      it('passing IInventoryItem should create a new form with FormGroup', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            receivedDate: expect.any(Object),
            manufacturedDate: expect.any(Object),
            expiryDate: expect.any(Object),
            retestDate: expect.any(Object),
            containerId: expect.any(Object),
            batchNo: expect.any(Object),
            mfgBatchNo: expect.any(Object),
            lotNoStr: expect.any(Object),
            binNumber: expect.any(Object),
            comments: expect.any(Object),
            quantityOnHandTotal: expect.any(Object),
            availableToPromiseTotal: expect.any(Object),
            accountingQuantityTotal: expect.any(Object),
            oldQuantityOnHand: expect.any(Object),
            oldAvailableToPromise: expect.any(Object),
            serialNumber: expect.any(Object),
            softIdentifier: expect.any(Object),
            activationNumber: expect.any(Object),
            activationValidThru: expect.any(Object),
            unitCost: expect.any(Object),
            inventoryItemType: expect.any(Object),
            product: expect.any(Object),
            supplier: expect.any(Object),
            ownerParty: expect.any(Object),
            status: expect.any(Object),
            facility: expect.any(Object),
            facilityLocation: expect.any(Object),
            uom: expect.any(Object),
            currencyUom: expect.any(Object),
            lot: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventoryItem', () => {
      it('should return NewInventoryItem for default InventoryItem initial value', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithNewData);

        const inventoryItem = service.getInventoryItem(formGroup);

        expect(inventoryItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventoryItem for empty InventoryItem initial value', () => {
        const formGroup = service.createInventoryItemFormGroup();

        const inventoryItem = service.getInventoryItem(formGroup);

        expect(inventoryItem).toMatchObject({});
      });

      it('should return IInventoryItem', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithRequiredData);

        const inventoryItem = service.getInventoryItem(formGroup);

        expect(inventoryItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventoryItem should not enable id FormControl', () => {
        const formGroup = service.createInventoryItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventoryItem should disable id FormControl', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
