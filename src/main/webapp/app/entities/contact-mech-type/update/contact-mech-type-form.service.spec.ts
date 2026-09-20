import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../contact-mech-type.test-samples';

import { ContactMechTypeFormService } from './contact-mech-type-form.service';

describe('ContactMechType Form Service', () => {
  let service: ContactMechTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ContactMechTypeFormService);
  });

  describe('Service methods', () => {
    describe('createContactMechTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContactMechTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IContactMechType should create a new form with FormGroup', () => {
        const formGroup = service.createContactMechTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getContactMechType', () => {
      it('should return NewContactMechType for default ContactMechType initial value', () => {
        const formGroup = service.createContactMechTypeFormGroup(sampleWithNewData);

        const contactMechType = service.getContactMechType(formGroup);

        expect(contactMechType).toMatchObject(sampleWithNewData);
      });

      it('should return NewContactMechType for empty ContactMechType initial value', () => {
        const formGroup = service.createContactMechTypeFormGroup();

        const contactMechType = service.getContactMechType(formGroup);

        expect(contactMechType).toMatchObject({});
      });

      it('should return IContactMechType', () => {
        const formGroup = service.createContactMechTypeFormGroup(sampleWithRequiredData);

        const contactMechType = service.getContactMechType(formGroup);

        expect(contactMechType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContactMechType should not enable id FormControl', () => {
        const formGroup = service.createContactMechTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContactMechType should disable id FormControl', () => {
        const formGroup = service.createContactMechTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
