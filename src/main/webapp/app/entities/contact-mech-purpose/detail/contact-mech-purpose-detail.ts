import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContactMechPurpose } from '../contact-mech-purpose.model';

@Component({
  selector: 'sys-contact-mech-purpose-detail',
  templateUrl: './contact-mech-purpose-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ContactMechPurposeDetail {
  readonly contactMechPurpose = input<IContactMechPurpose | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
