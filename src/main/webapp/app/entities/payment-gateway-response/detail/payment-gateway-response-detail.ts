import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentGatewayResponse } from '../payment-gateway-response.model';

@Component({
  selector: 'sys-payment-gateway-response-detail',
  templateUrl: './payment-gateway-response-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PaymentGatewayResponseDetail {
  readonly paymentGatewayResponse = input<IPaymentGatewayResponse | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
