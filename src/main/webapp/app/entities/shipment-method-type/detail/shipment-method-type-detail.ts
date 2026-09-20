import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IShipmentMethodType } from '../shipment-method-type.model';

@Component({
  selector: 'sys-shipment-method-type-detail',
  templateUrl: './shipment-method-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ShipmentMethodTypeDetail {
  readonly shipmentMethodType = input<IShipmentMethodType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
