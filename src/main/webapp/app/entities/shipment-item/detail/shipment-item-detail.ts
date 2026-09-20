import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IShipmentItem } from '../shipment-item.model';

@Component({
  selector: 'sys-shipment-item-detail',
  templateUrl: './shipment-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ShipmentItemDetail {
  readonly shipmentItem = input<IShipmentItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
