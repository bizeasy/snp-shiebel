import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICatalogueCategoryType } from '../catalogue-category-type.model';
import { CatalogueCategoryTypeService } from '../service/catalogue-category-type.service';

@Component({
  templateUrl: './catalogue-category-type-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class CatalogueCategoryTypeDeleteDialog {
  catalogueCategoryType?: ICatalogueCategoryType;

  protected readonly catalogueCategoryTypeService = inject(CatalogueCategoryTypeService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogueCategoryTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
