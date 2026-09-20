import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IShipmentType } from '../shipment-type.model';

@Component({
  selector: 'sys-shipment-type-detail',
  templateUrl: './shipment-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ShipmentTypeDetail {
  readonly shipmentType = input<IShipmentType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
