import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ITransactionCategory } from '../transaction-category.model';

@Component({
  selector: 'sys-transaction-category-detail',
  templateUrl: './transaction-category-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class TransactionCategoryDetail {
  readonly transactionCategory = input<ITransactionCategory | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
