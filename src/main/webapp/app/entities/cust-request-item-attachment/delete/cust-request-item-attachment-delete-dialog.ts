import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestItemAttachment } from '../cust-request-item-attachment.model';
import { CustRequestItemAttachmentService } from '../service/cust-request-item-attachment.service';

@Component({
  templateUrl: './cust-request-item-attachment-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class CustRequestItemAttachmentDeleteDialog {
  custRequestItemAttachment?: ICustRequestItemAttachment;

  protected readonly custRequestItemAttachmentService = inject(CustRequestItemAttachmentService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.custRequestItemAttachmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
