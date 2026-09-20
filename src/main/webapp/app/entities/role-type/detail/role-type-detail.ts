import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IRoleType } from '../role-type.model';

@Component({
  selector: 'sys-role-type-detail',
  templateUrl: './role-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class RoleTypeDetail {
  readonly roleType = input<IRoleType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
