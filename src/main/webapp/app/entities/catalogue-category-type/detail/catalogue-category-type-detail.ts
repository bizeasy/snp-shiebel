import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICatalogueCategoryType } from '../catalogue-category-type.model';

@Component({
  selector: 'sys-catalogue-category-type-detail',
  templateUrl: './catalogue-category-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CatalogueCategoryTypeDetail {
  readonly catalogueCategoryType = input<ICatalogueCategoryType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
