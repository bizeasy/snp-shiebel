import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICashBook } from '../cash-book.model';

@Component({
  selector: 'sys-cash-book-detail',
  templateUrl: './cash-book-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class CashBookDetail {
  readonly cashBook = input<ICashBook | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
