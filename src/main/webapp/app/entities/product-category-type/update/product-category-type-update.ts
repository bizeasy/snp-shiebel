import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductCategoryType } from '../product-category-type.model';
import { ProductCategoryTypeService } from '../service/product-category-type.service';

import { ProductCategoryTypeFormGroup, ProductCategoryTypeFormService } from './product-category-type-form.service';

@Component({
  selector: 'sys-product-category-type-update',
  templateUrl: './product-category-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductCategoryTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  productCategoryType: IProductCategoryType | null = null;

  protected productCategoryTypeService = inject(ProductCategoryTypeService);
  protected productCategoryTypeFormService = inject(ProductCategoryTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductCategoryTypeFormGroup = this.productCategoryTypeFormService.createProductCategoryTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productCategoryType }) => {
      this.productCategoryType = productCategoryType;
      if (productCategoryType) {
        this.updateForm(productCategoryType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productCategoryType = this.productCategoryTypeFormService.getProductCategoryType(this.editForm);
    if (productCategoryType.id === null) {
      this.subscribeToSaveResponse(this.productCategoryTypeService.create(productCategoryType));
    } else {
      this.subscribeToSaveResponse(this.productCategoryTypeService.update(productCategoryType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductCategoryType | null>): void {
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

  protected updateForm(productCategoryType: IProductCategoryType): void {
    this.productCategoryType = productCategoryType;
    this.productCategoryTypeFormService.resetForm(this.editForm, productCategoryType);
  }
}
