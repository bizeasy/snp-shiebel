import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureApplType } from '../product-feature-appl-type.model';
import { ProductFeatureApplTypeService } from '../service/product-feature-appl-type.service';

import { ProductFeatureApplTypeFormGroup, ProductFeatureApplTypeFormService } from './product-feature-appl-type-form.service';

@Component({
  selector: 'sys-product-feature-appl-type-update',
  templateUrl: './product-feature-appl-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureApplTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeatureApplType: IProductFeatureApplType | null = null;

  protected productFeatureApplTypeService = inject(ProductFeatureApplTypeService);
  protected productFeatureApplTypeFormService = inject(ProductFeatureApplTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureApplTypeFormGroup = this.productFeatureApplTypeFormService.createProductFeatureApplTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeatureApplType }) => {
      this.productFeatureApplType = productFeatureApplType;
      if (productFeatureApplType) {
        this.updateForm(productFeatureApplType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeatureApplType = this.productFeatureApplTypeFormService.getProductFeatureApplType(this.editForm);
    if (productFeatureApplType.id === null) {
      this.subscribeToSaveResponse(this.productFeatureApplTypeService.create(productFeatureApplType));
    } else {
      this.subscribeToSaveResponse(this.productFeatureApplTypeService.update(productFeatureApplType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeatureApplType | null>): void {
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

  protected updateForm(productFeatureApplType: IProductFeatureApplType): void {
    this.productFeatureApplType = productFeatureApplType;
    this.productFeatureApplTypeFormService.resetForm(this.editForm, productFeatureApplType);
  }
}
