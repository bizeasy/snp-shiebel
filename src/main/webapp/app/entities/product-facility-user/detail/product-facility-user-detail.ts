import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IProductFacilityUser } from '../product-facility-user.model';

@Component({
  selector: 'sys-product-facility-user-detail',
  templateUrl: './product-facility-user-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ProductFacilityUserDetail {
  readonly productFacilityUser = input<IProductFacilityUser | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
