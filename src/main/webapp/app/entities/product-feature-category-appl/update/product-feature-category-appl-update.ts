import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { IProductFeatureCategory } from 'app/entities/product-feature-category/product-feature-category.model';
import { ProductFeatureCategoryService } from 'app/entities/product-feature-category/service/product-feature-category.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureCategoryAppl } from '../product-feature-category-appl.model';
import { ProductFeatureCategoryApplService } from '../service/product-feature-category-appl.service';

import { ProductFeatureCategoryApplFormGroup, ProductFeatureCategoryApplFormService } from './product-feature-category-appl-form.service';

@Component({
  selector: 'sys-product-feature-category-appl-update',
  templateUrl: './product-feature-category-appl-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureCategoryApplUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeatureCategoryAppl: IProductFeatureCategoryAppl | null = null;

  productCategoriesSharedCollection = signal<IProductCategory[]>([]);
  productFeatureCategoriesSharedCollection = signal<IProductFeatureCategory[]>([]);

  protected productFeatureCategoryApplService = inject(ProductFeatureCategoryApplService);
  protected productFeatureCategoryApplFormService = inject(ProductFeatureCategoryApplFormService);
  protected productCategoryService = inject(ProductCategoryService);
  protected productFeatureCategoryService = inject(ProductFeatureCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureCategoryApplFormGroup = this.productFeatureCategoryApplFormService.createProductFeatureCategoryApplFormGroup();

  compareProductCategory = (o1: IProductCategory | null, o2: IProductCategory | null): boolean =>
    this.productCategoryService.compareProductCategory(o1, o2);

  compareProductFeatureCategory = (o1: IProductFeatureCategory | null, o2: IProductFeatureCategory | null): boolean =>
    this.productFeatureCategoryService.compareProductFeatureCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeatureCategoryAppl }) => {
      this.productFeatureCategoryAppl = productFeatureCategoryAppl;
      if (productFeatureCategoryAppl) {
        this.updateForm(productFeatureCategoryAppl);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeatureCategoryAppl = this.productFeatureCategoryApplFormService.getProductFeatureCategoryAppl(this.editForm);
    if (productFeatureCategoryAppl.id === null) {
      this.subscribeToSaveResponse(this.productFeatureCategoryApplService.create(productFeatureCategoryAppl));
    } else {
      this.subscribeToSaveResponse(this.productFeatureCategoryApplService.update(productFeatureCategoryAppl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeatureCategoryAppl | null>): void {
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

  protected updateForm(productFeatureCategoryAppl: IProductFeatureCategoryAppl): void {
    this.productFeatureCategoryAppl = productFeatureCategoryAppl;
    this.productFeatureCategoryApplFormService.resetForm(this.editForm, productFeatureCategoryAppl);

    this.productCategoriesSharedCollection.update(productCategories =>
      this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
        productCategories,
        productFeatureCategoryAppl.productCategory,
      ),
    );
    this.productFeatureCategoriesSharedCollection.update(productFeatureCategories =>
      this.productFeatureCategoryService.addProductFeatureCategoryToCollectionIfMissing<IProductFeatureCategory>(
        productFeatureCategories,
        productFeatureCategoryAppl.productFeatureCategory,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productCategoryService
      .query()
      .pipe(map((res: HttpResponse<IProductCategory[]>) => res.body ?? []))
      .pipe(
        map((productCategories: IProductCategory[]) =>
          this.productCategoryService.addProductCategoryToCollectionIfMissing<IProductCategory>(
            productCategories,
            this.productFeatureCategoryAppl?.productCategory,
          ),
        ),
      )
      .subscribe((productCategories: IProductCategory[]) => this.productCategoriesSharedCollection.set(productCategories));

    this.productFeatureCategoryService
      .query()
      .pipe(map((res: HttpResponse<IProductFeatureCategory[]>) => res.body ?? []))
      .pipe(
        map((productFeatureCategories: IProductFeatureCategory[]) =>
          this.productFeatureCategoryService.addProductFeatureCategoryToCollectionIfMissing<IProductFeatureCategory>(
            productFeatureCategories,
            this.productFeatureCategoryAppl?.productFeatureCategory,
          ),
        ),
      )
      .subscribe((productFeatureCategories: IProductFeatureCategory[]) =>
        this.productFeatureCategoriesSharedCollection.set(productFeatureCategories),
      );
  }
}
