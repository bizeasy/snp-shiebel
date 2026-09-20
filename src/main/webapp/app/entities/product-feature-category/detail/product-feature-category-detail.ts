import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureCategory } from '../product-feature-category.model';

@Component({
  selector: 'sys-product-feature-category-detail',
  templateUrl: './product-feature-category-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductFeatureCategoryDetail {
  readonly productFeatureCategory = input<IProductFeatureCategory | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
