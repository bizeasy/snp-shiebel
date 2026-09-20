import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICustomTimePeriod } from '../custom-time-period.model';

@Component({
  selector: 'sys-custom-time-period-detail',
  templateUrl: './custom-time-period-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class CustomTimePeriodDetail {
  readonly customTimePeriod = input<ICustomTimePeriod | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
