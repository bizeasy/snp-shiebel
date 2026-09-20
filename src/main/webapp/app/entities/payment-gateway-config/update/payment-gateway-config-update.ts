import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentGatewayConfig } from '../payment-gateway-config.model';
import { PaymentGatewayConfigService } from '../service/payment-gateway-config.service';

import { PaymentGatewayConfigFormGroup, PaymentGatewayConfigFormService } from './payment-gateway-config-form.service';

@Component({
  selector: 'sys-payment-gateway-config-update',
  templateUrl: './payment-gateway-config-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PaymentGatewayConfigUpdate implements OnInit {
  readonly isSaving = signal(false);
  paymentGatewayConfig: IPaymentGatewayConfig | null = null;

  protected paymentGatewayConfigService = inject(PaymentGatewayConfigService);
  protected paymentGatewayConfigFormService = inject(PaymentGatewayConfigFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentGatewayConfigFormGroup = this.paymentGatewayConfigFormService.createPaymentGatewayConfigFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentGatewayConfig }) => {
      this.paymentGatewayConfig = paymentGatewayConfig;
      if (paymentGatewayConfig) {
        this.updateForm(paymentGatewayConfig);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const paymentGatewayConfig = this.paymentGatewayConfigFormService.getPaymentGatewayConfig(this.editForm);
    if (paymentGatewayConfig.id === null) {
      this.subscribeToSaveResponse(this.paymentGatewayConfigService.create(paymentGatewayConfig));
    } else {
      this.subscribeToSaveResponse(this.paymentGatewayConfigService.update(paymentGatewayConfig));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPaymentGatewayConfig | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(paymentGatewayConfig: IPaymentGatewayConfig): void {
    this.paymentGatewayConfig = paymentGatewayConfig;
    this.paymentGatewayConfigFormService.resetForm(this.editForm, paymentGatewayConfig);
  }
}
