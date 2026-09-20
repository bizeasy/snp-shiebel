import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFacility, NewFacility } from '../facility.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacility for edit and NewFacilityFormGroupInput for create.
 */
type FacilityFormGroupInput = IFacility | PartialWithRequiredKeyOf<NewFacility>;

type FacilityFormDefaults = Pick<NewFacility, 'id'>;

type FacilityFormGroupContent = {
  id: FormControl<IFacility['id'] | NewFacility['id']>;
  name: FormControl<IFacility['name']>;
  description: FormControl<IFacility['description']>;
  facilitySize: FormControl<IFacility['facilitySize']>;
  facilityType: FormControl<IFacility['facilityType']>;
  parentFacility: FormControl<IFacility['parentFacility']>;
  ownerParty: FormControl<IFacility['ownerParty']>;
  facilityGroup: FormControl<IFacility['facilityGroup']>;
  sizeUom: FormControl<IFacility['sizeUom']>;
  geoPoint: FormControl<IFacility['geoPoint']>;
  inventoryItemType: FormControl<IFacility['inventoryItemType']>;
  productStore: FormControl<IFacility['productStore']>;
};

export type FacilityFormGroup = FormGroup<FacilityFormGroupContent>;

@Service()
export class FacilityFormService {
  createFacilityFormGroup(facility?: FacilityFormGroupInput): FacilityFormGroup {
    const facilityRawValue = {
      ...this.getFormDefaults(),
      ...(facility ?? { id: null }),
    };

    return new FormGroup<FacilityFormGroupContent>({
      id: new FormControl(
        { value: facilityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(facilityRawValue.name, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      description: new FormControl(facilityRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      facilitySize: new FormControl(facilityRawValue.facilitySize),
      facilityType: new FormControl(facilityRawValue.facilityType),
      parentFacility: new FormControl(facilityRawValue.parentFacility),
      ownerParty: new FormControl(facilityRawValue.ownerParty),
      facilityGroup: new FormControl(facilityRawValue.facilityGroup),
      sizeUom: new FormControl(facilityRawValue.sizeUom),
      geoPoint: new FormControl(facilityRawValue.geoPoint),
      inventoryItemType: new FormControl(facilityRawValue.inventoryItemType),
      productStore: new FormControl(facilityRawValue.productStore),
    });
  }

  getFacility(form: FacilityFormGroup): IFacility | NewFacility {
    return form.getRawValue();
  }

  resetForm(form: FacilityFormGroup, facility: FacilityFormGroupInput): void {
    const facilityRawValue = { ...this.getFormDefaults(), ...facility };
    form.reset({
      ...facilityRawValue,
      id: { value: facilityRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityFormDefaults {
    return {
      id: null,
    };
  }
}
