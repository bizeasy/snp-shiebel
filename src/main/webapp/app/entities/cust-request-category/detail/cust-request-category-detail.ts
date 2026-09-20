import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestCategory } from '../cust-request-category.model';

@Component({
  selector: 'sys-cust-request-category-detail',
  templateUrl: './cust-request-category-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CustRequestCategoryDetail {
  readonly custRequestCategory = input<ICustRequestCategory | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
