import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProductFeatureCategory } from 'app/entities/product-feature-category/product-feature-category.model';
import { ProductFeatureCategoryService } from 'app/entities/product-feature-category/service/product-feature-category.service';
import { IProductFeatureType } from 'app/entities/product-feature-type/product-feature-type.model';
import { ProductFeatureTypeService } from 'app/entities/product-feature-type/service/product-feature-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeature } from '../product-feature.model';
import { ProductFeatureService } from '../service/product-feature.service';

import { ProductFeatureFormGroup, ProductFeatureFormService } from './product-feature-form.service';

@Component({
  selector: 'sys-product-feature-update',
  templateUrl: './product-feature-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeature: IProductFeature | null = null;

  productFeatureTypesSharedCollection = signal<IProductFeatureType[]>([]);
  productFeatureCategoriesSharedCollection = signal<IProductFeatureCategory[]>([]);

  protected productFeatureService = inject(ProductFeatureService);
  protected productFeatureFormService = inject(ProductFeatureFormService);
  protected productFeatureTypeService = inject(ProductFeatureTypeService);
  protected productFeatureCategoryService = inject(ProductFeatureCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureFormGroup = this.productFeatureFormService.createProductFeatureFormGroup();

  compareProductFeatureType = (o1: IProductFeatureType | null, o2: IProductFeatureType | null): boolean =>
    this.productFeatureTypeService.compareProductFeatureType(o1, o2);

  compareProductFeatureCategory = (o1: IProductFeatureCategory | null, o2: IProductFeatureCategory | null): boolean =>
    this.productFeatureCategoryService.compareProductFeatureCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeature }) => {
      this.productFeature = productFeature;
      if (productFeature) {
        this.updateForm(productFeature);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeature = this.productFeatureFormService.getProductFeature(this.editForm);
    if (productFeature.id === null) {
      this.subscribeToSaveResponse(this.productFeatureService.create(productFeature));
    } else {
      this.subscribeToSaveResponse(this.productFeatureService.update(productFeature));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeature | null>): void {
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

  protected updateForm(productFeature: IProductFeature): void {
    this.productFeature = productFeature;
    this.productFeatureFormService.resetForm(this.editForm, productFeature);

    this.productFeatureTypesSharedCollection.update(productFeatureTypes =>
      this.productFeatureTypeService.addProductFeatureTypeToCollectionIfMissing<IProductFeatureType>(
        productFeatureTypes,
        productFeature.productFeatureType,
      ),
    );
    this.productFeatureCategoriesSharedCollection.update(productFeatureCategories =>
      this.productFeatureCategoryService.addProductFeatureCategoryToCollectionIfMissing<IProductFeatureCategory>(
        productFeatureCategories,
        productFeature.productFeatureCategory,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productFeatureTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductFeatureType[]>) => res.body ?? []))
      .pipe(
        map((productFeatureTypes: IProductFeatureType[]) =>
          this.productFeatureTypeService.addProductFeatureTypeToCollectionIfMissing<IProductFeatureType>(
            productFeatureTypes,
            this.productFeature?.productFeatureType,
          ),
        ),
      )
      .subscribe((productFeatureTypes: IProductFeatureType[]) => this.productFeatureTypesSharedCollection.set(productFeatureTypes));

    this.productFeatureCategoryService
      .query()
      .pipe(map((res: HttpResponse<IProductFeatureCategory[]>) => res.body ?? []))
      .pipe(
        map((productFeatureCategories: IProductFeatureCategory[]) =>
          this.productFeatureCategoryService.addProductFeatureCategoryToCollectionIfMissing<IProductFeatureCategory>(
            productFeatureCategories,
            this.productFeature?.productFeatureCategory,
          ),
        ),
      )
      .subscribe((productFeatureCategories: IProductFeatureCategory[]) =>
        this.productFeatureCategoriesSharedCollection.set(productFeatureCategories),
      );
  }
}
