import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItem } from '../inventory-item.model';

@Component({
  selector: 'sys-inventory-item-detail',
  templateUrl: './inventory-item-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class InventoryItemDetail {
  readonly inventoryItem = input<IInventoryItem | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
