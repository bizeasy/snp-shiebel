import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

import { ProductTypeFormGroup, ProductTypeFormService } from './product-type-form.service';

@Component({
  selector: 'sys-product-type-update',
  templateUrl: './product-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  productType: IProductType | null = null;

  protected productTypeService = inject(ProductTypeService);
  protected productTypeFormService = inject(ProductTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductTypeFormGroup = this.productTypeFormService.createProductTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productType }) => {
      this.productType = productType;
      if (productType) {
        this.updateForm(productType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productType = this.productTypeFormService.getProductType(this.editForm);
    if (productType.id === null) {
      this.subscribeToSaveResponse(this.productTypeService.create(productType));
    } else {
      this.subscribeToSaveResponse(this.productTypeService.update(productType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductType | null>): void {
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

  protected updateForm(productType: IProductType): void {
    this.productType = productType;
    this.productTypeFormService.resetForm(this.editForm, productType);
  }
}
