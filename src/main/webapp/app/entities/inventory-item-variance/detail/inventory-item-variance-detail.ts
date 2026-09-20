import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItemVariance } from '../inventory-item-variance.model';

@Component({
  selector: 'sys-inventory-item-variance-detail',
  templateUrl: './inventory-item-variance-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class InventoryItemVarianceDetail {
  readonly inventoryItemVariance = input<IInventoryItemVariance | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
