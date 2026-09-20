import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityCarrierShipment } from '../facility-carrier-shipment.model';
import { FacilityCarrierShipmentService } from '../service/facility-carrier-shipment.service';

@Component({
  templateUrl: './facility-carrier-shipment-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class FacilityCarrierShipmentDeleteDialog {
  facilityCarrierShipment?: IFacilityCarrierShipment;

  protected readonly facilityCarrierShipmentService = inject(FacilityCarrierShipmentService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facilityCarrierShipmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
