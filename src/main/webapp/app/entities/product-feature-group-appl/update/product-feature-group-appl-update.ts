import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IProductFeatureGroup } from 'app/entities/product-feature-group/product-feature-group.model';
import { ProductFeatureGroupService } from 'app/entities/product-feature-group/service/product-feature-group.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureGroupAppl } from '../product-feature-group-appl.model';
import { ProductFeatureGroupApplService } from '../service/product-feature-group-appl.service';

import { ProductFeatureGroupApplFormGroup, ProductFeatureGroupApplFormService } from './product-feature-group-appl-form.service';

@Component({
  selector: 'sys-product-feature-group-appl-update',
  templateUrl: './product-feature-group-appl-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFeatureGroupApplUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFeatureGroupAppl: IProductFeatureGroupAppl | null = null;

  productFeatureGroupsSharedCollection = signal<IProductFeatureGroup[]>([]);
  productFeaturesSharedCollection = signal<IProductFeature[]>([]);

  protected productFeatureGroupApplService = inject(ProductFeatureGroupApplService);
  protected productFeatureGroupApplFormService = inject(ProductFeatureGroupApplFormService);
  protected productFeatureGroupService = inject(ProductFeatureGroupService);
  protected productFeatureService = inject(ProductFeatureService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFeatureGroupApplFormGroup = this.productFeatureGroupApplFormService.createProductFeatureGroupApplFormGroup();

  compareProductFeatureGroup = (o1: IProductFeatureGroup | null, o2: IProductFeatureGroup | null): boolean =>
    this.productFeatureGroupService.compareProductFeatureGroup(o1, o2);

  compareProductFeature = (o1: IProductFeature | null, o2: IProductFeature | null): boolean =>
    this.productFeatureService.compareProductFeature(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFeatureGroupAppl }) => {
      this.productFeatureGroupAppl = productFeatureGroupAppl;
      if (productFeatureGroupAppl) {
        this.updateForm(productFeatureGroupAppl);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFeatureGroupAppl = this.productFeatureGroupApplFormService.getProductFeatureGroupAppl(this.editForm);
    if (productFeatureGroupAppl.id === null) {
      this.subscribeToSaveResponse(this.productFeatureGroupApplService.create(productFeatureGroupAppl));
    } else {
      this.subscribeToSaveResponse(this.productFeatureGroupApplService.update(productFeatureGroupAppl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFeatureGroupAppl | null>): void {
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

  protected updateForm(productFeatureGroupAppl: IProductFeatureGroupAppl): void {
    this.productFeatureGroupAppl = productFeatureGroupAppl;
    this.productFeatureGroupApplFormService.resetForm(this.editForm, productFeatureGroupAppl);

    this.productFeatureGroupsSharedCollection.update(productFeatureGroups =>
      this.productFeatureGroupService.addProductFeatureGroupToCollectionIfMissing<IProductFeatureGroup>(
        productFeatureGroups,
        productFeatureGroupAppl.productFeatureGroup,
      ),
    );
    this.productFeaturesSharedCollection.update(productFeatures =>
      this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
        productFeatures,
        productFeatureGroupAppl.productFeature,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productFeatureGroupService
      .query()
      .pipe(map((res: HttpResponse<IProductFeatureGroup[]>) => res.body ?? []))
      .pipe(
        map((productFeatureGroups: IProductFeatureGroup[]) =>
          this.productFeatureGroupService.addProductFeatureGroupToCollectionIfMissing<IProductFeatureGroup>(
            productFeatureGroups,
            this.productFeatureGroupAppl?.productFeatureGroup,
          ),
        ),
      )
      .subscribe((productFeatureGroups: IProductFeatureGroup[]) => this.productFeatureGroupsSharedCollection.set(productFeatureGroups));

    this.productFeatureService
      .query()
      .pipe(map((res: HttpResponse<IProductFeature[]>) => res.body ?? []))
      .pipe(
        map((productFeatures: IProductFeature[]) =>
          this.productFeatureService.addProductFeatureToCollectionIfMissing<IProductFeature>(
            productFeatures,
            this.productFeatureGroupAppl?.productFeature,
          ),
        ),
      )
      .subscribe((productFeatures: IProductFeature[]) => this.productFeaturesSharedCollection.set(productFeatures));
  }
}
