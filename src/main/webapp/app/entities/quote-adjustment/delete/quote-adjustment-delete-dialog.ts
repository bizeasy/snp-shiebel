import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteAdjustment } from '../quote-adjustment.model';
import { QuoteAdjustmentService } from '../service/quote-adjustment.service';

@Component({
  templateUrl: './quote-adjustment-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class QuoteAdjustmentDeleteDialog {
  quoteAdjustment?: IQuoteAdjustment;

  protected readonly quoteAdjustmentService = inject(QuoteAdjustmentService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quoteAdjustmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
