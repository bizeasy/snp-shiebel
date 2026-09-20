import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShipmentMethodTypeService } from '../service/shipment-method-type.service';
import { IShipmentMethodType } from '../shipment-method-type.model';

import { ShipmentMethodTypeFormGroup, ShipmentMethodTypeFormService } from './shipment-method-type-form.service';

@Component({
  selector: 'sys-shipment-method-type-update',
  templateUrl: './shipment-method-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShipmentMethodTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  shipmentMethodType: IShipmentMethodType | null = null;

  protected shipmentMethodTypeService = inject(ShipmentMethodTypeService);
  protected shipmentMethodTypeFormService = inject(ShipmentMethodTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShipmentMethodTypeFormGroup = this.shipmentMethodTypeFormService.createShipmentMethodTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentMethodType }) => {
      this.shipmentMethodType = shipmentMethodType;
      if (shipmentMethodType) {
        this.updateForm(shipmentMethodType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shipmentMethodType = this.shipmentMethodTypeFormService.getShipmentMethodType(this.editForm);
    if (shipmentMethodType.id === null) {
      this.subscribeToSaveResponse(this.shipmentMethodTypeService.create(shipmentMethodType));
    } else {
      this.subscribeToSaveResponse(this.shipmentMethodTypeService.update(shipmentMethodType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShipmentMethodType | null>): void {
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

  protected updateForm(shipmentMethodType: IShipmentMethodType): void {
    this.shipmentMethodType = shipmentMethodType;
    this.shipmentMethodTypeFormService.resetForm(this.editForm, shipmentMethodType);
  }
}
