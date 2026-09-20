import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ShipmentService } from 'app/entities/shipment/service/shipment.service';
import { IShipment } from 'app/entities/shipment/shipment.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShipmentStatusService } from '../service/shipment-status.service';
import { IShipmentStatus } from '../shipment-status.model';

import { ShipmentStatusFormGroup, ShipmentStatusFormService } from './shipment-status-form.service';

@Component({
  selector: 'sys-shipment-status-update',
  templateUrl: './shipment-status-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShipmentStatusUpdate implements OnInit {
  readonly isSaving = signal(false);
  shipmentStatus: IShipmentStatus | null = null;

  shipmentsSharedCollection = signal<IShipment[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);

  protected shipmentStatusService = inject(ShipmentStatusService);
  protected shipmentStatusFormService = inject(ShipmentStatusFormService);
  protected shipmentService = inject(ShipmentService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipmentStatusFormGroup = this.shipmentStatusFormService.createShipmentStatusFormGroup();

  compareShipment = (o1: IShipment | null, o2: IShipment | null): boolean => this.shipmentService.compareShipment(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentStatus }) => {
      this.shipmentStatus = shipmentStatus;
      if (shipmentStatus) {
        this.updateForm(shipmentStatus);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shipmentStatus = this.shipmentStatusFormService.getShipmentStatus(this.editForm);
    if (shipmentStatus.id === null) {
      this.subscribeToSaveResponse(this.shipmentStatusService.create(shipmentStatus));
    } else {
      this.subscribeToSaveResponse(this.shipmentStatusService.update(shipmentStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShipmentStatus | null>): void {
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

  protected updateForm(shipmentStatus: IShipmentStatus): void {
    this.shipmentStatus = shipmentStatus;
    this.shipmentStatusFormService.resetForm(this.editForm, shipmentStatus);

    this.shipmentsSharedCollection.update(shipments =>
      this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, shipmentStatus.shipment),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, shipmentStatus.status),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shipmentService
      .query()
      .pipe(map((res: HttpResponse<IShipment[]>) => res.body ?? []))
      .pipe(
        map((shipments: IShipment[]) =>
          this.shipmentService.addShipmentToCollectionIfMissing<IShipment>(shipments, this.shipmentStatus?.shipment),
        ),
      )
      .subscribe((shipments: IShipment[]) => this.shipmentsSharedCollection.set(shipments));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.shipmentStatus?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
