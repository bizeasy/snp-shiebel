import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentType } from '../payment-type.model';

@Component({
  selector: 'sys-payment-type-detail',
  templateUrl: './payment-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PaymentTypeDetail {
  readonly paymentType = input<IPaymentType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
