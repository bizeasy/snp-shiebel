import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteItem } from '../quote-item.model';

@Component({
  selector: 'sys-quote-item-detail',
  templateUrl: './quote-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class QuoteItemDetail {
  readonly quoteItem = input<IQuoteItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
