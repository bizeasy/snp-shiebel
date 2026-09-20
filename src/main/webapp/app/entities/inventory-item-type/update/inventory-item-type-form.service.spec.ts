import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inventory-item-type.test-samples';

import { InventoryItemTypeFormService } from './inventory-item-type-form.service';

describe('InventoryItemType Form Service', () => {
  let service: InventoryItemTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(InventoryItemTypeFormService);
  });

  describe('Service methods', () => {
    describe('createInventoryItemTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventoryItemTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IInventoryItemType should create a new form with FormGroup', () => {
        const formGroup = service.createInventoryItemTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventoryItemType', () => {
      it('should return NewInventoryItemType for default InventoryItemType initial value', () => {
        const formGroup = service.createInventoryItemTypeFormGroup(sampleWithNewData);

        const inventoryItemType = service.getInventoryItemType(formGroup);

        expect(inventoryItemType).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventoryItemType for empty InventoryItemType initial value', () => {
        const formGroup = service.createInventoryItemTypeFormGroup();

        const inventoryItemType = service.getInventoryItemType(formGroup);

        expect(inventoryItemType).toMatchObject({});
      });

      it('should return IInventoryItemType', () => {
        const formGroup = service.createInventoryItemTypeFormGroup(sampleWithRequiredData);

        const inventoryItemType = service.getInventoryItemType(formGroup);

        expect(inventoryItemType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventoryItemType should not enable id FormControl', () => {
        const formGroup = service.createInventoryItemTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventoryItemType should disable id FormControl', () => {
        const formGroup = service.createInventoryItemTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
