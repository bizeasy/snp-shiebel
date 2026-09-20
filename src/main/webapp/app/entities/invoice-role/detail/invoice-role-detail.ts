import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceRole } from '../invoice-role.model';

@Component({
  selector: 'sys-invoice-role-detail',
  templateUrl: './invoice-role-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class InvoiceRoleDetail {
  readonly invoiceRole = input<IInvoiceRole | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
