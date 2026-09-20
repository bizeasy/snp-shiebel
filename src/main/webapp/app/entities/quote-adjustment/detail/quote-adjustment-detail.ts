import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteAdjustment } from '../quote-adjustment.model';

@Component({
  selector: 'sys-quote-adjustment-detail',
  templateUrl: './quote-adjustment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class QuoteAdjustmentDetail {
  readonly quoteAdjustment = input<IQuoteAdjustment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
