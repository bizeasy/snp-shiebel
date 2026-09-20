import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItemAttachment } from '../order-item-attachment.model';

@Component({
  selector: 'sys-order-item-attachment-detail',
  templateUrl: './order-item-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderItemAttachmentDetail {
  readonly orderItemAttachment = input<IOrderItemAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
