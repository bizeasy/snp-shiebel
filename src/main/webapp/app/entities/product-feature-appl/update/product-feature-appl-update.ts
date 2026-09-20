import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IProductFeatureApplType } from 'app/entities/product-feature-appl-type/product-feature-appl-type.model';
import { ProductFeatureApplTypeService } from 'app/entities/product-feature-appl-type/service/product-feature-appl-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureAppl } from '../product-feature-appl.model';
import { ProductFeatureApplService } from '../service/product-feature-appl.service';

import { ProductFeatureApplFormGroup, ProductFeatureApplFormService } from './product-feature-appl-form.service';

@Component({
  selector: 'sys-product-feature-appl-update',
  templateUrl: './product-feature-appl-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureApplUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeatureAppl: IProductFeatureAppl | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  productFeaturesSharedCollection = signal<IProductFeature[]>([]);
  productFeatureApplTypesSharedCollection = signal<IProductFeatureApplType[]>([]);

  protected productFeatureApplService = inject(ProductFeatureApplService);
  protected productFeatureApplFormService = inject(ProductFeatureApplFormService);
  protected productService = inject(ProductService);
  protected productFeatureService = inject(ProductFeatureService);
  protected productFeatureApplTypeService = inject(ProductFeatureApplTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureApplFormGroup = this.productFeatureApplFormService.createProductFeatureApplFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductFeature = (o1: IProductFeature | null, o2: IProductFeature | null): boolean =>
    this.productFeatureService.compareProductFeature(o1, o2);

  compareProductFeatureApplType = (o1: IProductFeatureApplType | null, o2: IProductFeatureApplType | null): boolean =>
    this.productFeatureApplTypeService.compareProductFeatureApplType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeatureAppl }) => {
      this.productFeatureAppl = productFeatureAppl;
      if (productFeatureAppl) {
        this.updateForm(productFeatureAppl);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeatureAppl = this.productFeatureApplFormService.getProductFeatureAppl(this.editForm);
    if (productFeatureAppl.id === null) {
      this.subscribeToSaveResponse(this.productFeatureApplService.create(productFeatureAppl));
    } else {
      this.subscribeToSaveResponse(this.productFeatureApplService.update(productFeatureAppl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeatureAppl | null>): void {
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

  protected updateForm(productFeatureAppl: IProductFeatureAppl): void {
    this.productFeatureAppl = productFeatureAppl;
    this.productFeatureApplFormService.resetForm(this.editForm, productFeatureAppl);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productFeatureAppl.product),
    );
    this.productFeaturesSharedCollection.update(productFeatures =>
      this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
        productFeatures,
        productFeatureAppl.productFeature,
      ),
    );
    this.productFeatureApplTypesSharedCollection.update(productFeatureApplTypes =>
      this.productFeatureApplTypeService.addProductFeatureApplTypeToCollectionIfMissing<IProductFeatureApplType>(
        productFeatureApplTypes,
        productFeatureAppl.featureApplType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productFeatureAppl?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.productFeatureService
      .query()
      .pipe(map((res: HttpResponse<IProductFeature[]>) => res.body ?? []))
      .pipe(
        map((productFeatures: IProductFeature[]) =>
          this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
            productFeatures,
            this.productFeatureAppl?.productFeature,
          ),
        ),
      )
      .subscribe((productFeatures: IProductFeature[]) => this.productFeaturesSharedCollection.set(productFeatures));

    this.productFeatureApplTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductFeatureApplType[]>) => res.body ?? []))
      .pipe(
        map((productFeatureApplTypes: IProductFeatureApplType[]) =>
          this.productFeatureApplTypeService.addProductFeatureApplTypeToCollectionIfMissing<IProductFeatureApplType>(
            productFeatureApplTypes,
            this.productFeatureAppl?.featureApplType,
          ),
        ),
      )
      .subscribe((productFeatureApplTypes: IProductFeatureApplType[]) =>
        this.productFeatureApplTypesSharedCollection.set(productFeatureApplTypes),
      );
  }
}
