import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductPricePurpose } from 'app/entities/product-price-purpose/product-price-purpose.model';
import { ProductPricePurposeService } from 'app/entities/product-price-purpose/service/product-price-purpose.service';
import { IProductPriceType } from 'app/entities/product-price-type/product-price-type.model';
import { ProductPriceTypeService } from 'app/entities/product-price-type/service/product-price-type.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductPrice } from '../product-price.model';
import { ProductPriceService } from '../service/product-price.service';

import { ProductPriceFormGroup, ProductPriceFormService } from './product-price-form.service';

@Component({
  selector: 'sys-product-price-update',
  templateUrl: './product-price-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductPriceUpdate implements OnInit {
  readonly isSaving = signal(false);
  productPrice: IProductPrice | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  productPriceTypesSharedCollection = signal<IProductPriceType[]>([]);
  productPricePurposesSharedCollection = signal<IProductPricePurpose[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);

  protected productPriceService = inject(ProductPriceService);
  protected productPriceFormService = inject(ProductPriceFormService);
  protected productService = inject(ProductService);
  protected productPriceTypeService = inject(ProductPriceTypeService);
  protected productPricePurposeService = inject(ProductPricePurposeService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductPriceFormGroup = this.productPriceFormService.createProductPriceFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductPriceType = (o1: IProductPriceType | null, o2: IProductPriceType | null): boolean =>
    this.productPriceTypeService.compareProductPriceType(o1, o2);

  compareProductPricePurpose = (o1: IProductPricePurpose | null, o2: IProductPricePurpose | null): boolean =>
    this.productPricePurposeService.compareProductPricePurpose(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productPrice }) => {
      this.productPrice = productPrice;
      if (productPrice) {
        this.updateForm(productPrice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productPrice = this.productPriceFormService.getProductPrice(this.editForm);
    if (productPrice.id === null) {
      this.subscribeToSaveResponse(this.productPriceService.create(productPrice));
    } else {
      this.subscribeToSaveResponse(this.productPriceService.update(productPrice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductPrice | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(productPrice: IProductPrice): void {
    this.productPrice = productPrice;
    this.productPriceFormService.resetForm(this.editForm, productPrice);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productPrice.product),
    );
    this.productPriceTypesSharedCollection.update(productPriceTypes =>
      this.productPriceTypeService.addProductPriceTypeToCollectionIfMissing<IProductPriceType>(
        productPriceTypes,
        productPrice.productPriceType,
      ),
    );
    this.productPricePurposesSharedCollection.update(productPricePurposes =>
      this.productPricePurposeService.addProductPricePurposeToCollectionIfMissing<IProductPricePurpose>(
        productPricePurposes,
        productPrice.productPricePurpose,
      ),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, productPrice.currencyUom));
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productPrice?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.productPriceTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductPriceType[]>) => res.body ?? []))
      .pipe(
        map((productPriceTypes: IProductPriceType[]) =>
          this.productPriceTypeService.addProductPriceTypeToCollectionIfMissing<IProductPriceType>(
            productPriceTypes,
            this.productPrice?.productPriceType,
          ),
        ),
      )
      .subscribe((productPriceTypes: IProductPriceType[]) => this.productPriceTypesSharedCollection.set(productPriceTypes));

    this.productPricePurposeService
      .query()
      .pipe(map((res: HttpResponse<IProductPricePurpose[]>) => res.body ?? []))
      .pipe(
        map((productPricePurposes: IProductPricePurpose[]) =>
          this.productPricePurposeService.addProductPricePurposeToCollectionIfMissing<IProductPricePurpose>(
            productPricePurposes,
            this.productPrice?.productPricePurpose,
          ),
        ),
      )
      .subscribe((productPricePurposes: IProductPricePurpose[]) => this.productPricePurposesSharedCollection.set(productPricePurposes));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.productPrice?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
