import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPickList } from '../pick-list.model';

@Component({
  selector: 'sys-pick-list-detail',
  templateUrl: './pick-list-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class PickListDetail {
  readonly pickList = input<IPickList | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
