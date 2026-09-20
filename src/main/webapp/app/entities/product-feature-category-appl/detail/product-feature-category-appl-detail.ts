import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureCategoryAppl } from '../product-feature-category-appl.model';

@Component({
  selector: 'sys-product-feature-category-appl-detail',
  templateUrl: './product-feature-category-appl-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ProductFeatureCategoryApplDetail {
  readonly productFeatureCategoryAppl = input<IProductFeatureCategoryAppl | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
