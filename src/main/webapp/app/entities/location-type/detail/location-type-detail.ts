import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ILocationType } from '../location-type.model';

@Component({
  selector: 'sys-location-type-detail',
  templateUrl: './location-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class LocationTypeDetail {
  readonly locationType = input<ILocationType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
