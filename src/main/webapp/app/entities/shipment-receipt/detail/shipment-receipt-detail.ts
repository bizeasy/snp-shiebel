import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IShipmentReceipt } from '../shipment-receipt.model';

@Component({
  selector: 'sys-shipment-receipt-detail',
  templateUrl: './shipment-receipt-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ShipmentReceiptDetail {
  readonly shipmentReceipt = input<IShipmentReceipt | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
