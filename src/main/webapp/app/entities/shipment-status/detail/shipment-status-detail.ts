import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IShipmentStatus } from '../shipment-status.model';

@Component({
  selector: 'sys-shipment-status-detail',
  templateUrl: './shipment-status-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ShipmentStatusDetail {
  readonly shipmentStatus = input<IShipmentStatus | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
