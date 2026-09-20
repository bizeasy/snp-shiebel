import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureType } from '../product-feature-type.model';

@Component({
  selector: 'sys-product-feature-type-detail',
  templateUrl: './product-feature-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductFeatureTypeDetail {
  readonly productFeatureType = input<IProductFeatureType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
