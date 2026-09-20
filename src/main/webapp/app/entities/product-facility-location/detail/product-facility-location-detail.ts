import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFacilityLocation } from '../product-facility-location.model';

@Component({
  selector: 'sys-product-facility-location-detail',
  templateUrl: './product-facility-location-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductFacilityLocationDetail {
  readonly productFacilityLocation = input<IProductFacilityLocation | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
