import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IQuote } from '../quote.model';

@Component({
  selector: 'sys-quote-detail',
  templateUrl: './quote-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class QuoteDetail {
  readonly quote = input<IQuote | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
