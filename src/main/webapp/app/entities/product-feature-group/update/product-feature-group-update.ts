import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureGroup } from '../product-feature-group.model';
import { ProductFeatureGroupService } from '../service/product-feature-group.service';

import { ProductFeatureGroupFormGroup, ProductFeatureGroupFormService } from './product-feature-group-form.service';

@Component({
  selector: 'sys-product-feature-group-update',
  templateUrl: './product-feature-group-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureGroupUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeatureGroup: IProductFeatureGroup | null = null;

  protected productFeatureGroupService = inject(ProductFeatureGroupService);
  protected productFeatureGroupFormService = inject(ProductFeatureGroupFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureGroupFormGroup = this.productFeatureGroupFormService.createProductFeatureGroupFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeatureGroup }) => {
      this.productFeatureGroup = productFeatureGroup;
      if (productFeatureGroup) {
        this.updateForm(productFeatureGroup);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeatureGroup = this.productFeatureGroupFormService.getProductFeatureGroup(this.editForm);
    if (productFeatureGroup.id === null) {
      this.subscribeToSaveResponse(this.productFeatureGroupService.create(productFeatureGroup));
    } else {
      this.subscribeToSaveResponse(this.productFeatureGroupService.update(productFeatureGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeatureGroup | null>): void {
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

  protected updateForm(productFeatureGroup: IProductFeatureGroup): void {
    this.productFeatureGroup = productFeatureGroup;
    this.productFeatureGroupFormService.resetForm(this.editForm, productFeatureGroup);
  }
}
