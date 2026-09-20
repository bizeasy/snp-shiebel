import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentMethodType } from '../payment-method-type.model';
import { PaymentMethodTypeService } from '../service/payment-method-type.service';

import { PaymentMethodTypeFormGroup, PaymentMethodTypeFormService } from './payment-method-type-form.service';

@Component({
  selector: 'sys-payment-method-type-update',
  templateUrl: './payment-method-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PaymentMethodTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  paymentMethodType: IPaymentMethodType | null = null;

  protected paymentMethodTypeService = inject(PaymentMethodTypeService);
  protected paymentMethodTypeFormService = inject(PaymentMethodTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentMethodTypeFormGroup = this.paymentMethodTypeFormService.createPaymentMethodTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentMethodType }) => {
      this.paymentMethodType = paymentMethodType;
      if (paymentMethodType) {
        this.updateForm(paymentMethodType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const paymentMethodType = this.paymentMethodTypeFormService.getPaymentMethodType(this.editForm);
    if (paymentMethodType.id === null) {
      this.subscribeToSaveResponse(this.paymentMethodTypeService.create(paymentMethodType));
    } else {
      this.subscribeToSaveResponse(this.paymentMethodTypeService.update(paymentMethodType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPaymentMethodType | null>): void {
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

  protected updateForm(paymentMethodType: IPaymentMethodType): void {
    this.paymentMethodType = paymentMethodType;
    this.paymentMethodTypeFormService.resetForm(this.editForm, paymentMethodType);
  }
}
