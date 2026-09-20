import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IOrderStatus } from '../order-status.model';

@Component({
  selector: 'sys-order-status-detail',
  templateUrl: './order-status-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class OrderStatusDetail {
  readonly orderStatus = input<IOrderStatus | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
