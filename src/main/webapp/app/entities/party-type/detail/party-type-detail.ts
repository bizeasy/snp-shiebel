import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyType } from '../party-type.model';

@Component({
  selector: 'sys-party-type-detail',
  templateUrl: './party-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartyTypeDetail {
  readonly partyType = input<IPartyType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
