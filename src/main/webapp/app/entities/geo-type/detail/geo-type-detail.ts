import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeoType } from '../geo-type.model';

@Component({
  selector: 'sys-geo-type-detail',
  templateUrl: './geo-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class GeoTypeDetail {
  readonly geoType = input<IGeoType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
