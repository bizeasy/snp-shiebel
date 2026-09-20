import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFacility } from '../product-facility.model';
import { ProductFacilityService } from '../service/product-facility.service';

import { ProductFacilityFormGroup, ProductFacilityFormService } from './product-facility-form.service';

@Component({
  selector: 'sys-product-facility-update',
  templateUrl: './product-facility-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFacilityUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFacility: IProductFacility | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);

  protected productFacilityService = inject(ProductFacilityService);
  protected productFacilityFormService = inject(ProductFacilityFormService);
  protected productService = inject(ProductService);
  protected facilityService = inject(FacilityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFacilityFormGroup = this.productFacilityFormService.createProductFacilityFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFacility }) => {
      this.productFacility = productFacility;
      if (productFacility) {
        this.updateForm(productFacility);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFacility = this.productFacilityFormService.getProductFacility(this.editForm);
    if (productFacility.id === null) {
      this.subscribeToSaveResponse(this.productFacilityService.create(productFacility));
    } else {
      this.subscribeToSaveResponse(this.productFacilityService.update(productFacility));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFacility | null>): void {
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

  protected updateForm(productFacility: IProductFacility): void {
    this.productFacility = productFacility;
    this.productFacilityFormService.resetForm(this.editForm, productFacility);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productFacility.product),
    );
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, productFacility.facility),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productFacility?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.productFacility?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));
  }
}
