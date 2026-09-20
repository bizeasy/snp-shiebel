import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inventory-item-variance.test-samples';

import { InventoryItemVarianceFormService } from './inventory-item-variance-form.service';

describe('InventoryItemVariance Form Service', () => {
  let service: InventoryItemVarianceFormService;

  beforeEach(() => {
    service = TestBed.inject(InventoryItemVarianceFormService);
  });

  describe('Service methods', () => {
    describe('createInventoryItemVarianceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventoryItemVarianceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            varianceReason: expect.any(Object),
            atpVar: expect.any(Object),
            qohVar: expect.any(Object),
            comments: expect.any(Object),
            inventoryItem: expect.any(Object),
          }),
        );
      });

      it('passing IInventoryItemVariance should create a new form with FormGroup', () => {
        const formGroup = service.createInventoryItemVarianceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            varianceReason: expect.any(Object),
            atpVar: expect.any(Object),
            qohVar: expect.any(Object),
            comments: expect.any(Object),
            inventoryItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventoryItemVariance', () => {
      it('should return NewInventoryItemVariance for default InventoryItemVariance initial value', () => {
        const formGroup = service.createInventoryItemVarianceFormGroup(sampleWithNewData);

        const inventoryItemVariance = service.getInventoryItemVariance(formGroup);

        expect(inventoryItemVariance).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventoryItemVariance for empty InventoryItemVariance initial value', () => {
        const formGroup = service.createInventoryItemVarianceFormGroup();

        const inventoryItemVariance = service.getInventoryItemVariance(formGroup);

        expect(inventoryItemVariance).toMatchObject({});
      });

      it('should return IInventoryItemVariance', () => {
        const formGroup = service.createInventoryItemVarianceFormGroup(sampleWithRequiredData);

        const inventoryItemVariance = service.getInventoryItemVariance(formGroup);

        expect(inventoryItemVariance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventoryItemVariance should not enable id FormControl', () => {
        const formGroup = service.createInventoryItemVarianceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventoryItemVariance should disable id FormControl', () => {
        const formGroup = service.createInventoryItemVarianceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
