import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product.test-samples';

import { ProductFormService } from './product-form.service';

describe('Product Form Service', () => {
  let service: ProductFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductFormService);
  });

  describe('Service methods', () => {
    describe('createProductFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            internalName: expect.any(Object),
            brandName: expect.any(Object),
            variant: expect.any(Object),
            sku: expect.any(Object),
            introductionDate: expect.any(Object),
            releaseDate: expect.any(Object),
            quantityIncluded: expect.any(Object),
            piecesIncluded: expect.any(Object),
            weight: expect.any(Object),
            height: expect.any(Object),
            width: expect.any(Object),
            depth: expect.any(Object),
            smallImageUrl: expect.any(Object),
            mediumImageUrl: expect.any(Object),
            largeImageUrl: expect.any(Object),
            detailImageUrl: expect.any(Object),
            originalImageUrl: expect.any(Object),
            altImageUrl1: expect.any(Object),
            altImageUrl2: expect.any(Object),
            altImageUrl3: expect.any(Object),
            altImageUrl4: expect.any(Object),
            altImageUrl5: expect.any(Object),
            altImageUrl6: expect.any(Object),
            videoUrl: expect.any(Object),
            quantity: expect.any(Object),
            price: expect.any(Object),
            cgst: expect.any(Object),
            igst: expect.any(Object),
            sgst: expect.any(Object),
            cgstPercentage: expect.any(Object),
            igstPercentage: expect.any(Object),
            sgstPercentage: expect.any(Object),
            totalPrice: expect.any(Object),
            description: expect.any(Object),
            longDescription: expect.any(Object),
            info: expect.any(Object),
            isVirtual: expect.any(Object),
            isVariant: expect.any(Object),
            requireInventory: expect.any(Object),
            returnable: expect.any(Object),
            isPopular: expect.any(Object),
            isBanner: expect.any(Object),
            bannerUrl: expect.any(Object),
            productType: expect.any(Object),
            primaryProductCategory: expect.any(Object),
            virtualProduct: expect.any(Object),
            inventoryItemType: expect.any(Object),
            taxSlab: expect.any(Object),
            uom: expect.any(Object),
            quantityUom: expect.any(Object),
            weightUom: expect.any(Object),
            sizeUom: expect.any(Object),
          }),
        );
      });

      it('passing IProduct should create a new form with FormGroup', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            internalName: expect.any(Object),
            brandName: expect.any(Object),
            variant: expect.any(Object),
            sku: expect.any(Object),
            introductionDate: expect.any(Object),
            releaseDate: expect.any(Object),
            quantityIncluded: expect.any(Object),
            piecesIncluded: expect.any(Object),
            weight: expect.any(Object),
            height: expect.any(Object),
            width: expect.any(Object),
            depth: expect.any(Object),
            smallImageUrl: expect.any(Object),
            mediumImageUrl: expect.any(Object),
            largeImageUrl: expect.any(Object),
            detailImageUrl: expect.any(Object),
            originalImageUrl: expect.any(Object),
            altImageUrl1: expect.any(Object),
            altImageUrl2: expect.any(Object),
            altImageUrl3: expect.any(Object),
            altImageUrl4: expect.any(Object),
            altImageUrl5: expect.any(Object),
            altImageUrl6: expect.any(Object),
            videoUrl: expect.any(Object),
            quantity: expect.any(Object),
            price: expect.any(Object),
            cgst: expect.any(Object),
            igst: expect.any(Object),
            sgst: expect.any(Object),
            cgstPercentage: expect.any(Object),
            igstPercentage: expect.any(Object),
            sgstPercentage: expect.any(Object),
            totalPrice: expect.any(Object),
            description: expect.any(Object),
            longDescription: expect.any(Object),
            info: expect.any(Object),
            isVirtual: expect.any(Object),
            isVariant: expect.any(Object),
            requireInventory: expect.any(Object),
            returnable: expect.any(Object),
            isPopular: expect.any(Object),
            isBanner: expect.any(Object),
            bannerUrl: expect.any(Object),
            productType: expect.any(Object),
            primaryProductCategory: expect.any(Object),
            virtualProduct: expect.any(Object),
            inventoryItemType: expect.any(Object),
            taxSlab: expect.any(Object),
            uom: expect.any(Object),
            quantityUom: expect.any(Object),
            weightUom: expect.any(Object),
            sizeUom: expect.any(Object),
          }),
        );
      });
    });

    describe('getProduct', () => {
      it('should return NewProduct for default Product initial value', () => {
        const formGroup = service.createProductFormGroup(sampleWithNewData);

        const product = service.getProduct(formGroup);

        expect(product).toMatchObject(sampleWithNewData);
      });

      it('should return NewProduct for empty Product initial value', () => {
        const formGroup = service.createProductFormGroup();

        const product = service.getProduct(formGroup);

        expect(product).toMatchObject({});
      });

      it('should return IProduct', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        const product = service.getProduct(formGroup);

        expect(product).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProduct should not enable id FormControl', () => {
        const formGroup = service.createProductFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProduct should disable id FormControl', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
