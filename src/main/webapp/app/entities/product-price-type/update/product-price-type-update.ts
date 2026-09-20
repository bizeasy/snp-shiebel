import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductPriceType } from '../product-price-type.model';
import { ProductPriceTypeService } from '../service/product-price-type.service';

import { ProductPriceTypeFormGroup, ProductPriceTypeFormService } from './product-price-type-form.service';

@Component({
  selector: 'sys-product-price-type-update',
  templateUrl: './product-price-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductPriceTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  productPriceType: IProductPriceType | null = null;

  protected productPriceTypeService = inject(ProductPriceTypeService);
  protected productPriceTypeFormService = inject(ProductPriceTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductPriceTypeFormGroup = this.productPriceTypeFormService.createProductPriceTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productPriceType }) => {
      this.productPriceType = productPriceType;
      if (productPriceType) {
        this.updateForm(productPriceType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productPriceType = this.productPriceTypeFormService.getProductPriceType(this.editForm);
    if (productPriceType.id === null) {
      this.subscribeToSaveResponse(this.productPriceTypeService.create(productPriceType));
    } else {
      this.subscribeToSaveResponse(this.productPriceTypeService.update(productPriceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductPriceType | null>): void {
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

  protected updateForm(productPriceType: IProductPriceType): void {
    this.productPriceType = productPriceType;
    this.productPriceTypeFormService.resetForm(this.editForm, productPriceType);
  }
}
