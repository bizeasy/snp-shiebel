import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductPriceType } from '../product-price-type.model';

@Component({
  selector: 'sys-product-price-type-detail',
  templateUrl: './product-price-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductPriceTypeDetail {
  readonly productPriceType = input<IProductPriceType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
