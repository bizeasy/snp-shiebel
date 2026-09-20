import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { ShipmentItemService } from 'app/entities/shipment-item/service/shipment-item.service';
import { IShipmentItem } from 'app/entities/shipment-item/shipment-item.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderShipment } from '../order-shipment.model';
import { OrderShipmentService } from '../service/order-shipment.service';

import { OrderShipmentFormGroup, OrderShipmentFormService } from './order-shipment-form.service';

@Component({
  selector: 'sys-order-shipment-update',
  templateUrl: './order-shipment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OrderShipmentUpdate implements OnInit {
  readonly isSaving = signal(false);
  orderShipment: IOrderShipment | null = null;

  ordersSharedCollection = signal<IOrder[]>([]);
  orderItemsSharedCollection = signal<IOrderItem[]>([]);
  shipmentsSharedCollection = signal<IShipment[]>([]);
  shipmentItemsSharedCollection = signal<IShipmentItem[]>([]);

  protected orderShipmentService = inject(OrderShipmentService);
  protected orderShipmentFormService = inject(OrderShipmentFormService);
  protected orderService = inject(OrderService);
  protected orderItemService = inject(OrderItemService);
  protected shipmentService = inject(ShipmentService);
  protected shipmentItemService = inject(ShipmentItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderShipmentFormGroup = this.orderShipmentFormService.createOrderShipmentFormGroup();

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareShipment = (o1: IShipment | null, o2: IShipment | null): boolean => this.shipmentService.compareShipment(o1, o2);

  compareShipmentItem = (o1: IShipmentItem | null, o2: IShipmentItem | null): boolean =>
    this.shipmentItemService.compareShipmentItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderShipment }) => {
      this.orderShipment = orderShipment;
      if (orderShipment) {
        this.updateForm(orderShipment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orderShipment = this.orderShipmentFormService.getOrderShipment(this.editForm);
    if (orderShipment.id === null) {
      this.subscribeToSaveResponse(this.orderShipmentService.create(orderShipment));
    } else {
      this.subscribeToSaveResponse(this.orderShipmentService.update(orderShipment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrderShipment | null>): void {
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

  protected updateForm(orderShipment: IOrderShipment): void {
    this.orderShipment = orderShipment;
    this.orderShipmentFormService.resetForm(this.editForm, orderShipment);

    this.ordersSharedCollection.update(orders => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, orderShipment.order));
    this.orderItemsSharedCollection.update(orderItems =>
      this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, orderShipment.orderItem),
    );
    this.shipmentsSharedCollection.update(shipments =>
      this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, orderShipment.shipment),
    );
    this.shipmentItemsSharedCollection.update(shipmentItems =>
      this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(shipmentItems, orderShipment.shipmentItem),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.orderShipment?.order)))
      .subscribe((orders: IOrder[]) => this.ordersSharedCollection.set(orders));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.orderShipment?.orderItem),
        ),
      )
      .subscribe((orderItems: IOrderItem[]) => this.orderItemsSharedCollection.set(orderItems));

    this.shipmentService
      .query()
      .pipe(map((res: HttpResponse<IShipment[]>) => res.body ?? []))
      .pipe(
        map((shipments: IShipment[]) =>
          this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, this.orderShipment?.shipment),
        ),
      )
      .subscribe((shipments: IShipment[]) => this.shipmentsSharedCollection.set(shipments));

    this.shipmentItemService
      .query()
      .pipe(map((res: HttpResponse<IShipmentItem[]>) => res.body ?? []))
      .pipe(
        map((shipmentItems: IShipmentItem[]) =>
          this.shipmentItemService.addShipmentItemToCollectionIfMissing<IShipmentItem>(shipmentItems, this.orderShipment?.shipmentItem),
        ),
      )
      .subscribe((shipmentItems: IShipmentItem[]) => this.shipmentItemsSharedCollection.set(shipmentItems));
  }
}
