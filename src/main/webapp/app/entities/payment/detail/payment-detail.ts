import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPayment } from '../payment.model';

@Component({
  selector: 'sys-payment-detail',
  templateUrl: './payment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class PaymentDetail {
  readonly payment = input<IPayment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
