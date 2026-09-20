import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICashBookAttachment } from '../cash-book-attachment.model';

@Component({
  selector: 'sys-cash-book-attachment-detail',
  templateUrl: './cash-book-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CashBookAttachmentDetail {
  readonly cashBookAttachment = input<ICashBookAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
