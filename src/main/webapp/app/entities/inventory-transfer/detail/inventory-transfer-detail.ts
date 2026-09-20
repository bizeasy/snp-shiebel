import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryTransfer } from '../inventory-transfer.model';

@Component({
  selector: 'sys-inventory-transfer-detail',
  templateUrl: './inventory-transfer-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class InventoryTransferDetail {
  readonly inventoryTransfer = input<IInventoryTransfer | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
