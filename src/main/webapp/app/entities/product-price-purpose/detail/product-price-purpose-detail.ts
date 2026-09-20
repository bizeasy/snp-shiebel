import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductPricePurpose } from '../product-price-purpose.model';

@Component({
  selector: 'sys-product-price-purpose-detail',
  templateUrl: './product-price-purpose-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductPricePurposeDetail {
  readonly productPricePurpose = input<IProductPricePurpose | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
