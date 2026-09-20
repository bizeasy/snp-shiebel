import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestAttachment } from '../cust-request-attachment.model';

@Component({
  selector: 'sys-cust-request-attachment-detail',
  templateUrl: './cust-request-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CustRequestAttachmentDetail {
  readonly custRequestAttachment = input<ICustRequestAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
