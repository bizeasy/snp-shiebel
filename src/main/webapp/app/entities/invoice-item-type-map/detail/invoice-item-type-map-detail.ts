import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceItemTypeMap } from '../invoice-item-type-map.model';

@Component({
  selector: 'sys-invoice-item-type-map-detail',
  templateUrl: './invoice-item-type-map-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class InvoiceItemTypeMapDetail {
  readonly invoiceItemTypeMap = input<IInvoiceItemTypeMap | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
