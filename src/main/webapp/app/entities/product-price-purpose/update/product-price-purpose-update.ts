import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductPricePurpose } from '../product-price-purpose.model';
import { ProductPricePurposeService } from '../service/product-price-purpose.service';

import { ProductPricePurposeFormGroup, ProductPricePurposeFormService } from './product-price-purpose-form.service';

@Component({
  selector: 'sys-product-price-purpose-update',
  templateUrl: './product-price-purpose-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductPricePurposeUpdate implements OnInit {
  readonly isSaving = signal(false);
  productPricePurpose: IProductPricePurpose | null = null;

  protected productPricePurposeService = inject(ProductPricePurposeService);
  protected productPricePurposeFormService = inject(ProductPricePurposeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductPricePurposeFormGroup = this.productPricePurposeFormService.createProductPricePurposeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productPricePurpose }) => {
      this.productPricePurpose = productPricePurpose;
      if (productPricePurpose) {
        this.updateForm(productPricePurpose);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productPricePurpose = this.productPricePurposeFormService.getProductPricePurpose(this.editForm);
    if (productPricePurpose.id === null) {
      this.subscribeToSaveResponse(this.productPricePurposeService.create(productPricePurpose));
    } else {
      this.subscribeToSaveResponse(this.productPricePurposeService.update(productPricePurpose));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductPricePurpose | null>): void {
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

  protected updateForm(productPricePurpose: IProductPricePurpose): void {
    this.productPricePurpose = productPricePurpose;
    this.productPricePurposeFormService.resetForm(this.editForm, productPricePurpose);
  }
}
