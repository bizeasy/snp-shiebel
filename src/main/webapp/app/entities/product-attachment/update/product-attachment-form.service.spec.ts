import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../product-attachment.test-samples';

import { ProductAttachmentFormService } from './product-attachment-form.service';

describe('ProductAttachment Form Service', () => {
  let service: ProductAttachmentFormService;

  beforeEach(() => {
    service = TestBed.inject(ProductAttachmentFormService);
  });

  describe('Service methods', () => {
    describe('createProductAttachmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductAttachmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            product: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });

      it('passing IProductAttachment should create a new form with FormGroup', () => {
        const formGroup = service.createProductAttachmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            product: expect.any(Object),
            attachment: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductAttachment', () => {
      it('should return NewProductAttachment for default ProductAttachment initial value', () => {
        const formGroup = service.createProductAttachmentFormGroup(sampleWithNewData);

        const productAttachment = service.getProductAttachment(formGroup);

        expect(productAttachment).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductAttachment for empty ProductAttachment initial value', () => {
        const formGroup = service.createProductAttachmentFormGroup();

        const productAttachment = service.getProductAttachment(formGroup);

        expect(productAttachment).toMatchObject({});
      });

      it('should return IProductAttachment', () => {
        const formGroup = service.createProductAttachmentFormGroup(sampleWithRequiredData);

        const productAttachment = service.getProductAttachment(formGroup);

        expect(productAttachment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductAttachment should not enable id FormControl', () => {
        const formGroup = service.createProductAttachmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductAttachment should disable id FormControl', () => {
        const formGroup = service.createProductAttachmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
