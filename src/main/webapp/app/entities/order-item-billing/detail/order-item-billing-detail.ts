import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItemBilling } from '../order-item-billing.model';

@Component({
  selector: 'sys-order-item-billing-detail',
  templateUrl: './order-item-billing-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderItemBillingDetail {
  readonly orderItemBilling = input<IOrderItemBilling | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
