import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeoAssocType } from '../geo-assoc-type.model';

@Component({
  selector: 'sys-geo-assoc-type-detail',
  templateUrl: './geo-assoc-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class GeoAssocTypeDetail {
  readonly geoAssocType = input<IGeoAssocType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
