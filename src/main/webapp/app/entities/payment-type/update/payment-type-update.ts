import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentType } from '../payment-type.model';
import { PaymentTypeService } from '../service/payment-type.service';

import { PaymentTypeFormGroup, PaymentTypeFormService } from './payment-type-form.service';

@Component({
  selector: 'sys-payment-type-update',
  templateUrl: './payment-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PaymentTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  paymentType: IPaymentType | null = null;

  protected paymentTypeService = inject(PaymentTypeService);
  protected paymentTypeFormService = inject(PaymentTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentTypeFormGroup = this.paymentTypeFormService.createPaymentTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentType }) => {
      this.paymentType = paymentType;
      if (paymentType) {
        this.updateForm(paymentType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const paymentType = this.paymentTypeFormService.getPaymentType(this.editForm);
    if (paymentType.id === null) {
      this.subscribeToSaveResponse(this.paymentTypeService.create(paymentType));
    } else {
      this.subscribeToSaveResponse(this.paymentTypeService.update(paymentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPaymentType | null>): void {
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

  protected updateForm(paymentType: IPaymentType): void {
    this.paymentType = paymentType;
    this.paymentTypeFormService.resetForm(this.editForm, paymentType);
  }
}
