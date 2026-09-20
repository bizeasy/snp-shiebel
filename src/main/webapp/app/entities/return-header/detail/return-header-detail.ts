import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IReturnHeader } from '../return-header.model';

@Component({
  selector: 'sys-return-header-detail',
  templateUrl: './return-header-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ReturnHeaderDetail {
  readonly returnHeader = input<IReturnHeader | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
