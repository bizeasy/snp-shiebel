import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICarrierShipmentMethod } from '../carrier-shipment-method.model';
import { CarrierShipmentMethodService } from '../service/carrier-shipment-method.service';

@Component({
  templateUrl: './carrier-shipment-method-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class CarrierShipmentMethodDeleteDialog {
  carrierShipmentMethod?: ICarrierShipmentMethod;

  protected readonly carrierShipmentMethodService = inject(CarrierShipmentMethodService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.carrierShipmentMethodService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
