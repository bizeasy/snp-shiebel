import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequest } from '../cust-request.model';

@Component({
  selector: 'sys-cust-request-detail',
  templateUrl: './cust-request-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class CustRequestDetail {
  readonly custRequest = input<ICustRequest | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
