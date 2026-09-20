import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeoPoint } from '../geo-point.model';

@Component({
  selector: 'sys-geo-point-detail',
  templateUrl: './geo-point-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class GeoPointDetail {
  readonly geoPoint = input<IGeoPoint | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
