import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../catalogue-category.test-samples';

import { CatalogueCategoryFormService } from './catalogue-category-form.service';

describe('CatalogueCategory Form Service', () => {
  let service: CatalogueCategoryFormService;

  beforeEach(() => {
    service = TestBed.inject(CatalogueCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createCatalogueCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCatalogueCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            catalogue: expect.any(Object),
            productCategory: expect.any(Object),
            catalogueCategoryType: expect.any(Object),
          }),
        );
      });

      it('passing ICatalogueCategory should create a new form with FormGroup', () => {
        const formGroup = service.createCatalogueCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            catalogue: expect.any(Object),
            productCategory: expect.any(Object),
            catalogueCategoryType: expect.any(Object),
          }),
        );
      });
    });

    describe('getCatalogueCategory', () => {
      it('should return NewCatalogueCategory for default CatalogueCategory initial value', () => {
        const formGroup = service.createCatalogueCategoryFormGroup(sampleWithNewData);

        const catalogueCategory = service.getCatalogueCategory(formGroup);

        expect(catalogueCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewCatalogueCategory for empty CatalogueCategory initial value', () => {
        const formGroup = service.createCatalogueCategoryFormGroup();

        const catalogueCategory = service.getCatalogueCategory(formGroup);

        expect(catalogueCategory).toMatchObject({});
      });

      it('should return ICatalogueCategory', () => {
        const formGroup = service.createCatalogueCategoryFormGroup(sampleWithRequiredData);

        const catalogueCategory = service.getCatalogueCategory(formGroup);

        expect(catalogueCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICatalogueCategory should not enable id FormControl', () => {
        const formGroup = service.createCatalogueCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCatalogueCategory should disable id FormControl', () => {
        const formGroup = service.createCatalogueCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
