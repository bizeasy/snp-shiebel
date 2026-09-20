import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPickListItem } from '../pick-list-item.model';

@Component({
  selector: 'sys-pick-list-item-detail',
  templateUrl: './pick-list-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PickListItemDetail {
  readonly pickListItem = input<IPickListItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
