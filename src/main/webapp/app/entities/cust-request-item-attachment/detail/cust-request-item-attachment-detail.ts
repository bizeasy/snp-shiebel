import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestItemAttachment } from '../cust-request-item-attachment.model';

@Component({
  selector: 'sys-cust-request-item-attachment-detail',
  templateUrl: './cust-request-item-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CustRequestItemAttachmentDetail {
  readonly custRequestItemAttachment = input<ICustRequestItemAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
