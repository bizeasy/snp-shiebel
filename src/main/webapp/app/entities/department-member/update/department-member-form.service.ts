import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IDepartmentMember, NewDepartmentMember } from '../department-member.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepartmentMember for edit and NewDepartmentMemberFormGroupInput for create.
 */
type DepartmentMemberFormGroupInput = IDepartmentMember | PartialWithRequiredKeyOf<NewDepartmentMember>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDepartmentMember | NewDepartmentMember> = Omit<T, 'fromDate' | 'thruDate'> & {
  fromDate?: string | null;
  thruDate?: string | null;
};

type DepartmentMemberFormRawValue = FormValueOf<IDepartmentMember>;

type NewDepartmentMemberFormRawValue = FormValueOf<NewDepartmentMember>;

type DepartmentMemberFormDefaults = Pick<NewDepartmentMember, 'id' | 'fromDate' | 'thruDate' | 'isPrimary'>;

type DepartmentMemberFormGroupContent = {
  id: FormControl<DepartmentMemberFormRawValue['id'] | NewDepartmentMember['id']>;
  fromDate: FormControl<DepartmentMemberFormRawValue['fromDate']>;
  thruDate: FormControl<DepartmentMemberFormRawValue['thruDate']>;
  isPrimary: FormControl<DepartmentMemberFormRawValue['isPrimary']>;
  department: FormControl<DepartmentMemberFormRawValue['department']>;
  party: FormControl<DepartmentMemberFormRawValue['party']>;
  roleType: FormControl<DepartmentMemberFormRawValue['roleType']>;
};

export type DepartmentMemberFormGroup = FormGroup<DepartmentMemberFormGroupContent>;

@Service()
export class DepartmentMemberFormService {
  createDepartmentMemberFormGroup(departmentMember?: DepartmentMemberFormGroupInput): DepartmentMemberFormGroup {
    const departmentMemberRawValue = this.convertDepartmentMemberToDepartmentMemberRawValue({
      ...this.getFormDefaults(),
      ...(departmentMember ?? { id: null }),
    });

    return new FormGroup<DepartmentMemberFormGroupContent>({
      id: new FormControl(
        { value: departmentMemberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fromDate: new FormControl(departmentMemberRawValue.fromDate, {
        validators: [Validators.required],
      }),
      thruDate: new FormControl(departmentMemberRawValue.thruDate),
      isPrimary: new FormControl(departmentMemberRawValue.isPrimary),
      department: new FormControl(departmentMemberRawValue.department, {
        validators: [Validators.required],
      }),
      party: new FormControl(departmentMemberRawValue.party, {
        validators: [Validators.required],
      }),
      roleType: new FormControl(departmentMemberRawValue.roleType),
    });
  }

  getDepartmentMember(form: DepartmentMemberFormGroup): IDepartmentMember | NewDepartmentMember {
    return this.convertDepartmentMemberRawValueToDepartmentMember(form.getRawValue());
  }

  resetForm(form: DepartmentMemberFormGroup, departmentMember: DepartmentMemberFormGroupInput): void {
    const departmentMemberRawValue = this.convertDepartmentMemberToDepartmentMemberRawValue({
      ...this.getFormDefaults(),
      ...departmentMember,
    });
    form.reset({
      ...departmentMemberRawValue,
      id: { value: departmentMemberRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DepartmentMemberFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fromDate: currentTime,
      thruDate: currentTime,
      isPrimary: false,
    };
  }

  private convertDepartmentMemberRawValueToDepartmentMember(
    rawDepartmentMember: DepartmentMemberFormRawValue | NewDepartmentMemberFormRawValue,
  ): IDepartmentMember | NewDepartmentMember {
    return {
      ...rawDepartmentMember,
      fromDate: dayjs(rawDepartmentMember.fromDate, DATE_TIME_FORMAT),
      thruDate: dayjs(rawDepartmentMember.thruDate, DATE_TIME_FORMAT),
    };
  }

  private convertDepartmentMemberToDepartmentMemberRawValue(
    departmentMember: IDepartmentMember | (Partial<NewDepartmentMember> & DepartmentMemberFormDefaults),
  ): DepartmentMemberFormRawValue | PartialWithRequiredKeyOf<NewDepartmentMemberFormRawValue> {
    return {
      ...departmentMember,
      fromDate: departmentMember.fromDate ? departmentMember.fromDate.format(DATE_TIME_FORMAT) : undefined,
      thruDate: departmentMember.thruDate ? departmentMember.thruDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
