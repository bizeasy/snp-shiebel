import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestItem } from '../cust-request-item.model';

@Component({
  selector: 'sys-cust-request-item-detail',
  templateUrl: './cust-request-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class CustRequestItemDetail {
  readonly custRequestItem = input<ICustRequestItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
