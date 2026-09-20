import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ICustRequest, NewCustRequest } from '../cust-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustRequest for edit and NewCustRequestFormGroupInput for create.
 */
type CustRequestFormGroupInput = ICustRequest | PartialWithRequiredKeyOf<NewCustRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustRequest | NewCustRequest> = Omit<
  T,
  'custRequestDate' | 'responseRequiredDate' | 'openDateTime' | 'closedDateTime'
> & {
  custRequestDate?: string | null;
  responseRequiredDate?: string | null;
  openDateTime?: string | null;
  closedDateTime?: string | null;
};

type CustRequestFormRawValue = FormValueOf<ICustRequest>;

type NewCustRequestFormRawValue = FormValueOf<NewCustRequest>;

type CustRequestFormDefaults = Pick<
  NewCustRequest,
  'id' | 'custRequestDate' | 'responseRequiredDate' | 'openDateTime' | 'closedDateTime' | 'billed'
>;

type CustRequestFormGroupContent = {
  id: FormControl<CustRequestFormRawValue['id'] | NewCustRequest['id']>;
  name: FormControl<CustRequestFormRawValue['name']>;
  description: FormControl<CustRequestFormRawValue['description']>;
  priority: FormControl<CustRequestFormRawValue['priority']>;
  custRequestDate: FormControl<CustRequestFormRawValue['custRequestDate']>;
  responseRequiredDate: FormControl<CustRequestFormRawValue['responseRequiredDate']>;
  openDateTime: FormControl<CustRequestFormRawValue['openDateTime']>;
  closedDateTime: FormControl<CustRequestFormRawValue['closedDateTime']>;
  internalComment: FormControl<CustRequestFormRawValue['internalComment']>;
  billed: FormControl<CustRequestFormRawValue['billed']>;
  custEstimatedDurationDays: FormControl<CustRequestFormRawValue['custEstimatedDurationDays']>;
  custRequestType: FormControl<CustRequestFormRawValue['custRequestType']>;
  custRequestCategory: FormControl<CustRequestFormRawValue['custRequestCategory']>;
  status: FormControl<CustRequestFormRawValue['status']>;
  fromParty: FormControl<CustRequestFormRawValue['fromParty']>;
  productStore: FormControl<CustRequestFormRawValue['productStore']>;
  salesChannel: FormControl<CustRequestFormRawValue['salesChannel']>;
  currencyUom: FormControl<CustRequestFormRawValue['currencyUom']>;
};

export type CustRequestFormGroup = FormGroup<CustRequestFormGroupContent>;

@Service()
export class CustRequestFormService {
  createCustRequestFormGroup(custRequest?: CustRequestFormGroupInput): CustRequestFormGroup {
    const custRequestRawValue = this.convertCustRequestToCustRequestRawValue({
      ...this.getFormDefaults(),
      ...(custRequest ?? { id: null }),
    });

    return new FormGroup<CustRequestFormGroupContent>({
      id: new FormControl(
        { value: custRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(custRequestRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(custRequestRawValue.description, {
        validators: [Validators.maxLength(2000)],
      }),
      priority: new FormControl(custRequestRawValue.priority, {
        validators: [Validators.min(1), Validators.max(20)],
      }),
      custRequestDate: new FormControl(custRequestRawValue.custRequestDate, {
        validators: [Validators.required],
      }),
      responseRequiredDate: new FormControl(custRequestRawValue.responseRequiredDate),
      openDateTime: new FormControl(custRequestRawValue.openDateTime),
      closedDateTime: new FormControl(custRequestRawValue.closedDateTime),
      internalComment: new FormControl(custRequestRawValue.internalComment, {
        validators: [Validators.maxLength(2000)],
      }),
      billed: new FormControl(custRequestRawValue.billed),
      custEstimatedDurationDays: new FormControl(custRequestRawValue.custEstimatedDurationDays),
      custRequestType: new FormControl(custRequestRawValue.custRequestType),
      custRequestCategory: new FormControl(custRequestRawValue.custRequestCategory),
      status: new FormControl(custRequestRawValue.status),
      fromParty: new FormControl(custRequestRawValue.fromParty, {
        validators: [Validators.required],
      }),
      productStore: new FormControl(custRequestRawValue.productStore),
      salesChannel: new FormControl(custRequestRawValue.salesChannel),
      currencyUom: new FormControl(custRequestRawValue.currencyUom),
    });
  }

  getCustRequest(form: CustRequestFormGroup): ICustRequest | NewCustRequest {
    return this.convertCustRequestRawValueToCustRequest(form.getRawValue());
  }

  resetForm(form: CustRequestFormGroup, custRequest: CustRequestFormGroupInput): void {
    const custRequestRawValue = this.convertCustRequestToCustRequestRawValue({ ...this.getFormDefaults(), ...custRequest });
    form.reset({
      ...custRequestRawValue,
      id: { value: custRequestRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CustRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      custRequestDate: currentTime,
      responseRequiredDate: currentTime,
      openDateTime: currentTime,
      closedDateTime: currentTime,
      billed: false,
    };
  }

  private convertCustRequestRawValueToCustRequest(
    rawCustRequest: CustRequestFormRawValue | NewCustRequestFormRawValue,
  ): ICustRequest | NewCustRequest {
    return {
      ...rawCustRequest,
      custRequestDate: dayjs(rawCustRequest.custRequestDate, DATE_TIME_FORMAT),
      responseRequiredDate: dayjs(rawCustRequest.responseRequiredDate, DATE_TIME_FORMAT),
      openDateTime: dayjs(rawCustRequest.openDateTime, DATE_TIME_FORMAT),
      closedDateTime: dayjs(rawCustRequest.closedDateTime, DATE_TIME_FORMAT),
    };
  }

  private convertCustRequestToCustRequestRawValue(
    custRequest: ICustRequest | (Partial<NewCustRequest> & CustRequestFormDefaults),
  ): CustRequestFormRawValue | PartialWithRequiredKeyOf<NewCustRequestFormRawValue> {
    return {
      ...custRequest,
      custRequestDate: custRequest.custRequestDate ? custRequest.custRequestDate.format(DATE_TIME_FORMAT) : undefined,
      responseRequiredDate: custRequest.responseRequiredDate ? custRequest.responseRequiredDate.format(DATE_TIME_FORMAT) : undefined,
      openDateTime: custRequest.openDateTime ? custRequest.openDateTime.format(DATE_TIME_FORMAT) : undefined,
      closedDateTime: custRequest.closedDateTime ? custRequest.closedDateTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
