import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeo } from '../geo.model';

@Component({
  selector: 'sys-geo-detail',
  templateUrl: './geo-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class GeoDetail {
  readonly geo = input<IGeo | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
