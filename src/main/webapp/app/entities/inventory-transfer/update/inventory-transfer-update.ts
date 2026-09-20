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
import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { IItemIssuance } from 'app/entities/item-issuance/item-issuance.model';
import { ItemIssuanceService } from 'app/entities/item-issuance/service/item-issuance.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryTransfer } from '../inventory-transfer.model';
import { InventoryTransferService } from '../service/inventory-transfer.service';

import { InventoryTransferFormGroup, InventoryTransferFormService } from './inventory-transfer-form.service';

@Component({
  selector: 'sys-inventory-transfer-update',
  templateUrl: './inventory-transfer-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InventoryTransferUpdate implements OnInit {
  readonly isSaving = signal(false);
  inventoryTransfer: IInventoryTransfer | null = null;

  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);
  facilityLocationsSharedCollection = signal<IFacilityLocation[]>([]);
  itemIssuancesSharedCollection = signal<IItemIssuance[]>([]);

  protected inventoryTransferService = inject(InventoryTransferService);
  protected inventoryTransferFormService = inject(InventoryTransferFormService);
  protected inventoryItemService = inject(InventoryItemService);
  protected statusService = inject(StatusService);
  protected facilityService = inject(FacilityService);
  protected facilityLocationService = inject(FacilityLocationService);
  protected itemIssuanceService = inject(ItemIssuanceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryTransferFormGroup = this.inventoryTransferFormService.createInventoryTransferFormGroup();

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareFacilityLocation = (o1: IFacilityLocation | null, o2: IFacilityLocation | null): boolean =>
    this.facilityLocationService.compareFacilityLocation(o1, o2);

  compareItemIssuance = (o1: IItemIssuance | null, o2: IItemIssuance | null): boolean =>
    this.itemIssuanceService.compareItemIssuance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryTransfer }) => {
      this.inventoryTransfer = inventoryTransfer;
      if (inventoryTransfer) {
        this.updateForm(inventoryTransfer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const inventoryTransfer = this.inventoryTransferFormService.getInventoryTransfer(this.editForm);
    if (inventoryTransfer.id === null) {
      this.subscribeToSaveResponse(this.inventoryTransferService.create(inventoryTransfer));
    } else {
      this.subscribeToSaveResponse(this.inventoryTransferService.update(inventoryTransfer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInventoryTransfer | null>): void {
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

  protected updateForm(inventoryTransfer: IInventoryTransfer): void {
    this.inventoryTransfer = inventoryTransfer;
    this.inventoryTransferFormService.resetForm(this.editForm, inventoryTransfer);

    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, inventoryTransfer.inventoryItem),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, inventoryTransfer.status),
    );
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(
        facilities,
        inventoryTransfer.facility,
        inventoryTransfer.facilityTo,
      ),
    );
    this.facilityLocationsSharedCollection.update(facilityLocations =>
      this.facilityLocationService.addFacilityLocationToCollectionIfMissing<IFacilityLocation>(
        facilityLocations,
        inventoryTransfer.facilityLocation,
        inventoryTransfer.facilityLocationTo,
      ),
    );
    this.itemIssuancesSharedCollection.update(itemIssuances =>
      this.itemIssuanceService.addItemIssuanceToCollectionIfMissing<IItemIssuance>(itemIssuances, inventoryTransfer.issuance),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inventoryItemService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItem[]>) => res.body ?? []))
      .pipe(
        map((inventoryItems: IInventoryItem[]) =>
          this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(
            inventoryItems,
            this.inventoryTransfer?.inventoryItem,
          ),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(
        map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.inventoryTransfer?.status)),
      )
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(
            facilities,
            this.inventoryTransfer?.facility,
            this.inventoryTransfer?.facilityTo,
          ),
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
            this.inventoryTransfer?.facilityLocation,
            this.inventoryTransfer?.facilityLocationTo,
          ),
        ),
      )
      .subscribe((facilityLocations: IFacilityLocation[]) => this.facilityLocationsSharedCollection.set(facilityLocations));

    this.itemIssuanceService
      .query()
      .pipe(map((res: HttpResponse<IItemIssuance[]>) => res.body ?? []))
      .pipe(
        map((itemIssuances: IItemIssuance[]) =>
          this.itemIssuanceService.addItemIssuanceToCollectionIfMissing<IItemIssuance>(itemIssuances, this.inventoryTransfer?.issuance),
        ),
      )
      .subscribe((itemIssuances: IItemIssuance[]) => this.itemIssuancesSharedCollection.set(itemIssuances));
  }
}
