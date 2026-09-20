import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureType } from '../product-feature-type.model';
import { ProductFeatureTypeService } from '../service/product-feature-type.service';

import { ProductFeatureTypeFormGroup, ProductFeatureTypeFormService } from './product-feature-type-form.service';

@Component({
  selector: 'sys-product-feature-type-update',
  templateUrl: './product-feature-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeatureType: IProductFeatureType | null = null;

  protected productFeatureTypeService = inject(ProductFeatureTypeService);
  protected productFeatureTypeFormService = inject(ProductFeatureTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureTypeFormGroup = this.productFeatureTypeFormService.createProductFeatureTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeatureType }) => {
      this.productFeatureType = productFeatureType;
      if (productFeatureType) {
        this.updateForm(productFeatureType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeatureType = this.productFeatureTypeFormService.getProductFeatureType(this.editForm);
    if (productFeatureType.id === null) {
      this.subscribeToSaveResponse(this.productFeatureTypeService.create(productFeatureType));
    } else {
      this.subscribeToSaveResponse(this.productFeatureTypeService.update(productFeatureType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeatureType | null>): void {
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

  protected updateForm(productFeatureType: IProductFeatureType): void {
    this.productFeatureType = productFeatureType;
    this.productFeatureTypeFormService.resetForm(this.editForm, productFeatureType);
  }
}
