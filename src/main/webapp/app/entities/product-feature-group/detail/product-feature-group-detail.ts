import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureGroup } from '../product-feature-group.model';

@Component({
  selector: 'sys-product-feature-group-detail',
  templateUrl: './product-feature-group-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductFeatureGroupDetail {
  readonly productFeatureGroup = input<IProductFeatureGroup | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
