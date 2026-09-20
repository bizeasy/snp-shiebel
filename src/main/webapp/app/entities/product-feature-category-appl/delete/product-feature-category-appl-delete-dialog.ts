import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFeatureCategoryAppl } from '../product-feature-category-appl.model';
import { ProductFeatureCategoryApplService } from '../service/product-feature-category-appl.service';

@Component({
  templateUrl: './product-feature-category-appl-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class ProductFeatureCategoryApplDeleteDialog {
  productFeatureCategoryAppl?: IProductFeatureCategoryAppl;

  protected readonly productFeatureCategoryApplService = inject(ProductFeatureCategoryApplService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productFeatureCategoryApplService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
