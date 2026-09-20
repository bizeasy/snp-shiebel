import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IShipment } from '../shipment.model';

@Component({
  selector: 'sys-shipment-detail',
  templateUrl: './shipment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ShipmentDetail {
  readonly shipment = input<IShipment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
