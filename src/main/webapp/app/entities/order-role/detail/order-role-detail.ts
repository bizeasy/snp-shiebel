import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOrderRole } from '../order-role.model';

@Component({
  selector: 'sys-order-role-detail',
  templateUrl: './order-role-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OrderRoleDetail {
  readonly orderRole = input<IOrderRole | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
