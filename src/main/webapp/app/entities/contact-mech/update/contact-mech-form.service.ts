import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContactMech, NewContactMech } from '../contact-mech.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContactMech for edit and NewContactMechFormGroupInput for create.
 */
type ContactMechFormGroupInput = IContactMech | PartialWithRequiredKeyOf<NewContactMech>;

type ContactMechFormDefaults = Pick<NewContactMech, 'id'>;

type ContactMechFormGroupContent = {
  id: FormControl<IContactMech['id'] | NewContactMech['id']>;
  infoString: FormControl<IContactMech['infoString']>;
  contactMechType: FormControl<IContactMech['contactMechType']>;
};

export type ContactMechFormGroup = FormGroup<ContactMechFormGroupContent>;

@Service()
export class ContactMechFormService {
  createContactMechFormGroup(contactMech?: ContactMechFormGroupInput): ContactMechFormGroup {
    const contactMechRawValue = {
      ...this.getFormDefaults(),
      ...(contactMech ?? { id: null }),
    };

    return new FormGroup<ContactMechFormGroupContent>({
      id: new FormControl(
        { value: contactMechRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      infoString: new FormControl(contactMechRawValue.infoString, {
        validators: [Validators.maxLength(255)],
      }),
      contactMechType: new FormControl(contactMechRawValue.contactMechType),
    });
  }

  getContactMech(form: ContactMechFormGroup): IContactMech | NewContactMech {
    return form.getRawValue();
  }

  resetForm(form: ContactMechFormGroup, contactMech: ContactMechFormGroupInput): void {
    const contactMechRawValue = { ...this.getFormDefaults(), ...contactMech };
    form.reset({
      ...contactMechRawValue,
      id: { value: contactMechRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ContactMechFormDefaults {
    return {
      id: null,
    };
  }
}
