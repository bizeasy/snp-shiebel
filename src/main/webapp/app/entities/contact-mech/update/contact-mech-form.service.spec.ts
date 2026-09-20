import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../contact-mech.test-samples';

import { ContactMechFormService } from './contact-mech-form.service';

describe('ContactMech Form Service', () => {
  let service: ContactMechFormService;

  beforeEach(() => {
    service = TestBed.inject(ContactMechFormService);
  });

  describe('Service methods', () => {
    describe('createContactMechFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContactMechFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            infoString: expect.any(Object),
            contactMechType: expect.any(Object),
          }),
        );
      });

      it('passing IContactMech should create a new form with FormGroup', () => {
        const formGroup = service.createContactMechFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            infoString: expect.any(Object),
            contactMechType: expect.any(Object),
          }),
        );
      });
    });

    describe('getContactMech', () => {
      it('should return NewContactMech for default ContactMech initial value', () => {
        const formGroup = service.createContactMechFormGroup(sampleWithNewData);

        const contactMech = service.getContactMech(formGroup);

        expect(contactMech).toMatchObject(sampleWithNewData);
      });

      it('should return NewContactMech for empty ContactMech initial value', () => {
        const formGroup = service.createContactMechFormGroup();

        const contactMech = service.getContactMech(formGroup);

        expect(contactMech).toMatchObject({});
      });

      it('should return IContactMech', () => {
        const formGroup = service.createContactMechFormGroup(sampleWithRequiredData);

        const contactMech = service.getContactMech(formGroup);

        expect(contactMech).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContactMech should not enable id FormControl', () => {
        const formGroup = service.createContactMechFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContactMech should disable id FormControl', () => {
        const formGroup = service.createContactMechFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
