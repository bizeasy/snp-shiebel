import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../contact-mech-purpose.test-samples';

import { ContactMechPurposeFormService } from './contact-mech-purpose-form.service';

describe('ContactMechPurpose Form Service', () => {
  let service: ContactMechPurposeFormService;

  beforeEach(() => {
    service = TestBed.inject(ContactMechPurposeFormService);
  });

  describe('Service methods', () => {
    describe('createContactMechPurposeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContactMechPurposeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IContactMechPurpose should create a new form with FormGroup', () => {
        const formGroup = service.createContactMechPurposeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getContactMechPurpose', () => {
      it('should return NewContactMechPurpose for default ContactMechPurpose initial value', () => {
        const formGroup = service.createContactMechPurposeFormGroup(sampleWithNewData);

        const contactMechPurpose = service.getContactMechPurpose(formGroup);

        expect(contactMechPurpose).toMatchObject(sampleWithNewData);
      });

      it('should return NewContactMechPurpose for empty ContactMechPurpose initial value', () => {
        const formGroup = service.createContactMechPurposeFormGroup();

        const contactMechPurpose = service.getContactMechPurpose(formGroup);

        expect(contactMechPurpose).toMatchObject({});
      });

      it('should return IContactMechPurpose', () => {
        const formGroup = service.createContactMechPurposeFormGroup(sampleWithRequiredData);

        const contactMechPurpose = service.getContactMechPurpose(formGroup);

        expect(contactMechPurpose).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContactMechPurpose should not enable id FormControl', () => {
        const formGroup = service.createContactMechPurposeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContactMechPurpose should disable id FormControl', () => {
        const formGroup = service.createContactMechPurposeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
