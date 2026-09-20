import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IShoppingListType } from '../shopping-list-type.model';

@Component({
  selector: 'sys-shopping-list-type-detail',
  templateUrl: './shopping-list-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ShoppingListTypeDetail {
  readonly shoppingListType = input<IShoppingListType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
