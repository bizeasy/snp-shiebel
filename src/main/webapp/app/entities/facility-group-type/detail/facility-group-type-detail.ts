import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityGroupType } from '../facility-group-type.model';

@Component({
  selector: 'sys-facility-group-type-detail',
  templateUrl: './facility-group-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FacilityGroupTypeDetail {
  readonly facilityGroupType = input<IFacilityGroupType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
