import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPostalAddress } from '../postal-address.model';

@Component({
  selector: 'sys-postal-address-detail',
  templateUrl: './postal-address-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PostalAddressDetail {
  readonly postalAddress = input<IPostalAddress | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
