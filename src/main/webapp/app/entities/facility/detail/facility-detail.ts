import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacility } from '../facility.model';

@Component({
  selector: 'sys-facility-detail',
  templateUrl: './facility-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FacilityDetail {
  readonly facility = input<IFacility | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
