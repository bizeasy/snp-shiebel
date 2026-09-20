import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFacilityLocation, NewFacilityLocation } from '../facility-location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacilityLocation for edit and NewFacilityLocationFormGroupInput for create.
 */
type FacilityLocationFormGroupInput = IFacilityLocation | PartialWithRequiredKeyOf<NewFacilityLocation>;

type FacilityLocationFormDefaults = Pick<NewFacilityLocation, 'id'>;

type FacilityLocationFormGroupContent = {
  id: FormControl<IFacilityLocation['id'] | NewFacilityLocation['id']>;
  locationSeqId: FormControl<IFacilityLocation['locationSeqId']>;
  areaId: FormControl<IFacilityLocation['areaId']>;
  aisleId: FormControl<IFacilityLocation['aisleId']>;
  sectionId: FormControl<IFacilityLocation['sectionId']>;
  levelId: FormControl<IFacilityLocation['levelId']>;
  positionId: FormControl<IFacilityLocation['positionId']>;
  facility: FormControl<IFacilityLocation['facility']>;
  locationType: FormControl<IFacilityLocation['locationType']>;
};

export type FacilityLocationFormGroup = FormGroup<FacilityLocationFormGroupContent>;

@Service()
export class FacilityLocationFormService {
  createFacilityLocationFormGroup(facilityLocation?: FacilityLocationFormGroupInput): FacilityLocationFormGroup {
    const facilityLocationRawValue = {
      ...this.getFormDefaults(),
      ...(facilityLocation ?? { id: null }),
    };

    return new FormGroup<FacilityLocationFormGroupContent>({
      id: new FormControl(
        { value: facilityLocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      locationSeqId: new FormControl(facilityLocationRawValue.locationSeqId, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      areaId: new FormControl(facilityLocationRawValue.areaId, {
        validators: [Validators.maxLength(20)],
      }),
      aisleId: new FormControl(facilityLocationRawValue.aisleId, {
        validators: [Validators.maxLength(20)],
      }),
      sectionId: new FormControl(facilityLocationRawValue.sectionId, {
        validators: [Validators.maxLength(20)],
      }),
      levelId: new FormControl(facilityLocationRawValue.levelId, {
        validators: [Validators.maxLength(20)],
      }),
      positionId: new FormControl(facilityLocationRawValue.positionId, {
        validators: [Validators.maxLength(20)],
      }),
      facility: new FormControl(facilityLocationRawValue.facility, {
        validators: [Validators.required],
      }),
      locationType: new FormControl(facilityLocationRawValue.locationType),
    });
  }

  getFacilityLocation(form: FacilityLocationFormGroup): IFacilityLocation | NewFacilityLocation {
    return form.getRawValue();
  }

  resetForm(form: FacilityLocationFormGroup, facilityLocation: FacilityLocationFormGroupInput): void {
    const facilityLocationRawValue = { ...this.getFormDefaults(), ...facilityLocation };
    form.reset({
      ...facilityLocationRawValue,
      id: { value: facilityLocationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacilityLocationFormDefaults {
    return {
      id: null,
    };
  }
}
