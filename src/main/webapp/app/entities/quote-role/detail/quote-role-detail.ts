import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteRole } from '../quote-role.model';

@Component({
  selector: 'sys-quote-role-detail',
  templateUrl: './quote-role-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class QuoteRoleDetail {
  readonly quoteRole = input<IQuoteRole | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
