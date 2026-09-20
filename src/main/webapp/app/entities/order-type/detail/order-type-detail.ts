import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderType } from '../order-type.model';

@Component({
  selector: 'sys-order-type-detail',
  templateUrl: './order-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderTypeDetail {
  readonly orderType = input<IOrderType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
