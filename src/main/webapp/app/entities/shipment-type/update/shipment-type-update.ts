import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShipmentTypeService } from '../service/shipment-type.service';
import { IShipmentType } from '../shipment-type.model';

import { ShipmentTypeFormGroup, ShipmentTypeFormService } from './shipment-type-form.service';

@Component({
  selector: 'sys-shipment-type-update',
  templateUrl: './shipment-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShipmentTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  shipmentType: IShipmentType | null = null;

  protected shipmentTypeService = inject(ShipmentTypeService);
  protected shipmentTypeFormService = inject(ShipmentTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipmentTypeFormGroup = this.shipmentTypeFormService.createShipmentTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentType }) => {
      this.shipmentType = shipmentType;
      if (shipmentType) {
        this.updateForm(shipmentType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shipmentType = this.shipmentTypeFormService.getShipmentType(this.editForm);
    if (shipmentType.id === null) {
      this.subscribeToSaveResponse(this.shipmentTypeService.create(shipmentType));
    } else {
      this.subscribeToSaveResponse(this.shipmentTypeService.update(shipmentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShipmentType | null>): void {
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

  protected updateForm(shipmentType: IShipmentType): void {
    this.shipmentType = shipmentType;
    this.shipmentTypeFormService.resetForm(this.editForm, shipmentType);
  }
}
