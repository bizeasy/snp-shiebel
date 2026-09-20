import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContactMech } from '../contact-mech.model';

@Component({
  selector: 'sys-contact-mech-detail',
  templateUrl: './contact-mech-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ContactMechDetail {
  readonly contactMech = input<IContactMech | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
