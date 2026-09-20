import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureApplType } from '../product-feature-appl-type.model';

@Component({
  selector: 'sys-product-feature-appl-type-detail',
  templateUrl: './product-feature-appl-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductFeatureApplTypeDetail {
  readonly productFeatureApplType = input<IProductFeatureApplType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
