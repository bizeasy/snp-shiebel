import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityGroupMember } from '../facility-group-member.model';

@Component({
  selector: 'sys-facility-group-member-detail',
  templateUrl: './facility-group-member-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class FacilityGroupMemberDetail {
  readonly facilityGroupMember = input<IFacilityGroupMember | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
