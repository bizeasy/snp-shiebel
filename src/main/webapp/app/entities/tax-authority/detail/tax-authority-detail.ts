import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ITaxAuthority } from '../tax-authority.model';

@Component({
  selector: 'sys-tax-authority-detail',
  templateUrl: './tax-authority-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class TaxAuthorityDetail {
  readonly taxAuthority = input<ITaxAuthority | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
