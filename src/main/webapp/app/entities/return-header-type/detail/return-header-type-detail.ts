import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IReturnHeaderType } from '../return-header-type.model';

@Component({
  selector: 'sys-return-header-type-detail',
  templateUrl: './return-header-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ReturnHeaderTypeDetail {
  readonly returnHeaderType = input<IReturnHeaderType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
