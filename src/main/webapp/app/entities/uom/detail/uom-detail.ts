import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IUom } from '../uom.model';

@Component({
  selector: 'sys-uom-detail',
  templateUrl: './uom-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class UomDetail {
  readonly uom = input<IUom | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
