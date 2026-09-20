import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItemDelegate } from '../inventory-item-delegate.model';
import { InventoryItemDelegateService } from '../service/inventory-item-delegate.service';

@Component({
  templateUrl: './inventory-item-delegate-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class InventoryItemDelegateDeleteDialog {
  inventoryItemDelegate?: IInventoryItemDelegate;

  protected readonly inventoryItemDelegateService = inject(InventoryItemDelegateService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inventoryItemDelegateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
