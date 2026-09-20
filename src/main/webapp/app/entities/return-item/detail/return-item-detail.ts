import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IReturnItem } from '../return-item.model';

@Component({
  selector: 'sys-return-item-detail',
  templateUrl: './return-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ReturnItemDetail {
  readonly returnItem = input<IReturnItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
