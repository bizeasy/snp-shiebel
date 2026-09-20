import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentGatewayConfig } from '../payment-gateway-config.model';

@Component({
  selector: 'sys-payment-gateway-config-detail',
  templateUrl: './payment-gateway-config-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PaymentGatewayConfigDetail {
  readonly paymentGatewayConfig = input<IPaymentGatewayConfig | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
