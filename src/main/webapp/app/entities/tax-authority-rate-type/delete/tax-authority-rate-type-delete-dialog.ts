import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TaxAuthorityRateTypeService } from '../service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from '../tax-authority-rate-type.model';

@Component({
  templateUrl: './tax-authority-rate-type-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class TaxAuthorityRateTypeDeleteDialog {
  taxAuthorityRateType?: ITaxAuthorityRateType;

  protected readonly taxAuthorityRateTypeService = inject(TaxAuthorityRateTypeService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taxAuthorityRateTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
