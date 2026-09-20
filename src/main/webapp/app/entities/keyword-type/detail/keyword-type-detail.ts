import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IKeywordType } from '../keyword-type.model';

@Component({
  selector: 'sys-keyword-type-detail',
  templateUrl: './keyword-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class KeywordTypeDetail {
  readonly keywordType = input<IKeywordType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
