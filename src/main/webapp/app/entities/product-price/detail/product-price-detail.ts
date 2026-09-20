import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IProductPrice } from '../product-price.model';

@Component({
  selector: 'sys-product-price-detail',
  templateUrl: './product-price-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ProductPriceDetail {
  readonly productPrice = input<IProductPrice | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
