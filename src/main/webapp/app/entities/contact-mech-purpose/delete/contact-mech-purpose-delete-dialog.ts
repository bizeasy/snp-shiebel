import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IContactMechPurpose } from '../contact-mech-purpose.model';
import { ContactMechPurposeService } from '../service/contact-mech-purpose.service';

@Component({
  templateUrl: './contact-mech-purpose-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class ContactMechPurposeDeleteDialog {
  contactMechPurpose?: IContactMechPurpose;

  protected readonly contactMechPurposeService = inject(ContactMechPurposeService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactMechPurposeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
