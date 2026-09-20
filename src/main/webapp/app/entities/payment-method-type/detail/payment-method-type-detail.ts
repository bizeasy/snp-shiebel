import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentMethodType } from '../payment-method-type.model';

@Component({
  selector: 'sys-payment-method-type-detail',
  templateUrl: './payment-method-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PaymentMethodTypeDetail {
  readonly paymentMethodType = input<IPaymentMethodType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
