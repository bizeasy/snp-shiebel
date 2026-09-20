import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductCategoryType } from '../product-category-type.model';

@Component({
  selector: 'sys-product-category-type-detail',
  templateUrl: './product-category-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductCategoryTypeDetail {
  readonly productCategoryType = input<IProductCategoryType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
