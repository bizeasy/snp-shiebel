import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContentType } from '../content-type.model';

@Component({
  selector: 'sys-content-type-detail',
  templateUrl: './content-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ContentTypeDetail {
  readonly contentType = input<IContentType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
