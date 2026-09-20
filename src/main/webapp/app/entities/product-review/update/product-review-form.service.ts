import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProductReview, NewProductReview } from '../product-review.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductReview for edit and NewProductReviewFormGroupInput for create.
 */
type ProductReviewFormGroupInput = IProductReview | PartialWithRequiredKeyOf<NewProductReview>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductReview | NewProductReview> = Omit<T, 'reviewDate'> & {
  reviewDate?: string | null;
};

type ProductReviewFormRawValue = FormValueOf<IProductReview>;

type NewProductReviewFormRawValue = FormValueOf<NewProductReview>;

type ProductReviewFormDefaults = Pick<NewProductReview, 'id' | 'reviewDate'>;

type ProductReviewFormGroupContent = {
  id: FormControl<ProductReviewFormRawValue['id'] | NewProductReview['id']>;
  rating: FormControl<ProductReviewFormRawValue['rating']>;
  ratingInfo: FormControl<ProductReviewFormRawValue['ratingInfo']>;
  message: FormControl<ProductReviewFormRawValue['message']>;
  reviewDate: FormControl<ProductReviewFormRawValue['reviewDate']>;
  product: FormControl<ProductReviewFormRawValue['product']>;
  party: FormControl<ProductReviewFormRawValue['party']>;
  orderItem: FormControl<ProductReviewFormRawValue['orderItem']>;
};

export type ProductReviewFormGroup = FormGroup<ProductReviewFormGroupContent>;

@Service()
export class ProductReviewFormService {
  createProductReviewFormGroup(productReview?: ProductReviewFormGroupInput): ProductReviewFormGroup {
    const productReviewRawValue = this.convertProductReviewToProductReviewRawValue({
      ...this.getFormDefaults(),
      ...(productReview ?? { id: null }),
    });

    return new FormGroup<ProductReviewFormGroupContent>({
      id: new FormControl(
        { value: productReviewRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      rating: new FormControl(productReviewRawValue.rating, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      ratingInfo: new FormControl(productReviewRawValue.ratingInfo),
      message: new FormControl(productReviewRawValue.message, {
        validators: [Validators.maxLength(2000)],
      }),
      reviewDate: new FormControl(productReviewRawValue.reviewDate),
      product: new FormControl(productReviewRawValue.product, {
        validators: [Validators.required],
      }),
      party: new FormControl(productReviewRawValue.party),
      orderItem: new FormControl(productReviewRawValue.orderItem),
    });
  }

  getProductReview(form: ProductReviewFormGroup): IProductReview | NewProductReview {
    return this.convertProductReviewRawValueToProductReview(form.getRawValue());
  }

  resetForm(form: ProductReviewFormGroup, productReview: ProductReviewFormGroupInput): void {
    const productReviewRawValue = this.convertProductReviewToProductReviewRawValue({ ...this.getFormDefaults(), ...productReview });
    form.reset({
      ...productReviewRawValue,
      id: { value: productReviewRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductReviewFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      reviewDate: currentTime,
    };
  }

  private convertProductReviewRawValueToProductReview(
    rawProductReview: ProductReviewFormRawValue | NewProductReviewFormRawValue,
  ): IProductReview | NewProductReview {
    return {
      ...rawProductReview,
      reviewDate: dayjs(rawProductReview.reviewDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductReviewToProductReviewRawValue(
    productReview: IProductReview | (Partial<NewProductReview> & ProductReviewFormDefaults),
  ): ProductReviewFormRawValue | PartialWithRequiredKeyOf<NewProductReviewFormRawValue> {
    return {
      ...productReview,
      reviewDate: productReview.reviewDate ? productReview.reviewDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
