import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IStatusValidChange } from '../status-valid-change.model';

@Component({
  selector: 'sys-status-valid-change-detail',
  templateUrl: './status-valid-change-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class StatusValidChangeDetail {
  readonly statusValidChange = input<IStatusValidChange | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
