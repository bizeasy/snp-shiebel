import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IFacility } from 'app/entities/facility/facility.model';
import { IOrder } from 'app/entities/order/order.model';
import { ShipmentMethodTypeService } from 'app/entities/shipment-method-type/service/shipment-method-type.service';
import { IShipmentMethodType } from 'app/entities/shipment-method-type/shipment-method-type.model';
import { ShipmentTypeService } from 'app/entities/shipment-type/service/shipment-type.service';
import { IShipmentType } from 'app/entities/shipment-type/shipment-type.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { ShipmentService } from '../service/shipment.service';
import { IShipment } from '../shipment.model';

import { ShipmentFormGroup, ShipmentFormService } from './shipment-form.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';

@Component({
  selector: 'sys-shipment-update',
  templateUrl: './shipment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShipmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  shipment: IShipment | null = null;

  shipmentTypesSharedCollection = signal<IShipmentType[]>([]);
  shipmentMethodTypesSharedCollection = signal<IShipmentMethodType[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  contactMechesSharedCollection = signal<IContactMech[]>([]);

  protected shipmentService = inject(ShipmentService);
  protected shipmentFormService = inject(ShipmentFormService);
  protected shipmentTypeService = inject(ShipmentTypeService);
  protected shipmentMethodTypeService = inject(ShipmentMethodTypeService);
  protected statusService = inject(StatusService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected facilityService = inject(FacilityService);
  protected partyService = inject(PartyService);
  protected contactMechService = inject(ContactMechService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipmentFormGroup = this.shipmentFormService.createShipmentFormGroup();

  compareShipmentType = (o1: IShipmentType | null, o2: IShipmentType | null): boolean =>
    this.shipmentTypeService.compareShipmentType(o1, o2);

  compareShipmentMethodType = (o1: IShipmentMethodType | null, o2: IShipmentMethodType | null): boolean =>
    this.shipmentMethodTypeService.compareShipmentMethodType(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareContactMech = (o1: IContactMech | null, o2: IContactMech | null): boolean => this.contactMechService.compareContactMech(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipment }) => {
      this.shipment = shipment;
      if (shipment) {
        this.updateForm(shipment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shipment = this.shipmentFormService.getShipment(this.editForm);
    if (shipment.id === null) {
      this.subscribeToSaveResponse(this.shipmentService.create(shipment));
    } else {
      this.subscribeToSaveResponse(this.shipmentService.update(shipment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShipment | null>): void {
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

  protected updateForm(shipment: IShipment): void {
    this.shipment = shipment;
    this.shipmentFormService.resetForm(this.editForm, shipment);

    this.shipmentTypesSharedCollection.update(shipmentTypes =>
      this.shipmentTypeService.addShipmentTypeToCollectionIfMissing<IShipmentType>(shipmentTypes, shipment.shipmentType),
    );
    this.shipmentMethodTypesSharedCollection.update(shipmentMethodTypes =>
      this.shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing<IShipmentMethodType>(
        shipmentMethodTypes,
        shipment.shipmentMethodType,
      ),
    );
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, shipment.status));
    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, shipment.order));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, shipment.orderItem),
    );
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, shipment.originFacility, shipment.destinationFacility),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, shipment.fromParty, shipment.toParty, shipment.carrierParty),
    );
    this.contactMechesSharedCollection.update(contactMeches =>
      this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, shipment.contactMech),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shipmentTypeService
      .query()
      .pipe(map((res: HttpResponse<IShipmentType[]>) => res.body ?? []))
      .pipe(
        map((shipmentTypes: IShipmentType[]) =>
          this.shipmentTypeService.addShipmentTypeToCollectionIfMissing<IShipmentType>(shipmentTypes, this.shipment?.shipmentType),
        ),
      )
      .subscribe((shipmentTypes: IShipmentType[]) => this.shipmentTypesSharedCollection.set(shipmentTypes));

    this.shipmentMethodTypeService
      .query()
      .pipe(map((res: HttpResponse<IShipmentMethodType[]>) => res.body ?? []))
      .pipe(
        map((shipmentMethodTypes: IShipmentMethodType[]) =>
          this.shipmentMethodTypeService.addShipmentMethodTypeToCollectionIfMissing<IShipmentMethodType>(
            shipmentMethodTypes,
            this.shipment?.shipmentMethodType,
          ),
        ),
      )
      .subscribe((shipmentMethodTypes: IShipmentMethodType[]) => this.shipmentMethodTypesSharedCollection.set(shipmentMethodTypes));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.shipment?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.shipment?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.shipment?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(
            facilities,
            this.shipment?.originFacility,
            this.shipment?.destinationFacility,
          ),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(
            parties,
            this.shipment?.fromParty,
            this.shipment?.toParty,
            this.shipment?.carrierParty,
          ),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.contactMechService
      .query()
      .pipe(map((res: HttpResponse<IContactMech[]>) => res.body ?? []))
      .pipe(
        map((contactMeches: IContactMech[]) =>
          this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, this.shipment?.contactMech),
        ),
      )
      .subscribe((contactMeches: IContactMech[]) => this.contactMechesSharedCollection.set(contactMeches));
  }
}
