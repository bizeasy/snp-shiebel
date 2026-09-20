import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceType } from '../invoice-type.model';

@Component({
  selector: 'sys-invoice-type-detail',
  templateUrl: './invoice-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class InvoiceTypeDetail {
  readonly invoiceType = input<IInvoiceType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
