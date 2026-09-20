import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItemType } from '../inventory-item-type.model';

@Component({
  selector: 'sys-inventory-item-type-detail',
  templateUrl: './inventory-item-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class InventoryItemTypeDetail {
  readonly inventoryItemType = input<IInventoryItemType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
