import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityParty } from '../facility-party.model';

@Component({
  selector: 'sys-facility-party-detail',
  templateUrl: './facility-party-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class FacilityPartyDetail {
  readonly facilityParty = input<IFacilityParty | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
