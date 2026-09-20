import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ILot } from '../lot.model';

@Component({
  selector: 'sys-lot-detail',
  templateUrl: './lot-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class LotDetail {
  readonly lot = input<ILot | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
