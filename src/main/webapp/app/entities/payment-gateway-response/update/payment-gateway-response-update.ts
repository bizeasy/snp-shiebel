import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IPaymentGatewayConfig } from 'app/entities/payment-gateway-config/payment-gateway-config.model';
import { PaymentGatewayConfigService } from 'app/entities/payment-gateway-config/service/payment-gateway-config.service';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentGatewayResponse } from '../payment-gateway-response.model';
import { PaymentGatewayResponseService } from '../service/payment-gateway-response.service';

import { PaymentGatewayResponseFormGroup, PaymentGatewayResponseFormService } from './payment-gateway-response-form.service';

@Component({
  selector: 'sys-payment-gateway-response-update',
  templateUrl: './payment-gateway-response-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PaymentGatewayResponseUpdate implements OnInit {
  readonly isSaving = signal(false);
  paymentGatewayResponse: IPaymentGatewayResponse | null = null;

  paymentGatewayConfigsSharedCollection = signal<IPaymentGatewayConfig[]>([]);
  paymentMethodTypesSharedCollection = signal<IPaymentMethodType[]>([]);

  protected paymentGatewayResponseService = inject(PaymentGatewayResponseService);
  protected paymentGatewayResponseFormService = inject(PaymentGatewayResponseFormService);
  protected paymentGatewayConfigService = inject(PaymentGatewayConfigService);
  protected paymentMethodTypeService = inject(PaymentMethodTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentGatewayResponseFormGroup = this.paymentGatewayResponseFormService.createPaymentGatewayResponseFormGroup();

  comparePaymentGatewayConfig = (o1: IPaymentGatewayConfig | null, o2: IPaymentGatewayConfig | null): boolean =>
    this.paymentGatewayConfigService.comparePaymentGatewayConfig(o1, o2);

  comparePaymentMethodType = (o1: IPaymentMethodType | null, o2: IPaymentMethodType | null): boolean =>
    this.paymentMethodTypeService.comparePaymentMethodType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentGatewayResponse }) => {
      this.paymentGatewayResponse = paymentGatewayResponse;
      if (paymentGatewayResponse) {
        this.updateForm(paymentGatewayResponse);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const paymentGatewayResponse = this.paymentGatewayResponseFormService.getPaymentGatewayResponse(this.editForm);
    if (paymentGatewayResponse.id === null) {
      this.subscribeToSaveResponse(this.paymentGatewayResponseService.create(paymentGatewayResponse));
    } else {
      this.subscribeToSaveResponse(this.paymentGatewayResponseService.update(paymentGatewayResponse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPaymentGatewayResponse | null>): void {
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

  protected updateForm(paymentGatewayResponse: IPaymentGatewayResponse): void {
    this.paymentGatewayResponse = paymentGatewayResponse;
    this.paymentGatewayResponseFormService.resetForm(this.editForm, paymentGatewayResponse);

    this.paymentGatewayConfigsSharedCollection.update(paymentGatewayConfigs =>
      this.paymentGatewayConfigService.addPaymentGatewayConfigToCollectionIfMissing<IPaymentGatewayConfig>(
        paymentGatewayConfigs,
        paymentGatewayResponse.paymentGatewayConfig,
      ),
    );
    this.paymentMethodTypesSharedCollection.update(paymentMethodTypes =>
      this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
        paymentMethodTypes,
        paymentGatewayResponse.paymentMethodType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentGatewayConfigService
      .query()
      .pipe(map((res: HttpResponse<IPaymentGatewayConfig[]>) => res.body ?? []))
      .pipe(
        map((paymentGatewayConfigs: IPaymentGatewayConfig[]) =>
          this.paymentGatewayConfigService.addPaymentGatewayConfigToCollectionIfMissing<IPaymentGatewayConfig>(
            paymentGatewayConfigs,
            this.paymentGatewayResponse?.paymentGatewayConfig,
          ),
        ),
      )
      .subscribe((paymentGatewayConfigs: IPaymentGatewayConfig[]) => this.paymentGatewayConfigsSharedCollection.set(paymentGatewayConfigs));

    this.paymentMethodTypeService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethodType[]>) => res.body ?? []))
      .pipe(
        map((paymentMethodTypes: IPaymentMethodType[]) =>
          this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
            paymentMethodTypes,
            this.paymentGatewayResponse?.paymentMethodType,
          ),
        ),
      )
      .subscribe((paymentMethodTypes: IPaymentMethodType[]) => this.paymentMethodTypesSharedCollection.set(paymentMethodTypes));
  }
}
