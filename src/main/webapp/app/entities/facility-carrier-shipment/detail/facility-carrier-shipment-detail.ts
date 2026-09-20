import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityCarrierShipment } from '../facility-carrier-shipment.model';

@Component({
  selector: 'sys-facility-carrier-shipment-detail',
  templateUrl: './facility-carrier-shipment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FacilityCarrierShipmentDetail {
  readonly facilityCarrierShipment = input<IFacilityCarrierShipment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
