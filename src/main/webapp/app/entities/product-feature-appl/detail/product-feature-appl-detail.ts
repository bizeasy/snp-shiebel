import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureAppl } from '../product-feature-appl.model';

@Component({
  selector: 'sys-product-feature-appl-detail',
  templateUrl: './product-feature-appl-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ProductFeatureApplDetail {
  readonly productFeatureAppl = input<IProductFeatureAppl | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
