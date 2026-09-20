import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { FacilityLocationService } from 'app/entities/facility-location/service/facility-location.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFacilityLocation } from '../product-facility-location.model';
import { ProductFacilityLocationService } from '../service/product-facility-location.service';

import { ProductFacilityLocationFormGroup, ProductFacilityLocationFormService } from './product-facility-location-form.service';

@Component({
  selector: 'sys-product-facility-location-update',
  templateUrl: './product-facility-location-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFacilityLocationUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFacilityLocation: IProductFacilityLocation | null = null;

  productsSharedCollection = signal<IProduct[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);
  facilityLocationsSharedCollection = signal<IFacilityLocation[]>([]);

  protected productFacilityLocationService = inject(ProductFacilityLocationService);
  protected productFacilityLocationFormService = inject(ProductFacilityLocationFormService);
  protected productService = inject(ProductService);
  protected facilityService = inject(FacilityService);
  protected facilityLocationService = inject(FacilityLocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFacilityLocationFormGroup = this.productFacilityLocationFormService.createProductFacilityLocationFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareFacilityLocation = (o1: IFacilityLocation | null, o2: IFacilityLocation | null): boolean =>
    this.facilityLocationService.compareFacilityLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFacilityLocation }) => {
      this.productFacilityLocation = productFacilityLocation;
      if (productFacilityLocation) {
        this.updateForm(productFacilityLocation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFacilityLocation = this.productFacilityLocationFormService.getProductFacilityLocation(this.editForm);
    if (productFacilityLocation.id === null) {
      this.subscribeToSaveResponse(this.productFacilityLocationService.create(productFacilityLocation));
    } else {
      this.subscribeToSaveResponse(this.productFacilityLocationService.update(productFacilityLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFacilityLocation | null>): void {
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

  protected updateForm(productFacilityLocation: IProductFacilityLocation): void {
    this.productFacilityLocation = productFacilityLocation;
    this.productFacilityLocationFormService.resetForm(this.editForm, productFacilityLocation);

    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, productFacilityLocation.product),
    );
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, productFacilityLocation.facility),
    );
    this.facilityLocationsSharedCollection.update(facilityLocations =>
      this.facilityLocationService.addFacilityLocationToCollectionIfMissing<IFacilityLocation>(
        facilityLocations,
        productFacilityLocation.facilityLocation,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productFacilityLocation?.product),
        ),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.productFacilityLocation?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.facilityLocationService
      .query()
      .pipe(map((res: HttpResponse<IFacilityLocation[]>) => res.body ?? []))
      .pipe(
        map((facilityLocations: IFacilityLocation[]) =>
          this.facilityLocationService.addFacilityLocationToCollectionIfMissing<IFacilityLocation>(
            facilityLocations,
            this.productFacilityLocation?.facilityLocation,
          ),
        ),
      )
      .subscribe((facilityLocations: IFacilityLocation[]) => this.facilityLocationsSharedCollection.set(facilityLocations));
  }
}
