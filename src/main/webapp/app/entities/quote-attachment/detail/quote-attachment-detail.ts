import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteAttachment } from '../quote-attachment.model';

@Component({
  selector: 'sys-quote-attachment-detail',
  templateUrl: './quote-attachment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class QuoteAttachmentDetail {
  readonly quoteAttachment = input<IQuoteAttachment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
