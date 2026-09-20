import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICarrierShipmentMethod } from '../carrier-shipment-method.model';

@Component({
  selector: 'sys-carrier-shipment-method-detail',
  templateUrl: './carrier-shipment-method-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CarrierShipmentMethodDetail {
  readonly carrierShipmentMethod = input<ICarrierShipmentMethod | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
