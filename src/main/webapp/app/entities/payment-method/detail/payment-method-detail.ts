import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentMethod } from '../payment-method.model';

@Component({
  selector: 'sys-payment-method-detail',
  templateUrl: './payment-method-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class PaymentMethodDetail {
  readonly paymentMethod = input<IPaymentMethod | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
