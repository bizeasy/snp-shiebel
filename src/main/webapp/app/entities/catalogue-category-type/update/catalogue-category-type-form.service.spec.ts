import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../catalogue-category-type.test-samples';

import { CatalogueCategoryTypeFormService } from './catalogue-category-type-form.service';

describe('CatalogueCategoryType Form Service', () => {
  let service: CatalogueCategoryTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(CatalogueCategoryTypeFormService);
  });

  describe('Service methods', () => {
    describe('createCatalogueCategoryTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCatalogueCategoryTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ICatalogueCategoryType should create a new form with FormGroup', () => {
        const formGroup = service.createCatalogueCategoryTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getCatalogueCategoryType', () => {
      it('should return NewCatalogueCategoryType for default CatalogueCategoryType initial value', () => {
        const formGroup = service.createCatalogueCategoryTypeFormGroup(sampleWithNewData);

        const catalogueCategoryType = service.getCatalogueCategoryType(formGroup);

        expect(catalogueCategoryType).toMatchObject(sampleWithNewData);
      });

      it('should return NewCatalogueCategoryType for empty CatalogueCategoryType initial value', () => {
        const formGroup = service.createCatalogueCategoryTypeFormGroup();

        const catalogueCategoryType = service.getCatalogueCategoryType(formGroup);

        expect(catalogueCategoryType).toMatchObject({});
      });

      it('should return ICatalogueCategoryType', () => {
        const formGroup = service.createCatalogueCategoryTypeFormGroup(sampleWithRequiredData);

        const catalogueCategoryType = service.getCatalogueCategoryType(formGroup);

        expect(catalogueCategoryType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICatalogueCategoryType should not enable id FormControl', () => {
        const formGroup = service.createCatalogueCategoryTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCatalogueCategoryType should disable id FormControl', () => {
        const formGroup = service.createCatalogueCategoryTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
