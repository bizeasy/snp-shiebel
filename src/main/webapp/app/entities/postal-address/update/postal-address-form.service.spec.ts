import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../postal-address.test-samples';

import { PostalAddressFormService } from './postal-address-form.service';

describe('PostalAddress Form Service', () => {
  let service: PostalAddressFormService;

  beforeEach(() => {
    service = TestBed.inject(PostalAddressFormService);
  });

  describe('Service methods', () => {
    describe('createPostalAddressFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPostalAddressFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            toName: expect.any(Object),
            address1: expect.any(Object),
            address2: expect.any(Object),
            city: expect.any(Object),
            landmark: expect.any(Object),
            postalCode: expect.any(Object),
            isDefault: expect.any(Object),
            customAddressType: expect.any(Object),
            stateStr: expect.any(Object),
            countryStr: expect.any(Object),
            isIndianAddress: expect.any(Object),
            note: expect.any(Object),
            directions: expect.any(Object),
            contactMech: expect.any(Object),
            state: expect.any(Object),
            pincode: expect.any(Object),
            country: expect.any(Object),
            geoPoint: expect.any(Object),
          }),
        );
      });

      it('passing IPostalAddress should create a new form with FormGroup', () => {
        const formGroup = service.createPostalAddressFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            toName: expect.any(Object),
            address1: expect.any(Object),
            address2: expect.any(Object),
            city: expect.any(Object),
            landmark: expect.any(Object),
            postalCode: expect.any(Object),
            isDefault: expect.any(Object),
            customAddressType: expect.any(Object),
            stateStr: expect.any(Object),
            countryStr: expect.any(Object),
            isIndianAddress: expect.any(Object),
            note: expect.any(Object),
            directions: expect.any(Object),
            contactMech: expect.any(Object),
            state: expect.any(Object),
            pincode: expect.any(Object),
            country: expect.any(Object),
            geoPoint: expect.any(Object),
          }),
        );
      });
    });

    describe('getPostalAddress', () => {
      it('should return NewPostalAddress for default PostalAddress initial value', () => {
        const formGroup = service.createPostalAddressFormGroup(sampleWithNewData);

        const postalAddress = service.getPostalAddress(formGroup);

        expect(postalAddress).toMatchObject(sampleWithNewData);
      });

      it('should return NewPostalAddress for empty PostalAddress initial value', () => {
        const formGroup = service.createPostalAddressFormGroup();

        const postalAddress = service.getPostalAddress(formGroup);

        expect(postalAddress).toMatchObject({});
      });

      it('should return IPostalAddress', () => {
        const formGroup = service.createPostalAddressFormGroup(sampleWithRequiredData);

        const postalAddress = service.getPostalAddress(formGroup);

        expect(postalAddress).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPostalAddress should not enable id FormControl', () => {
        const formGroup = service.createPostalAddressFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPostalAddress should disable id FormControl', () => {
        const formGroup = service.createPostalAddressFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
