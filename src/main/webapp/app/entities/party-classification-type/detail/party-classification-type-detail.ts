import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyClassificationType } from '../party-classification-type.model';

@Component({
  selector: 'sys-party-classification-type-detail',
  templateUrl: './party-classification-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartyClassificationTypeDetail {
  readonly partyClassificationType = input<IPartyClassificationType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
