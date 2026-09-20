import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentGatewayResponse } from '../payment-gateway-response.model';
import { PaymentGatewayResponseService } from '../service/payment-gateway-response.service';

@Component({
  templateUrl: './payment-gateway-response-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class PaymentGatewayResponseDeleteDialog {
  paymentGatewayResponse?: IPaymentGatewayResponse;

  protected readonly paymentGatewayResponseService = inject(PaymentGatewayResponseService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentGatewayResponseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
