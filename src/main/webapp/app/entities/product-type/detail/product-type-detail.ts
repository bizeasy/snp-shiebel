import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductType } from '../product-type.model';

@Component({
  selector: 'sys-product-type-detail',
  templateUrl: './product-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductTypeDetail {
  readonly productType = input<IProductType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
