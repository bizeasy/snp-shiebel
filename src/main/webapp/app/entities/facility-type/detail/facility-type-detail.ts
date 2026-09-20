import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityType } from '../facility-type.model';

@Component({
  selector: 'sys-facility-type-detail',
  templateUrl: './facility-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FacilityTypeDetail {
  readonly facilityType = input<IFacilityType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
