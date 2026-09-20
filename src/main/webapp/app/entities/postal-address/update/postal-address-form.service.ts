import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPostalAddress, NewPostalAddress } from '../postal-address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPostalAddress for edit and NewPostalAddressFormGroupInput for create.
 */
type PostalAddressFormGroupInput = IPostalAddress | PartialWithRequiredKeyOf<NewPostalAddress>;

type PostalAddressFormDefaults = Pick<NewPostalAddress, 'id' | 'isDefault' | 'isIndianAddress'>;

type PostalAddressFormGroupContent = {
  id: FormControl<IPostalAddress['id'] | NewPostalAddress['id']>;
  toName: FormControl<IPostalAddress['toName']>;
  address1: FormControl<IPostalAddress['address1']>;
  address2: FormControl<IPostalAddress['address2']>;
  city: FormControl<IPostalAddress['city']>;
  landmark: FormControl<IPostalAddress['landmark']>;
  postalCode: FormControl<IPostalAddress['postalCode']>;
  isDefault: FormControl<IPostalAddress['isDefault']>;
  customAddressType: FormControl<IPostalAddress['customAddressType']>;
  stateStr: FormControl<IPostalAddress['stateStr']>;
  countryStr: FormControl<IPostalAddress['countryStr']>;
  isIndianAddress: FormControl<IPostalAddress['isIndianAddress']>;
  note: FormControl<IPostalAddress['note']>;
  directions: FormControl<IPostalAddress['directions']>;
  contactMech: FormControl<IPostalAddress['contactMech']>;
  state: FormControl<IPostalAddress['state']>;
  pincode: FormControl<IPostalAddress['pincode']>;
  country: FormControl<IPostalAddress['country']>;
  geoPoint: FormControl<IPostalAddress['geoPoint']>;
};

export type PostalAddressFormGroup = FormGroup<PostalAddressFormGroupContent>;

@Service()
export class PostalAddressFormService {
  createPostalAddressFormGroup(postalAddress?: PostalAddressFormGroupInput): PostalAddressFormGroup {
    const postalAddressRawValue = {
      ...this.getFormDefaults(),
      ...(postalAddress ?? { id: null }),
    };

    return new FormGroup<PostalAddressFormGroupContent>({
      id: new FormControl(
        { value: postalAddressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      toName: new FormControl(postalAddressRawValue.toName, {
        validators: [Validators.maxLength(100)],
      }),
      address1: new FormControl(postalAddressRawValue.address1, {
        validators: [Validators.maxLength(200)],
      }),
      address2: new FormControl(postalAddressRawValue.address2, {
        validators: [Validators.maxLength(200)],
      }),
      city: new FormControl(postalAddressRawValue.city, {
        validators: [Validators.maxLength(60)],
      }),
      landmark: new FormControl(postalAddressRawValue.landmark, {
        validators: [Validators.maxLength(60)],
      }),
      postalCode: new FormControl(postalAddressRawValue.postalCode, {
        validators: [Validators.maxLength(10)],
      }),
      isDefault: new FormControl(postalAddressRawValue.isDefault),
      customAddressType: new FormControl(postalAddressRawValue.customAddressType, {
        validators: [Validators.maxLength(25)],
      }),
      stateStr: new FormControl(postalAddressRawValue.stateStr, {
        validators: [Validators.maxLength(40)],
      }),
      countryStr: new FormControl(postalAddressRawValue.countryStr, {
        validators: [Validators.maxLength(40)],
      }),
      isIndianAddress: new FormControl(postalAddressRawValue.isIndianAddress),
      note: new FormControl(postalAddressRawValue.note, {
        validators: [Validators.maxLength(255)],
      }),
      directions: new FormControl(postalAddressRawValue.directions, {
        validators: [Validators.maxLength(255)],
      }),
      contactMech: new FormControl(postalAddressRawValue.contactMech),
      state: new FormControl(postalAddressRawValue.state),
      pincode: new FormControl(postalAddressRawValue.pincode),
      country: new FormControl(postalAddressRawValue.country),
      geoPoint: new FormControl(postalAddressRawValue.geoPoint),
    });
  }

  getPostalAddress(form: PostalAddressFormGroup): IPostalAddress | NewPostalAddress {
    return form.getRawValue();
  }

  resetForm(form: PostalAddressFormGroup, postalAddress: PostalAddressFormGroupInput): void {
    const postalAddressRawValue = { ...this.getFormDefaults(), ...postalAddress };
    form.reset({
      ...postalAddressRawValue,
      id: { value: postalAddressRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PostalAddressFormDefaults {
    return {
      id: null,
      isDefault: false,
      isIndianAddress: false,
    };
  }
}
