import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ITransactionType } from '../transaction-type.model';

@Component({
  selector: 'sys-transaction-type-detail',
  templateUrl: './transaction-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class TransactionTypeDetail {
  readonly transactionType = input<ITransactionType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
