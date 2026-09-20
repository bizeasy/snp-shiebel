import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pick-list-item.test-samples';

import { PickListItemFormService } from './pick-list-item-form.service';

describe('PickListItem Form Service', () => {
  let service: PickListItemFormService;

  beforeEach(() => {
    service = TestBed.inject(PickListItemFormService);
  });

  describe('Service methods', () => {
    describe('createPickListItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPickListItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            pickList: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            inventoryItem: expect.any(Object),
            product: expect.any(Object),
            facilityLocation: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IPickListItem should create a new form with FormGroup', () => {
        const formGroup = service.createPickListItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            pickList: expect.any(Object),
            order: expect.any(Object),
            orderItem: expect.any(Object),
            inventoryItem: expect.any(Object),
            product: expect.any(Object),
            facilityLocation: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getPickListItem', () => {
      it('should return NewPickListItem for default PickListItem initial value', () => {
        const formGroup = service.createPickListItemFormGroup(sampleWithNewData);

        const pickListItem = service.getPickListItem(formGroup);

        expect(pickListItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewPickListItem for empty PickListItem initial value', () => {
        const formGroup = service.createPickListItemFormGroup();

        const pickListItem = service.getPickListItem(formGroup);

        expect(pickListItem).toMatchObject({});
      });

      it('should return IPickListItem', () => {
        const formGroup = service.createPickListItemFormGroup(sampleWithRequiredData);

        const pickListItem = service.getPickListItem(formGroup);

        expect(pickListItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPickListItem should not enable id FormControl', () => {
        const formGroup = service.createPickListItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPickListItem should disable id FormControl', () => {
        const formGroup = service.createPickListItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
