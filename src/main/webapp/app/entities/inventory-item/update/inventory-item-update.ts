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
import { IInventoryItemType } from 'app/entities/inventory-item-type/inventory-item-type.model';
import { InventoryItemTypeService } from 'app/entities/inventory-item-type/service/inventory-item-type.service';
import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { PartyService } from 'app/entities/party/service/party.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { ILot } from 'app/entities/lot/lot.model';
import { LotService } from 'app/entities/lot/service/lot.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItem } from '../inventory-item.model';
import { InventoryItemService } from '../service/inventory-item.service';

import { InventoryItemFormGroup, InventoryItemFormService } from './inventory-item-form.service';

@Component({
  selector: 'sys-inventory-item-update',
  templateUrl: './inventory-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InventoryItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  inventoryItem: IInventoryItem | null = null;

  inventoryItemTypesSharedCollection = signal<IInventoryItemType[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);
  facilityLocationsSharedCollection = signal<IFacilityLocation[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);
  lotsSharedCollection = signal<ILot[]>([]);

  protected inventoryItemService = inject(InventoryItemService);
  protected inventoryItemFormService = inject(InventoryItemFormService);
  protected inventoryItemTypeService = inject(InventoryItemTypeService);
  protected productService = inject(ProductService);
  protected partyService = inject(PartyService);
  protected statusService = inject(StatusService);
  protected facilityService = inject(FacilityService);
  protected facilityLocationService = inject(FacilityLocationService);
  protected uomService = inject(UomService);
  protected lotService = inject(LotService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryItemFormGroup = this.inventoryItemFormService.createInventoryItemFormGroup();

  compareInventoryItemType = (o1: IInventoryItemType | null, o2: IInventoryItemType | null): boolean =>
    this.inventoryItemTypeService.compareInventoryItemType(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareFacilityLocation = (o1: IFacilityLocation | null, o2: IFacilityLocation | null): boolean =>
    this.facilityLocationService.compareFacilityLocation(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  compareLot = (o1: ILot | null, o2: ILot | null): boolean => this.lotService.compareLot(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItem }) => {
      this.inventoryItem = inventoryItem;
      if (inventoryItem) {
        this.updateForm(inventoryItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const inventoryItem = this.inventoryItemFormService.getInventoryItem(this.editForm);
    if (inventoryItem.id === null) {
      this.subscribeToSaveResponse(this.inventoryItemService.create(inventoryItem));
    } else {
      this.subscribeToSaveResponse(this.inventoryItemService.update(inventoryItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInventoryItem | null>): void {
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

  protected updateForm(inventoryItem: IInventoryItem): void {
    this.inventoryItem = inventoryItem;
    this.inventoryItemFormService.resetForm(this.editForm, inventoryItem);

    this.inventoryItemTypesSharedCollection.update(inventoryItemTypes =>
      this.inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing<IInventoryItemType>(
        inventoryItemTypes,
        inventoryItem.inventoryItemType,
      ),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, inventoryItem.product),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, inventoryItem.supplier, inventoryItem.ownerParty),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, inventoryItem.status),
    );
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, inventoryItem.facility),
    );
    this.facilityLocationsSharedCollection.update(facilityLocations =>
      this.facilityLocationService.addFacilityLocationToCollectionIfMissing<IFacilityLocation>(
        facilityLocations,
        inventoryItem.facilityLocation,
      ),
    );
    this.uomsSharedCollection.update(uoms =>
      this.uomService.addUomToCollectionIfMissing<IUom>(uoms, inventoryItem.uom, inventoryItem.currencyUom),
    );
    this.lotsSharedCollection.update(lots => this.lotService.addLotToCollectionIfMissing<ILot>(lots, inventoryItem.lot));
  }

  protected loadRelationshipsOptions(): void {
    this.inventoryItemTypeService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItemType[]>) => res.body ?? []))
      .pipe(
        map((inventoryItemTypes: IInventoryItemType[]) =>
          this.inventoryItemTypeService.addInventoryItemTypeToCollectionIfMissing<IInventoryItemType>(
            inventoryItemTypes,
            this.inventoryItem?.inventoryItemType,
          ),
        ),
      )
      .subscribe((inventoryItemTypes: IInventoryItemType[]) => this.inventoryItemTypesSharedCollection.set(inventoryItemTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.inventoryItem?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.inventoryItem?.supplier, this.inventoryItem?.ownerParty),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.inventoryItem?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.inventoryItem?.facility),
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
            this.inventoryItem?.facilityLocation,
          ),
        ),
      )
      .subscribe((facilityLocations: IFacilityLocation[]) => this.facilityLocationsSharedCollection.set(facilityLocations));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(
        map((uoms: IUom[]) =>
          this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.inventoryItem?.uom, this.inventoryItem?.currencyUom),
        ),
      )
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));

    this.lotService
      .query()
      .pipe(map((res: HttpResponse<ILot[]>) => res.body ?? []))
      .pipe(map((lots: ILot[]) => this.lotService.addLotToCollectionIfMissing<ILot>(lots, this.inventoryItem?.lot)))
      .subscribe((lots: ILot[]) => this.lotsSharedCollection.set(lots));
  }
}
