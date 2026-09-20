import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityContactMech } from '../facility-contact-mech.model';

@Component({
  selector: 'sys-facility-contact-mech-detail',
  templateUrl: './facility-contact-mech-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class FacilityContactMechDetail {
  readonly facilityContactMech = input<IFacilityContactMech | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
