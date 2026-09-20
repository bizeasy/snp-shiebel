import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContactMechType, NewContactMechType } from '../contact-mech-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContactMechType for edit and NewContactMechTypeFormGroupInput for create.
 */
type ContactMechTypeFormGroupInput = IContactMechType | PartialWithRequiredKeyOf<NewContactMechType>;

type ContactMechTypeFormDefaults = Pick<NewContactMechType, 'id'>;

type ContactMechTypeFormGroupContent = {
  id: FormControl<IContactMechType['id'] | NewContactMechType['id']>;
  name: FormControl<IContactMechType['name']>;
  description: FormControl<IContactMechType['description']>;
};

export type ContactMechTypeFormGroup = FormGroup<ContactMechTypeFormGroupContent>;

@Service()
export class ContactMechTypeFormService {
  createContactMechTypeFormGroup(contactMechType?: ContactMechTypeFormGroupInput): ContactMechTypeFormGroup {
    const contactMechTypeRawValue = {
      ...this.getFormDefaults(),
      ...(contactMechType ?? { id: null }),
    };

    return new FormGroup<ContactMechTypeFormGroupContent>({
      id: new FormControl(
        { value: contactMechTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(contactMechTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(contactMechTypeRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
    });
  }

  getContactMechType(form: ContactMechTypeFormGroup): IContactMechType | NewContactMechType {
    return form.getRawValue();
  }

  resetForm(form: ContactMechTypeFormGroup, contactMechType: ContactMechTypeFormGroupInput): void {
    const contactMechTypeRawValue = { ...this.getFormDefaults(), ...contactMechType };
    form.reset({
      ...contactMechTypeRawValue,
      id: { value: contactMechTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ContactMechTypeFormDefaults {
    return {
      id: null,
    };
  }
}
