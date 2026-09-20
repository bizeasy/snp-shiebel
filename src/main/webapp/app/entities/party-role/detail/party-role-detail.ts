import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyRole } from '../party-role.model';

@Component({
  selector: 'sys-party-role-detail',
  templateUrl: './party-role-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PartyRoleDetail {
  readonly partyRole = input<IPartyRole | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
