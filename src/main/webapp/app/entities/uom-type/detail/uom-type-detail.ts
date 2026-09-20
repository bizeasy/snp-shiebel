import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IUomType } from '../uom-type.model';

@Component({
  selector: 'sys-uom-type-detail',
  templateUrl: './uom-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class UomTypeDetail {
  readonly uomType = input<IUomType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
