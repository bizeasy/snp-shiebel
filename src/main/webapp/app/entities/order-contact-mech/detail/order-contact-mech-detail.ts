import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IOrderContactMech } from '../order-contact-mech.model';

@Component({
  selector: 'sys-order-contact-mech-detail',
  templateUrl: './order-contact-mech-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class OrderContactMechDetail {
  readonly orderContactMech = input<IOrderContactMech | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
