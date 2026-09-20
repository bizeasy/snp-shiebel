import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISalesChannel, NewSalesChannel } from '../sales-channel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalesChannel for edit and NewSalesChannelFormGroupInput for create.
 */
type SalesChannelFormGroupInput = ISalesChannel | PartialWithRequiredKeyOf<NewSalesChannel>;

type SalesChannelFormDefaults = Pick<NewSalesChannel, 'id'>;

type SalesChannelFormGroupContent = {
  id: FormControl<ISalesChannel['id'] | NewSalesChannel['id']>;
  name: FormControl<ISalesChannel['name']>;
  description: FormControl<ISalesChannel['description']>;
  sequenceNo: FormControl<ISalesChannel['sequenceNo']>;
};

export type SalesChannelFormGroup = FormGroup<SalesChannelFormGroupContent>;

@Service()
export class SalesChannelFormService {
  createSalesChannelFormGroup(salesChannel?: SalesChannelFormGroupInput): SalesChannelFormGroup {
    const salesChannelRawValue = {
      ...this.getFormDefaults(),
      ...(salesChannel ?? { id: null }),
    };

    return new FormGroup<SalesChannelFormGroupContent>({
      id: new FormControl(
        { value: salesChannelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(salesChannelRawValue.name, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(salesChannelRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      sequenceNo: new FormControl(salesChannelRawValue.sequenceNo),
    });
  }

  getSalesChannel(form: SalesChannelFormGroup): ISalesChannel | NewSalesChannel {
    return form.getRawValue();
  }

  resetForm(form: SalesChannelFormGroup, salesChannel: SalesChannelFormGroupInput): void {
    const salesChannelRawValue = { ...this.getFormDefaults(), ...salesChannel };
    form.reset({
      ...salesChannelRawValue,
      id: { value: salesChannelRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SalesChannelFormDefaults {
    return {
      id: null,
    };
  }
}
