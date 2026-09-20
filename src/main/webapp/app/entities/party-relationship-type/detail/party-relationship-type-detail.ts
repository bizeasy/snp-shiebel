import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyRelationshipType } from '../party-relationship-type.model';

@Component({
  selector: 'sys-party-relationship-type-detail',
  templateUrl: './party-relationship-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartyRelationshipTypeDetail {
  readonly partyRelationshipType = input<IPartyRelationshipType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
