import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyClassificationGroup } from '../party-classification-group.model';

@Component({
  selector: 'sys-party-classification-group-detail',
  templateUrl: './party-classification-group-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartyClassificationGroupDetail {
  readonly partyClassificationGroup = input<IPartyClassificationGroup | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
