import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityGroup } from '../facility-group.model';

@Component({
  selector: 'sys-facility-group-detail',
  templateUrl: './facility-group-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FacilityGroupDetail {
  readonly facilityGroup = input<IFacilityGroup | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
