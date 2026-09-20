import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPicklistRole } from '../picklist-role.model';

@Component({
  selector: 'sys-picklist-role-detail',
  templateUrl: './picklist-role-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PicklistRoleDetail {
  readonly picklistRole = input<IPicklistRole | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
