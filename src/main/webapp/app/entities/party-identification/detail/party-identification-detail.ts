import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyIdentification } from '../party-identification.model';

@Component({
  selector: 'sys-party-identification-detail',
  templateUrl: './party-identification-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartyIdentificationDetail {
  readonly partyIdentification = input<IPartyIdentification | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
