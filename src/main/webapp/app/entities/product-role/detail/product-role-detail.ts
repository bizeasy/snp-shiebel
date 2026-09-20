import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductRole } from '../product-role.model';

@Component({
  selector: 'sys-product-role-detail',
  templateUrl: './product-role-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ProductRoleDetail {
  readonly productRole = input<IProductRole | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
