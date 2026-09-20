import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IShipmentAttachment } from '../shipment-attachment.model';

@Component({
  selector: 'sys-shipment-attachment-detail',
  templateUrl: './shipment-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ShipmentAttachmentDetail {
  readonly shipmentAttachment = input<IShipmentAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
