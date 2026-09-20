import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductAssocType } from '../product-assoc-type.model';
import { ProductAssocTypeService } from '../service/product-assoc-type.service';

import { ProductAssocTypeFormGroup, ProductAssocTypeFormService } from './product-assoc-type-form.service';

@Component({
  selector: 'sys-product-assoc-type-update',
  templateUrl: './product-assoc-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductAssocTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  productAssocType: IProductAssocType | null = null;

  protected productAssocTypeService = inject(ProductAssocTypeService);
  protected productAssocTypeFormService = inject(ProductAssocTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductAssocTypeFormGroup = this.productAssocTypeFormService.createProductAssocTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productAssocType }) => {
      this.productAssocType = productAssocType;
      if (productAssocType) {
        this.updateForm(productAssocType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productAssocType = this.productAssocTypeFormService.getProductAssocType(this.editForm);
    if (productAssocType.id === null) {
      this.subscribeToSaveResponse(this.productAssocTypeService.create(productAssocType));
    } else {
      this.subscribeToSaveResponse(this.productAssocTypeService.update(productAssocType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductAssocType | null>): void {
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

  protected updateForm(productAssocType: IProductAssocType): void {
    this.productAssocType = productAssocType;
    this.productAssocTypeFormService.resetForm(this.editForm, productAssocType);
  }
}
