import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IShoppingListItem } from '../shopping-list-item.model';

@Component({
  selector: 'sys-shopping-list-item-detail',
  templateUrl: './shopping-list-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ShoppingListItemDetail {
  readonly shoppingListItem = input<IShoppingListItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
