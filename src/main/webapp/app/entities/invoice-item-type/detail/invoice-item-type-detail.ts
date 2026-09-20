import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceItemType } from '../invoice-item-type.model';

@Component({
  selector: 'sys-invoice-item-type-detail',
  templateUrl: './invoice-item-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class InvoiceItemTypeDetail {
  readonly invoiceItemType = input<IInvoiceItemType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
