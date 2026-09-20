import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ITaxAuthorityRateType } from '../tax-authority-rate-type.model';

@Component({
  selector: 'sys-tax-authority-rate-type-detail',
  templateUrl: './tax-authority-rate-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class TaxAuthorityRateTypeDetail {
  readonly taxAuthorityRateType = input<ITaxAuthorityRateType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
