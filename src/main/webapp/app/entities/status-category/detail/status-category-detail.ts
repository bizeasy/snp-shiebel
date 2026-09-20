import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IStatusCategory } from '../status-category.model';

@Component({
  selector: 'sys-status-category-detail',
  templateUrl: './status-category-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class StatusCategoryDetail {
  readonly statusCategory = input<IStatusCategory | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
