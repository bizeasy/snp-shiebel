import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICatalogue } from '../catalogue.model';

@Component({
  selector: 'sys-catalogue-detail',
  templateUrl: './catalogue-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CatalogueDetail {
  readonly catalogue = input<ICatalogue | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
