import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-review.test-samples';

import { ProductReviewFormService } from './product-review-form.service';

describe('ProductReview Form Service', () => {
  let service: ProductReviewFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductReviewFormService);
  });

  describe('Service methods', () => {
    describe('createProductReviewFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductReviewFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            ratingInfo: expect.any(Object),
            message: expect.any(Object),
            reviewDate: expect.any(Object),
            product: expect.any(Object),
            party: expect.any(Object),
            orderItem: expect.any(Object),
          }),
        );
      });

      it('passing IProductReview should create a new form with FormGroup', () => {
        const formGroup = service.createProductReviewFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            ratingInfo: expect.any(Object),
            message: expect.any(Object),
            reviewDate: expect.any(Object),
            product: expect.any(Object),
            party: expect.any(Object),
            orderItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductReview', () => {
      it('should return NewProductReview for default ProductReview initial value', () => {
        const formGroup = service.createProductReviewFormGroup(sampleWithNewData);

        const productReview = service.getProductReview(formGroup);

        expect(productReview).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductReview for empty ProductReview initial value', () => {
        const formGroup = service.createProductReviewFormGroup();

        const productReview = service.getProductReview(formGroup);

        expect(productReview).toMatchObject({});
      });

      it('should return IProductReview', () => {
        const formGroup = service.createProductReviewFormGroup(sampleWithRequiredData);

        const productReview = service.getProductReview(formGroup);

        expect(productReview).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductReview should not enable id FormControl', () => {
        const formGroup = service.createProductReviewFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductReview should disable id FormControl', () => {
        const formGroup = service.createProductReviewFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
