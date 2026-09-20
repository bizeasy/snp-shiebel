import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICatalogueCategory } from '../catalogue-category.model';

@Component({
  selector: 'sys-catalogue-category-detail',
  templateUrl: './catalogue-category-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CatalogueCategoryDetail {
  readonly catalogueCategory = input<ICatalogueCategory | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
