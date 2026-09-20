import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductAssocType } from '../product-assoc-type.model';

@Component({
  selector: 'sys-product-assoc-type-detail',
  templateUrl: './product-assoc-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductAssocTypeDetail {
  readonly productAssocType = input<IProductAssocType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
