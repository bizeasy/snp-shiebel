import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderShipment } from '../order-shipment.model';

@Component({
  selector: 'sys-order-shipment-detail',
  templateUrl: './order-shipment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderShipmentDetail {
  readonly orderShipment = input<IOrderShipment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
