import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacilityGroup } from 'app/entities/facility-group/facility-group.model';
import { FacilityGroupService } from 'app/entities/facility-group/service/facility-group.service';
import { IFacilityType } from 'app/entities/facility-type/facility-type.model';
import { FacilityTypeService } from 'app/entities/facility-type/service/facility-type.service';
import { IGeoPoint } from 'app/entities/geo-point/geo-point.model';
import { GeoPointService } from 'app/entities/geo-point/service/geo-point.service';
import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { InventoryItemTypeService } from 'app/entities/inventory-item-type/service/inventory-item-type.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ProductStoreService } from 'app/entities/product-store/service/product-store.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacility } from '../facility.model';
import { FacilityService } from '../service/facility.service';

import { FacilityFormGroup, FacilityFormService } from './facility-form.service';

@Component({
  selector: 'sys-facility-update',
  templateUrl: './facility-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityUpdate implements OnInit {
  readonly isSaving = signal(false);
  facility: IFacility | null = null;

  facilityTypesSharedCollection = signal<IFacilityType[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  facilityGroupsSharedCollection = signal<IFacilityGroup[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  geoPointsSharedCollection = signal<IGeoPoint[]>([]);
  inventoryItemTypesSharedCollection = signal<IInventoryItemType[]>([]);
  productStoresSharedCollection = signal<IProductStore[]>([]);

  protected facilityService = inject(FacilityService);
  protected facilityFormService = inject(FacilityFormService);
  protected facilityTypeService = inject(FacilityTypeService);
  protected partyService = inject(PartyService);
  protected facilityGroupService = inject(FacilityGroupService);
  protected uomService = inject(UomService);
  protected geoPointService = inject(GeoPointService);
  protected inventoryItemTypeService = inject(InventoryItemTypeService);
  protected productStoreService = inject(ProductStoreService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityFormGroup = this.facilityFormService.createFacilityFormGroup();

  compareFacilityType = (o1: IFacilityType | null, o2: IFacilityType | null): boolean =>
    this.facilityTypeService.compareFacilityType(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareFacilityGroup = (o1: IFacilityGroup | null, o2: IFacilityGroup | null): boolean =>
    this.facilityGroupService.compareFacilityGroup(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareGeoPoint = (o1: IGeoPoint | null, o2: IGeoPoint | null): boolean => this.geoPointService.compareGeoPoint(o1, o2);

  compareInventoryItemType = (o1: IInventoryItemType | null, o2: IInventoryItemType | null): boolean =>
    this.inventoryItemTypeService.compareInventoryItemType(o1, o2);

  compareProductStore = (o1: IProductStore | null, o2: IProductStore | null): boolean =>
    this.productStoreService.compareProductStore(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facility }) => {
      this.facility = facility;
      if (facility) {
        this.updateForm(facility);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facility = this.facilityFormService.getFacility(this.editForm);
    if (facility.id === null) {
      this.subscribeToSaveResponse(this.facilityService.create(facility));
    } else {
      this.subscribeToSaveResponse(this.facilityService.update(facility));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacility | null>): void {
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

  protected updateForm(facility: IFacility): void {
    this.facility = facility;
    this.facilityFormService.resetForm(this.editForm, facility);

    this.facilityTypesSharedCollection.update(facilityTypes =>
      this.facilityTypeService.addFacilityTypeToCollectionIfMissing<IFacilityType>(facilityTypes, facility.facilityType),
    );
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, facility.parentFacility),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, facility.ownerParty));
    this.facilityGroupsSharedCollection.update(facilityGroups =>
      this.facilityGroupService.addFacilityGroupToCollectionIfMissing<IFacilityGroup>(facilityGroups, facility.facilityGroup),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, facility.sizeUom));
    this.geoPointsSharedCollection.update(geoPoints =>
      this.geoPointService.addGeoPointToCollectionIfMissing<IGeoPoint>(geoPoints, facility.geoPoint),
    );
    this.inventoryItemTypesSharedCollection.update(inventoryItemTypes =>
      this.inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing<IInventoryItemType>(
        inventoryItemTypes,
        facility.inventoryItemType,
      ),
    );
    this.productStoresSharedCollection.update(productStores =>
      this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, facility.productStore),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facilityTypeService
      .query()
      .pipe(map((res: HttpResponse<IFacilityType[]>) => res.body ?? []))
      .pipe(
        map((facilityTypes: IFacilityType[]) =>
          this.facilityTypeService.addFacilityTypeToCollectionIfMissing<IFacilityType>(facilityTypes, this.facility?.facilityType),
        ),
      )
      .subscribe((facilityTypes: IFacilityType[]) => this.facilityTypesSharedCollection.set(facilityTypes));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.facility?.parentFacility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.facility?.ownerParty)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.facilityGroupService
      .query()
      .pipe(map((res: HttpResponse<IFacilityGroup[]>) => res.body ?? []))
      .pipe(
        map((facilityGroups: IFacilityGroup[]) =>
          this.facilityGroupService.addFacilityGroupToCollectionIfMissing<IFacilityGroup>(facilityGroups, this.facility?.facilityGroup),
        ),
      )
      .subscribe((facilityGroups: IFacilityGroup[]) => this.facilityGroupsSharedCollection.set(facilityGroups));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.facility?.sizeUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.geoPointService
      .query()
      .pipe(map((res: HttpResponse<IGeoPoint[]>) => res.body ?? []))
      .pipe(
        map((geoPoints: IGeoPoint[]) =>
          this.geoPointService.addGeoPointToCollectionIfMissing<IGeoPoint>(geoPoints, this.facility?.geoPoint),
        ),
      )
      .subscribe((geoPoints: IGeoPoint[]) => this.geoPointsSharedCollection.set(geoPoints));

    this.inventoryItemTypeService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItemType[]>) => res.body ?? []))
      .pipe(
        map((inventoryItemTypes: IInventoryItemType[]) =>
          this.inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing<IInventoryItemType>(
            inventoryItemTypes,
            this.facility?.inventoryItemType,
          ),
        ),
      )
      .subscribe((inventoryItemTypes: IInventoryItemType[]) => this.inventoryItemTypesSharedCollection.set(inventoryItemTypes));

    this.productStoreService
      .query()
      .pipe(map((res: HttpResponse<IProductStore[]>) => res.body ?? []))
      .pipe(
        map((productStores: IProductStore[]) =>
          this.productStoreService.addProductStoreToCollectionIfMissing<IProductStore>(productStores, this.facility?.productStore),
        ),
      )
      .subscribe((productStores: IProductStore[]) => this.productStoresSharedCollection.set(productStores));
  }
}
