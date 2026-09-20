import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteType } from '../quote-type.model';

@Component({
  selector: 'sys-quote-type-detail',
  templateUrl: './quote-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class QuoteTypeDetail {
  readonly quoteType = input<IQuoteType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
