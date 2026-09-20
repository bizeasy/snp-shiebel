import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureCategory } from '../product-feature-category.model';
import { ProductFeatureCategoryService } from '../service/product-feature-category.service';

import { ProductFeatureCategoryFormGroup, ProductFeatureCategoryFormService } from './product-feature-category-form.service';

@Component({
  selector: 'sys-product-feature-category-update',
  templateUrl: './product-feature-category-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureCategoryUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeatureCategory: IProductFeatureCategory | null = null;

  protected productFeatureCategoryService = inject(ProductFeatureCategoryService);
  protected productFeatureCategoryFormService = inject(ProductFeatureCategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureCategoryFormGroup = this.productFeatureCategoryFormService.createProductFeatureCategoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeatureCategory }) => {
      this.productFeatureCategory = productFeatureCategory;
      if (productFeatureCategory) {
        this.updateForm(productFeatureCategory);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeatureCategory = this.productFeatureCategoryFormService.getProductFeatureCategory(this.editForm);
    if (productFeatureCategory.id === null) {
      this.subscribeToSaveResponse(this.productFeatureCategoryService.create(productFeatureCategory));
    } else {
      this.subscribeToSaveResponse(this.productFeatureCategoryService.update(productFeatureCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeatureCategory | null>): void {
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

  protected updateForm(productFeatureCategory: IProductFeatureCategory): void {
    this.productFeatureCategory = productFeatureCategory;
    this.productFeatureCategoryFormService.resetForm(this.editForm, productFeatureCategory);
  }
}
