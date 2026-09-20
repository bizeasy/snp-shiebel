import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { ShipmentMethodTypeService } from 'app/entities/shipment-method-type/service/shipment-method-type.service';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICarrierShipmentMethod } from '../carrier-shipment-method.model';
import { CarrierShipmentMethodService } from '../service/carrier-shipment-method.service';

import { CarrierShipmentMethodFormGroup, CarrierShipmentMethodFormService } from './carrier-shipment-method-form.service';

@Component({
  selector: 'sys-carrier-shipment-method-update',
  templateUrl: './carrier-shipment-method-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CarrierShipmentMethodUpdate implements OnInit {
  readonly isSaving = signal(false);
  carrierShipmentMethod: ICarrierShipmentMethod | null = null;

  shipmentMethodTypesSharedCollection = signal<IShipmentMethodType[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected carrierShipmentMethodService = inject(CarrierShipmentMethodService);
  protected carrierShipmentMethodFormService = inject(CarrierShipmentMethodFormService);
  protected shipmentMethodTypeService = inject(ShipmentMethodTypeService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CarrierShipmentMethodFormGroup = this.carrierShipmentMethodFormService.createCarrierShipmentMethodFormGroup();

  compareShipmentMethodType = (o1: IShipmentMethodType | null, o2: IShipmentMethodType | null): boolean =>
    this.shipmentMethodTypeService.compareShipmentMethodType(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carrierShipmentMethod }) => {
      this.carrierShipmentMethod = carrierShipmentMethod;
      if (carrierShipmentMethod) {
        this.updateForm(carrierShipmentMethod);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const carrierShipmentMethod = this.carrierShipmentMethodFormService.getCarrierShipmentMethod(this.editForm);
    if (carrierShipmentMethod.id === null) {
      this.subscribeToSaveResponse(this.carrierShipmentMethodService.create(carrierShipmentMethod));
    } else {
      this.subscribeToSaveResponse(this.carrierShipmentMethodService.update(carrierShipmentMethod));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICarrierShipmentMethod | null>): void {
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

  protected updateForm(carrierShipmentMethod: ICarrierShipmentMethod): void {
    this.carrierShipmentMethod = carrierShipmentMethod;
    this.carrierShipmentMethodFormService.resetForm(this.editForm, carrierShipmentMethod);

    this.shipmentMethodTypesSharedCollection.update(shipmentMethodTypes =>
      this.shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing<IShipmentMethodType>(
        shipmentMethodTypes,
        carrierShipmentMethod.shipmentMethodType,
      ),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, carrierShipmentMethod.party),
    );
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, carrierShipmentMethod.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shipmentMethodTypeService
      .query()
      .pipe(map((res: HttpResponse<IShipmentMethodType[]>) => res.body ?? []))
      .pipe(
        map((shipmentMethodTypes: IShipmentMethodType[]) =>
          this.shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing<IShipmentMethodType>(
            shipmentMethodTypes,
            this.carrierShipmentMethod?.shipmentMethodType,
          ),
        ),
      )
      .subscribe((shipmentMethodTypes: IShipmentMethodType[]) => this.shipmentMethodTypesSharedCollection.set(shipmentMethodTypes));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.carrierShipmentMethod?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.carrierShipmentMethod?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
