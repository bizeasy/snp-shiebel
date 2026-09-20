import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shopping-list-item.test-samples';

import { ShoppingListItemFormService } from './shopping-list-item-form.service';

describe('ShoppingListItem Form Service', () => {
  let service: ShoppingListItemFormService;

  beforeEach(() => {
    service = TestBed.inject(ShoppingListItemFormService);
  });

  describe('Service methods', () => {
    describe('createShoppingListItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShoppingListItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            requiredByDate: expect.any(Object),
            selectedAmount: expect.any(Object),
            maxAmount: expect.any(Object),
            description: expect.any(Object),
            story: expect.any(Object),
            shoppingList: expect.any(Object),
            product: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IShoppingListItem should create a new form with FormGroup', () => {
        const formGroup = service.createShoppingListItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            quantity: expect.any(Object),
            requiredByDate: expect.any(Object),
            selectedAmount: expect.any(Object),
            maxAmount: expect.any(Object),
            description: expect.any(Object),
            story: expect.any(Object),
            shoppingList: expect.any(Object),
            product: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getShoppingListItem', () => {
      it('should return NewShoppingListItem for default ShoppingListItem initial value', () => {
        const formGroup = service.createShoppingListItemFormGroup(sampleWithNewData);

        const shoppingListItem = service.getShoppingListItem(formGroup);

        expect(shoppingListItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewShoppingListItem for empty ShoppingListItem initial value', () => {
        const formGroup = service.createShoppingListItemFormGroup();

        const shoppingListItem = service.getShoppingListItem(formGroup);

        expect(shoppingListItem).toMatchObject({});
      });

      it('should return IShoppingListItem', () => {
        const formGroup = service.createShoppingListItemFormGroup(sampleWithRequiredData);

        const shoppingListItem = service.getShoppingListItem(formGroup);

        expect(shoppingListItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShoppingListItem should not enable id FormControl', () => {
        const formGroup = service.createShoppingListItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShoppingListItem should disable id FormControl', () => {
        const formGroup = service.createShoppingListItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
