import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReturnHeader, NewReturnHeader } from '../return-header.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReturnHeader for edit and NewReturnHeaderFormGroupInput for create.
 */
type ReturnHeaderFormGroupInput = IReturnHeader | PartialWithRequiredKeyOf<NewReturnHeader>;

type ReturnHeaderFormDefaults = Pick<NewReturnHeader, 'id' | 'needsInventoryReceive'>;

type ReturnHeaderFormGroupContent = {
  id: FormControl<IReturnHeader['id'] | NewReturnHeader['id']>;
  needsInventoryReceive: FormControl<IReturnHeader['needsInventoryReceive']>;
  supplierRmaId: FormControl<IReturnHeader['supplierRmaId']>;
  returnType: FormControl<IReturnHeader['returnType']>;
  status: FormControl<IReturnHeader['status']>;
  fromParty: FormControl<IReturnHeader['fromParty']>;
  toParty: FormControl<IReturnHeader['toParty']>;
  paymentMethod: FormControl<IReturnHeader['paymentMethod']>;
  originContactMech: FormControl<IReturnHeader['originContactMech']>;
  destinationFacility: FormControl<IReturnHeader['destinationFacility']>;
  currencyUom: FormControl<IReturnHeader['currencyUom']>;
};

export type ReturnHeaderFormGroup = FormGroup<ReturnHeaderFormGroupContent>;

@Service()
export class ReturnHeaderFormService {
  createReturnHeaderFormGroup(returnHeader?: ReturnHeaderFormGroupInput): ReturnHeaderFormGroup {
    const returnHeaderRawValue = {
      ...this.getFormDefaults(),
      ...(returnHeader ?? { id: null }),
    };

    return new FormGroup<ReturnHeaderFormGroupContent>({
      id: new FormControl(
        { value: returnHeaderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      needsInventoryReceive: new FormControl(returnHeaderRawValue.needsInventoryReceive),
      supplierRmaId: new FormControl(returnHeaderRawValue.supplierRmaId, {
        validators: [Validators.maxLength(20)],
      }),
      returnType: new FormControl(returnHeaderRawValue.returnType),
      status: new FormControl(returnHeaderRawValue.status),
      fromParty: new FormControl(returnHeaderRawValue.fromParty),
      toParty: new FormControl(returnHeaderRawValue.toParty),
      paymentMethod: new FormControl(returnHeaderRawValue.paymentMethod),
      originContactMech: new FormControl(returnHeaderRawValue.originContactMech),
      destinationFacility: new FormControl(returnHeaderRawValue.destinationFacility),
      currencyUom: new FormControl(returnHeaderRawValue.currencyUom),
    });
  }

  getReturnHeader(form: ReturnHeaderFormGroup): IReturnHeader | NewReturnHeader {
    return form.getRawValue();
  }

  resetForm(form: ReturnHeaderFormGroup, returnHeader: ReturnHeaderFormGroupInput): void {
    const returnHeaderRawValue = { ...this.getFormDefaults(), ...returnHeader };
    form.reset({
      ...returnHeaderRawValue,
      id: { value: returnHeaderRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ReturnHeaderFormDefaults {
    return {
      id: null,
      needsInventoryReceive: false,
    };
  }
}
