import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../item-issuance.test-samples';

import { ItemIssuanceFormService } from './item-issuance-form.service';

describe('ItemIssuance Form Service', () => {
  let service: ItemIssuanceFormService;

  beforeEach(() => {
    service = TestBed.inject(ItemIssuanceFormService);
  });

  describe('Service methods', () => {
    describe('createItemIssuanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createItemIssuanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            message: expect.any(Object),
            issuedDate: expect.any(Object),
            issuedBy: expect.any(Object),
            quantity: expect.any(Object),
            cancelQuantity: expect.any(Object),
            inventoryItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
            issuedByParty: expect.any(Object),
          }),
        );
      });

      it('passing IItemIssuance should create a new form with FormGroup', () => {
        const formGroup = service.createItemIssuanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            message: expect.any(Object),
            issuedDate: expect.any(Object),
            issuedBy: expect.any(Object),
            quantity: expect.any(Object),
            cancelQuantity: expect.any(Object),
            inventoryItem: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            shipment: expect.any(Object),
            shipmentItem: expect.any(Object),
            issuedByParty: expect.any(Object),
          }),
        );
      });
    });

    describe('getItemIssuance', () => {
      it('should return NewItemIssuance for default ItemIssuance initial value', () => {
        const formGroup = service.createItemIssuanceFormGroup(sampleWithNewData);

        const itemIssuance = service.getItemIssuance(formGroup);

        expect(itemIssuance).toMatchObject(sampleWithNewData);
      });

      it('should return NewItemIssuance for empty ItemIssuance initial value', () => {
        const formGroup = service.createItemIssuanceFormGroup();

        const itemIssuance = service.getItemIssuance(formGroup);

        expect(itemIssuance).toMatchObject({});
      });

      it('should return IItemIssuance', () => {
        const formGroup = service.createItemIssuanceFormGroup(sampleWithRequiredData);

        const itemIssuance = service.getItemIssuance(formGroup);

        expect(itemIssuance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IItemIssuance should not enable id FormControl', () => {
        const formGroup = service.createItemIssuanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewItemIssuance should disable id FormControl', () => {
        const formGroup = service.createItemIssuanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
