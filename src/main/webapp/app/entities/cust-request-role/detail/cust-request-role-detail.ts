import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestRole } from '../cust-request-role.model';

@Component({
  selector: 'sys-cust-request-role-detail',
  templateUrl: './cust-request-role-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CustRequestRoleDetail {
  readonly custRequestRole = input<ICustRequestRole | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
