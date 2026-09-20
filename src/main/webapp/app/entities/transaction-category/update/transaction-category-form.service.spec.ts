import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transaction-category.test-samples';

import { TransactionCategoryFormService } from './transaction-category-form.service';

describe('TransactionCategory Form Service', () => {
  let service: TransactionCategoryFormService;

  beforeEach(() => {
    service = TestBed.inject(TransactionCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createTransactionCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransactionCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ITransactionCategory should create a new form with FormGroup', () => {
        const formGroup = service.createTransactionCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransactionCategory', () => {
      it('should return NewTransactionCategory for default TransactionCategory initial value', () => {
        const formGroup = service.createTransactionCategoryFormGroup(sampleWithNewData);

        const transactionCategory = service.getTransactionCategory(formGroup);

        expect(transactionCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransactionCategory for empty TransactionCategory initial value', () => {
        const formGroup = service.createTransactionCategoryFormGroup();

        const transactionCategory = service.getTransactionCategory(formGroup);

        expect(transactionCategory).toMatchObject({});
      });

      it('should return ITransactionCategory', () => {
        const formGroup = service.createTransactionCategoryFormGroup(sampleWithRequiredData);

        const transactionCategory = service.getTransactionCategory(formGroup);

        expect(transactionCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransactionCategory should not enable id FormControl', () => {
        const formGroup = service.createTransactionCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransactionCategory should disable id FormControl', () => {
        const formGroup = service.createTransactionCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
