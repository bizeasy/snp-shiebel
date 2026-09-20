import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shopping-list.test-samples';

import { ShoppingListFormService } from './shopping-list-form.service';

describe('ShoppingList Form Service', () => {
  let service: ShoppingListFormService;

  beforeEach(() => {
    service = TestBed.inject(ShoppingListFormService);
  });

  describe('Service methods', () => {
    describe('createShoppingListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShoppingListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
            shoppingListType: expect.any(Object),
            party: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IShoppingList should create a new form with FormGroup', () => {
        const formGroup = service.createShoppingListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            isActive: expect.any(Object),
            shoppingListType: expect.any(Object),
            party: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getShoppingList', () => {
      it('should return NewShoppingList for default ShoppingList initial value', () => {
        const formGroup = service.createShoppingListFormGroup(sampleWithNewData);

        const shoppingList = service.getShoppingList(formGroup);

        expect(shoppingList).toMatchObject(sampleWithNewData);
      });

      it('should return NewShoppingList for empty ShoppingList initial value', () => {
        const formGroup = service.createShoppingListFormGroup();

        const shoppingList = service.getShoppingList(formGroup);

        expect(shoppingList).toMatchObject({});
      });

      it('should return IShoppingList', () => {
        const formGroup = service.createShoppingListFormGroup(sampleWithRequiredData);

        const shoppingList = service.getShoppingList(formGroup);

        expect(shoppingList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShoppingList should not enable id FormControl', () => {
        const formGroup = service.createShoppingListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShoppingList should disable id FormControl', () => {
        const formGroup = service.createShoppingListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
