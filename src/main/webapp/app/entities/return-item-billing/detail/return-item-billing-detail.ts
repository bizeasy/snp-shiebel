import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IReturnItemBilling } from '../return-item-billing.model';

@Component({
  selector: 'sys-return-item-billing-detail',
  templateUrl: './return-item-billing-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ReturnItemBillingDetail {
  readonly returnItemBilling = input<IReturnItemBilling | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
