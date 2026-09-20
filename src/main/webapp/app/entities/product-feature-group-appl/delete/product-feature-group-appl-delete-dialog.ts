import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureGroupAppl } from '../product-feature-group-appl.model';
import { ProductFeatureGroupApplService } from '../service/product-feature-group-appl.service';

@Component({
  templateUrl: './product-feature-group-appl-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class ProductFeatureGroupApplDeleteDialog {
  productFeatureGroupAppl?: IProductFeatureGroupAppl;

  protected readonly productFeatureGroupApplService = inject(ProductFeatureGroupApplService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productFeatureGroupApplService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
