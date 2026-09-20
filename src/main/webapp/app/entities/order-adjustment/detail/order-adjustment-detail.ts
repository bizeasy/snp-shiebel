import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IOrderAdjustment } from '../order-adjustment.model';

@Component({
  selector: 'sys-order-adjustment-detail',
  templateUrl: './order-adjustment-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class OrderAdjustmentDetail {
  readonly orderAdjustment = input<IOrderAdjustment | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
