import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduct | NewProduct> = Omit<T, 'introductionDate' | 'releaseDate'> & {
  introductionDate?: string | null;
  releaseDate?: string | null;
};

type ProductFormRawValue = FormValueOf<IProduct>;

type NewProductFormRawValue = FormValueOf<NewProduct>;

type ProductFormDefaults = Pick<
  NewProduct,
  'id' | 'introductionDate' | 'releaseDate' | 'isVirtual' | 'isVariant' | 'requireInventory' | 'returnable' | 'isPopular' | 'isBanner'
>;

type ProductFormGroupContent = {
  id: FormControl<ProductFormRawValue['id'] | NewProduct['id']>;
  name: FormControl<ProductFormRawValue['name']>;
  internalName: FormControl<ProductFormRawValue['internalName']>;
  brandName: FormControl<ProductFormRawValue['brandName']>;
  variant: FormControl<ProductFormRawValue['variant']>;
  sku: FormControl<ProductFormRawValue['sku']>;
  introductionDate: FormControl<ProductFormRawValue['introductionDate']>;
  releaseDate: FormControl<ProductFormRawValue['releaseDate']>;
  quantityIncluded: FormControl<ProductFormRawValue['quantityIncluded']>;
  piecesIncluded: FormControl<ProductFormRawValue['piecesIncluded']>;
  weight: FormControl<ProductFormRawValue['weight']>;
  height: FormControl<ProductFormRawValue['height']>;
  width: FormControl<ProductFormRawValue['width']>;
  depth: FormControl<ProductFormRawValue['depth']>;
  smallImageUrl: FormControl<ProductFormRawValue['smallImageUrl']>;
  mediumImageUrl: FormControl<ProductFormRawValue['mediumImageUrl']>;
  largeImageUrl: FormControl<ProductFormRawValue['largeImageUrl']>;
  detailImageUrl: FormControl<ProductFormRawValue['detailImageUrl']>;
  originalImageUrl: FormControl<ProductFormRawValue['originalImageUrl']>;
  altImageUrl1: FormControl<ProductFormRawValue['altImageUrl1']>;
  altImageUrl2: FormControl<ProductFormRawValue['altImageUrl2']>;
  altImageUrl3: FormControl<ProductFormRawValue['altImageUrl3']>;
  altImageUrl4: FormControl<ProductFormRawValue['altImageUrl4']>;
  altImageUrl5: FormControl<ProductFormRawValue['altImageUrl5']>;
  altImageUrl6: FormControl<ProductFormRawValue['altImageUrl6']>;
  videoUrl: FormControl<ProductFormRawValue['videoUrl']>;
  quantity: FormControl<ProductFormRawValue['quantity']>;
  price: FormControl<ProductFormRawValue['price']>;
  cgst: FormControl<ProductFormRawValue['cgst']>;
  igst: FormControl<ProductFormRawValue['igst']>;
  sgst: FormControl<ProductFormRawValue['sgst']>;
  cgstPercentage: FormControl<ProductFormRawValue['cgstPercentage']>;
  igstPercentage: FormControl<ProductFormRawValue['igstPercentage']>;
  sgstPercentage: FormControl<ProductFormRawValue['sgstPercentage']>;
  totalPrice: FormControl<ProductFormRawValue['totalPrice']>;
  description: FormControl<ProductFormRawValue['description']>;
  longDescription: FormControl<ProductFormRawValue['longDescription']>;
  info: FormControl<ProductFormRawValue['info']>;
  isVirtual: FormControl<ProductFormRawValue['isVirtual']>;
  isVariant: FormControl<ProductFormRawValue['isVariant']>;
  requireInventory: FormControl<ProductFormRawValue['requireInventory']>;
  returnable: FormControl<ProductFormRawValue['returnable']>;
  isPopular: FormControl<ProductFormRawValue['isPopular']>;
  isBanner: FormControl<ProductFormRawValue['isBanner']>;
  bannerUrl: FormControl<ProductFormRawValue['bannerUrl']>;
  productType: FormControl<ProductFormRawValue['productType']>;
  primaryProductCategory: FormControl<ProductFormRawValue['primaryProductCategory']>;
  virtualProduct: FormControl<ProductFormRawValue['virtualProduct']>;
  inventoryItemType: FormControl<ProductFormRawValue['inventoryItemType']>;
  taxSlab: FormControl<ProductFormRawValue['taxSlab']>;
  uom: FormControl<ProductFormRawValue['uom']>;
  quantityUom: FormControl<ProductFormRawValue['quantityUom']>;
  weightUom: FormControl<ProductFormRawValue['weightUom']>;
  sizeUom: FormControl<ProductFormRawValue['sizeUom']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Service()
export class ProductFormService {
  createProductFormGroup(product?: ProductFormGroupInput): ProductFormGroup {
    const productRawValue = this.convertProductToProductRawValue({
      ...this.getFormDefaults(),
      ...(product ?? { id: null }),
    });

    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      internalName: new FormControl(productRawValue.internalName, {
        validators: [Validators.maxLength(60)],
      }),
      brandName: new FormControl(productRawValue.brandName, {
        validators: [Validators.maxLength(60)],
      }),
      variant: new FormControl(productRawValue.variant, {
        validators: [Validators.maxLength(60)],
      }),
      sku: new FormControl(productRawValue.sku, {
        validators: [Validators.maxLength(60)],
      }),
      introductionDate: new FormControl(productRawValue.introductionDate),
      releaseDate: new FormControl(productRawValue.releaseDate),
      quantityIncluded: new FormControl(productRawValue.quantityIncluded),
      piecesIncluded: new FormControl(productRawValue.piecesIncluded),
      weight: new FormControl(productRawValue.weight),
      height: new FormControl(productRawValue.height),
      width: new FormControl(productRawValue.width),
      depth: new FormControl(productRawValue.depth),
      smallImageUrl: new FormControl(productRawValue.smallImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      mediumImageUrl: new FormControl(productRawValue.mediumImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      largeImageUrl: new FormControl(productRawValue.largeImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      detailImageUrl: new FormControl(productRawValue.detailImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      originalImageUrl: new FormControl(productRawValue.originalImageUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      altImageUrl1: new FormControl(productRawValue.altImageUrl1, {
        validators: [Validators.maxLength(2000)],
      }),
      altImageUrl2: new FormControl(productRawValue.altImageUrl2, {
        validators: [Validators.maxLength(2000)],
      }),
      altImageUrl3: new FormControl(productRawValue.altImageUrl3, {
        validators: [Validators.maxLength(2000)],
      }),
      altImageUrl4: new FormControl(productRawValue.altImageUrl4, {
        validators: [Validators.maxLength(2000)],
      }),
      altImageUrl5: new FormControl(productRawValue.altImageUrl5, {
        validators: [Validators.maxLength(2000)],
      }),
      altImageUrl6: new FormControl(productRawValue.altImageUrl6, {
        validators: [Validators.maxLength(2000)],
      }),
      videoUrl: new FormControl(productRawValue.videoUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      quantity: new FormControl(productRawValue.quantity),
      price: new FormControl(productRawValue.price),
      cgst: new FormControl(productRawValue.cgst),
      igst: new FormControl(productRawValue.igst),
      sgst: new FormControl(productRawValue.sgst),
      cgstPercentage: new FormControl(productRawValue.cgstPercentage),
      igstPercentage: new FormControl(productRawValue.igstPercentage),
      sgstPercentage: new FormControl(productRawValue.sgstPercentage),
      totalPrice: new FormControl(productRawValue.totalPrice),
      description: new FormControl(productRawValue.description, {
        validators: [Validators.maxLength(100)],
      }),
      longDescription: new FormControl(productRawValue.longDescription, {
        validators: [Validators.maxLength(2000)],
      }),
      info: new FormControl(productRawValue.info),
      isVirtual: new FormControl(productRawValue.isVirtual),
      isVariant: new FormControl(productRawValue.isVariant),
      requireInventory: new FormControl(productRawValue.requireInventory),
      returnable: new FormControl(productRawValue.returnable),
      isPopular: new FormControl(productRawValue.isPopular),
      isBanner: new FormControl(productRawValue.isBanner),
      bannerUrl: new FormControl(productRawValue.bannerUrl, {
        validators: [Validators.maxLength(2000)],
      }),
      productType: new FormControl(productRawValue.productType),
      primaryProductCategory: new FormControl(productRawValue.primaryProductCategory),
      virtualProduct: new FormControl(productRawValue.virtualProduct),
      inventoryItemType: new FormControl(productRawValue.inventoryItemType),
      taxSlab: new FormControl(productRawValue.taxSlab),
      uom: new FormControl(productRawValue.uom),
      quantityUom: new FormControl(productRawValue.quantityUom),
      weightUom: new FormControl(productRawValue.weightUom),
      sizeUom: new FormControl(productRawValue.sizeUom),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return this.convertProductRawValueToProduct(form.getRawValue());
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = this.convertProductToProductRawValue({ ...this.getFormDefaults(), ...product });
    form.reset({
      ...productRawValue,
      id: { value: productRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ProductFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      introductionDate: currentTime,
      releaseDate: currentTime,
      isVirtual: false,
      isVariant: false,
      requireInventory: false,
      returnable: false,
      isPopular: false,
      isBanner: false,
    };
  }

  private convertProductRawValueToProduct(rawProduct: ProductFormRawValue | NewProductFormRawValue): IProduct | NewProduct {
    return {
      ...rawProduct,
      introductionDate: dayjs(rawProduct.introductionDate, DATE_TIME_FORMAT),
      releaseDate: dayjs(rawProduct.releaseDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductToProductRawValue(
    product: IProduct | (Partial<NewProduct> & ProductFormDefaults),
  ): ProductFormRawValue | PartialWithRequiredKeyOf<NewProductFormRawValue> {
    return {
      ...product,
      introductionDate: product.introductionDate ? product.introductionDate.format(DATE_TIME_FORMAT) : undefined,
      releaseDate: product.releaseDate ? product.releaseDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
