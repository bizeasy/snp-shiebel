import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { ShipmentMethodTypeService } from 'app/entities/shipment-method-type/service/shipment-method-type.service';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityCarrierShipment } from '../facility-carrier-shipment.model';
import { FacilityCarrierShipmentService } from '../service/facility-carrier-shipment.service';

import { FacilityCarrierShipmentFormGroup, FacilityCarrierShipmentFormService } from './facility-carrier-shipment-form.service';

@Component({
  selector: 'sys-facility-carrier-shipment-update',
  templateUrl: './facility-carrier-shipment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityCarrierShipmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityCarrierShipment: IFacilityCarrierShipment | null = null;

  facilitiesSharedCollection = signal<IFacility[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);
  shipmentMethodTypesSharedCollection = signal<IShipmentMethodType[]>([]);

  protected facilityCarrierShipmentService = inject(FacilityCarrierShipmentService);
  protected facilityCarrierShipmentFormService = inject(FacilityCarrierShipmentFormService);
  protected facilityService = inject(FacilityService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected shipmentMethodTypeService = inject(ShipmentMethodTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityCarrierShipmentFormGroup = this.facilityCarrierShipmentFormService.createFacilityCarrierShipmentFormGroup();

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  compareShipmentMethodType = (o1: IShipmentMethodType | null, o2: IShipmentMethodType | null): boolean =>
    this.shipmentMethodTypeService.compareShipmentMethodType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityCarrierShipment }) => {
      this.facilityCarrierShipment = facilityCarrierShipment;
      if (facilityCarrierShipment) {
        this.updateForm(facilityCarrierShipment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityCarrierShipment = this.facilityCarrierShipmentFormService.getFacilityCarrierShipment(this.editForm);
    if (facilityCarrierShipment.id === null) {
      this.subscribeToSaveResponse(this.facilityCarrierShipmentService.create(facilityCarrierShipment));
    } else {
      this.subscribeToSaveResponse(this.facilityCarrierShipmentService.update(facilityCarrierShipment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityCarrierShipment | null>): void {
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

  protected updateForm(facilityCarrierShipment: IFacilityCarrierShipment): void {
    this.facilityCarrierShipment = facilityCarrierShipment;
    this.facilityCarrierShipmentFormService.resetForm(this.editForm, facilityCarrierShipment);

    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, facilityCarrierShipment.facility),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, facilityCarrierShipment.party),
    );
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, facilityCarrierShipment.roleType),
    );
    this.shipmentMethodTypesSharedCollection.update(shipmentMethodTypes =>
      this.shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing<IShipmentMethodType>(
        shipmentMethodTypes,
        facilityCarrierShipment.shipmentMethodType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.facilityCarrierShipment?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.facilityCarrierShipment?.party)),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.facilityCarrierShipment?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));

    this.shipmentMethodTypeService
      .query()
      .pipe(map((res: HttpResponse<IShipmentMethodType[]>) => res.body ?? []))
      .pipe(
        map((shipmentMethodTypes: IShipmentMethodType[]) =>
          this.shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing<IShipmentMethodType>(
            shipmentMethodTypes,
            this.facilityCarrierShipment?.shipmentMethodType,
          ),
        ),
      )
      .subscribe((shipmentMethodTypes: IShipmentMethodType[]) => this.shipmentMethodTypesSharedCollection.set(shipmentMethodTypes));
  }
}
