import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentApplication } from '../payment-application.model';

@Component({
  selector: 'sys-payment-application-detail',
  templateUrl: './payment-application-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PaymentApplicationDetail {
  readonly paymentApplication = input<IPaymentApplication | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
