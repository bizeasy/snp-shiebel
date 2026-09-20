import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityLocation } from '../facility-location.model';

@Component({
  selector: 'sys-facility-location-detail',
  templateUrl: './facility-location-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FacilityLocationDetail {
  readonly facilityLocation = input<IFacilityLocation | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
