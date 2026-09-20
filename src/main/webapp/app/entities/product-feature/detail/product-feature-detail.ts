import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeature } from '../product-feature.model';

@Component({
  selector: 'sys-product-feature-detail',
  templateUrl: './product-feature-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductFeatureDetail {
  readonly productFeature = input<IProductFeature | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
