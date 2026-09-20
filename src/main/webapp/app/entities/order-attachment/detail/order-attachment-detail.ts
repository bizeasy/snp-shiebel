import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderAttachment } from '../order-attachment.model';

@Component({
  selector: 'sys-order-attachment-detail',
  templateUrl: './order-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderAttachmentDetail {
  readonly orderAttachment = input<IOrderAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
