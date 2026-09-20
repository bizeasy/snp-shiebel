import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ISystemParameters } from '../system-parameters.model';

@Component({
  selector: 'sys-system-parameters-detail',
  templateUrl: './system-parameters-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class SystemParametersDetail {
  readonly systemParameters = input<ISystemParameters | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
