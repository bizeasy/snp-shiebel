import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderAdjustmentType } from '../order-adjustment-type.model';

@Component({
  selector: 'sys-order-adjustment-type-detail',
  templateUrl: './order-adjustment-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderAdjustmentTypeDetail {
  readonly orderAdjustmentType = input<IOrderAdjustmentType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
