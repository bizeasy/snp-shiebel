import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShipmentItemService } from '../service/shipment-item.service';
import { IShipmentItem } from '../shipment-item.model';

import { ShipmentItemFormGroup, ShipmentItemFormService } from './shipment-item-form.service';

@Component({
  selector: 'sys-shipment-item-update',
  templateUrl: './shipment-item-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShipmentItemUpdate implements OnInit {
  readonly isSaving = signal(false);
  shipmentItem: IShipmentItem | null = null;

  shipmentsSharedCollection = signal<IShipment[]>([]);
  productsSharedCollection = signal<IProduct[]>([]);

  protected shipmentItemService = inject(ShipmentItemService);
  protected shipmentItemFormService = inject(ShipmentItemFormService);
  protected shipmentService = inject(ShipmentService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipmentItemFormGroup = this.shipmentItemFormService.createShipmentItemFormGroup();

  compareShipment = (o1: IShipment | null, o2: IShipment | null): boolean => this.shipmentService.compareShipment(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentItem }) => {
      this.shipmentItem = shipmentItem;
      if (shipmentItem) {
        this.updateForm(shipmentItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shipmentItem = this.shipmentItemFormService.getShipmentItem(this.editForm);
    if (shipmentItem.id === null) {
      this.subscribeToSaveResponse(this.shipmentItemService.create(shipmentItem));
    } else {
      this.subscribeToSaveResponse(this.shipmentItemService.update(shipmentItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShipmentItem | null>): void {
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

  protected updateForm(shipmentItem: IShipmentItem): void {
    this.shipmentItem = shipmentItem;
    this.shipmentItemFormService.resetForm(this.editForm, shipmentItem);

    this.shipmentsSharedCollection.update(shipments =>
      this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, shipmentItem.shipment),
    );
    this.productsSharedCollection.update(products =>
      this.productService.addProductToCollectionIfMissing<IProduct>(products, shipmentItem.product),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shipmentService
      .query()
      .pipe(map((res: HttpResponse<IShipment[]>) => res.body ?? []))
      .pipe(
        map((shipments: IShipment[]) =>
          this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, this.shipmentItem?.shipment),
        ),
      )
      .subscribe((shipments: IShipment[]) => this.shipmentsSharedCollection.set(shipments));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.shipmentItem?.product)),
      )
      .subscribe((products: IProduct[]) => this.productsSharedCollection.set(products));
  }
}
