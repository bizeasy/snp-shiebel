import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPartyContactMech } from '../party-contact-mech.model';

@Component({
  selector: 'sys-party-contact-mech-detail',
  templateUrl: './party-contact-mech-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class PartyContactMechDetail {
  readonly partyContactMech = input<IPartyContactMech | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
