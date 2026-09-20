import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItem } from '../order-item.model';

@Component({
  selector: 'sys-order-item-detail',
  templateUrl: './order-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class OrderItemDetail {
  readonly orderItem = input<IOrderItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
