import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContactMechType } from '../contact-mech-type.model';

@Component({
  selector: 'sys-contact-mech-type-detail',
  templateUrl: './contact-mech-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ContactMechTypeDetail {
  readonly contactMechType = input<IContactMechType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
