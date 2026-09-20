import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IStatus } from '../status.model';

@Component({
  selector: 'sys-status-detail',
  templateUrl: './status-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class StatusDetail {
  readonly status = input<IStatus | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
