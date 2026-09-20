import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderItemType } from '../order-item-type.model';

@Component({
  selector: 'sys-order-item-type-detail',
  templateUrl: './order-item-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderItemTypeDetail {
  readonly orderItemType = input<IOrderItemType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
