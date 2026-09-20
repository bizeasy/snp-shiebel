import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContactMechPurpose, NewContactMechPurpose } from '../contact-mech-purpose.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContactMechPurpose for edit and NewContactMechPurposeFormGroupInput for create.
 */
type ContactMechPurposeFormGroupInput = IContactMechPurpose | PartialWithRequiredKeyOf<NewContactMechPurpose>;

type ContactMechPurposeFormDefaults = Pick<NewContactMechPurpose, 'id'>;

type ContactMechPurposeFormGroupContent = {
  id: FormControl<IContactMechPurpose['id'] | NewContactMechPurpose['id']>;
  name: FormControl<IContactMechPurpose['name']>;
  description: FormControl<IContactMechPurpose['description']>;
};

export type ContactMechPurposeFormGroup = FormGroup<ContactMechPurposeFormGroupContent>;

@Service()
export class ContactMechPurposeFormService {
  createContactMechPurposeFormGroup(contactMechPurpose?: ContactMechPurposeFormGroupInput): ContactMechPurposeFormGroup {
    const contactMechPurposeRawValue = {
      ...this.getFormDefaults(),
      ...(contactMechPurpose ?? { id: null }),
    };

    return new FormGroup<ContactMechPurposeFormGroupContent>({
      id: new FormControl(
        { value: contactMechPurposeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(contactMechPurposeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(contactMechPurposeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getContactMechPurpose(form: ContactMechPurposeFormGroup): IContactMechPurpose | NewContactMechPurpose {
    return form.getRawValue();
  }

  resetForm(form: ContactMechPurposeFormGroup, contactMechPurpose: ContactMechPurposeFormGroupInput): void {
    const contactMechPurposeRawValue = { ...this.getFormDefaults(), ...contactMechPurpose };
    form.reset({
      ...contactMechPurposeRawValue,
      id: { value: contactMechPurposeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ContactMechPurposeFormDefaults {
    return {
      id: null,
    };
  }
}
