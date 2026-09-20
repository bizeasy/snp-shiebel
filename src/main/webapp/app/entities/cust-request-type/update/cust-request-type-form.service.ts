import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustRequestType, NewCustRequestType } from '../cust-request-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustRequestType for edit and NewCustRequestTypeFormGroupInput for create.
 */
type CustRequestTypeFormGroupInput = ICustRequestType | PartialWithRequiredKeyOf<NewCustRequestType>;

type CustRequestTypeFormDefaults = Pick<NewCustRequestType, 'id'>;

type CustRequestTypeFormGroupContent = {
  id: FormControl<ICustRequestType['id'] | NewCustRequestType['id']>;
  name: FormControl<ICustRequestType['name']>;
  description: FormControl<ICustRequestType['description']>;
  parent: FormControl<ICustRequestType['parent']>;
  party: FormControl<ICustRequestType['party']>;
};

export type CustRequestTypeFormGroup = FormGroup<CustRequestTypeFormGroupContent>;

@Service()
export class CustRequestTypeFormService {
  createCustRequestTypeFormGroup(custRequestType?: CustRequestTypeFormGroupInput): CustRequestTypeFormGroup {
    const custRequestTypeRawValue = {
      ...this.getFormDefaults(),
      ...(custRequestType ?? { id: null }),
    };

    return new FormGroup<CustRequestTypeFormGroupContent>({
      id: new FormControl(
        { value: custRequestTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(custRequestTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(custRequestTypeRawValue.description, {
        validators: [Validators.maxLength(255)],
      }),
      parent: new FormControl(custRequestTypeRawValue.parent),
      party: new FormControl(custRequestTypeRawValue.party),
    });
  }

  getCustRequestType(form: CustRequestTypeFormGroup): ICustRequestType | NewCustRequestType {
    return form.getRawValue();
  }

  resetForm(form: CustRequestTypeFormGroup, custRequestType: CustRequestTypeFormGroupInput): void {
    const custRequestTypeRawValue = { ...this.getFormDefaults(), ...custRequestType };
    form.reset({
      ...custRequestTypeRawValue,
      id: { value: custRequestTypeRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustRequestTypeFormDefaults {
    return {
      id: null,
    };
  }
}
