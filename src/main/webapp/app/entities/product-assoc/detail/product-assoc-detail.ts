import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IProductAssoc } from '../product-assoc.model';

@Component({
  selector: 'sys-product-assoc-detail',
  templateUrl: './product-assoc-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ProductAssocDetail {
  readonly productAssoc = input<IProductAssoc | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
