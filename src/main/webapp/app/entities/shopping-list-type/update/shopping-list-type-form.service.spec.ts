import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../shopping-list-type.test-samples';

import { ShoppingListTypeFormService } from './shopping-list-type-form.service';

describe('ShoppingListType Form Service', () => {
  let service: ShoppingListTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ShoppingListTypeFormService);
  });

  describe('Service methods', () => {
    describe('createShoppingListTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShoppingListTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IShoppingListType should create a new form with FormGroup', () => {
        const formGroup = service.createShoppingListTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getShoppingListType', () => {
      it('should return NewShoppingListType for default ShoppingListType initial value', () => {
        const formGroup = service.createShoppingListTypeFormGroup(sampleWithNewData);

        const shoppingListType = service.getShoppingListType(formGroup);

        expect(shoppingListType).toMatchObject(sampleWithNewData);
      });

      it('should return NewShoppingListType for empty ShoppingListType initial value', () => {
        const formGroup = service.createShoppingListTypeFormGroup();

        const shoppingListType = service.getShoppingListType(formGroup);

        expect(shoppingListType).toMatchObject({});
      });

      it('should return IShoppingListType', () => {
        const formGroup = service.createShoppingListTypeFormGroup(sampleWithRequiredData);

        const shoppingListType = service.getShoppingListType(formGroup);

        expect(shoppingListType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShoppingListType should not enable id FormControl', () => {
        const formGroup = service.createShoppingListTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShoppingListType should disable id FormControl', () => {
        const formGroup = service.createShoppingListTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
