import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';

import { PaymentMethodFormGroup, PaymentMethodFormService } from './payment-method-form.service';

@Component({
  selector: 'sys-payment-method-update',
  templateUrl: './payment-method-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PaymentMethodUpdate implements OnInit {
  readonly isSaving = signal(false);
  paymentMethod: IPaymentMethod | null = null;

  paymentMethodTypesSharedCollection = signal<IPaymentMethodType[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected paymentMethodService = inject(PaymentMethodService);
  protected paymentMethodFormService = inject(PaymentMethodFormService);
  protected paymentMethodTypeService = inject(PaymentMethodTypeService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentMethodFormGroup = this.paymentMethodFormService.createPaymentMethodFormGroup();

  comparePaymentMethodType = (o1: IPaymentMethodType | null, o2: IPaymentMethodType | null): boolean =>
    this.paymentMethodTypeService.comparePaymentMethodType(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentMethod }) => {
      this.paymentMethod = paymentMethod;
      if (paymentMethod) {
        this.updateForm(paymentMethod);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const paymentMethod = this.paymentMethodFormService.getPaymentMethod(this.editForm);
    if (paymentMethod.id === null) {
      this.subscribeToSaveResponse(this.paymentMethodService.create(paymentMethod));
    } else {
      this.subscribeToSaveResponse(this.paymentMethodService.update(paymentMethod));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPaymentMethod | null>): void {
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

  protected updateForm(paymentMethod: IPaymentMethod): void {
    this.paymentMethod = paymentMethod;
    this.paymentMethodFormService.resetForm(this.editForm, paymentMethod);

    this.paymentMethodTypesSharedCollection.update(paymentMethodTypes =>
      this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
        paymentMethodTypes,
        paymentMethod.paymentMethodType,
      ),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, paymentMethod.party));
  }

  protected loadRelationshipsOptions(): void {
    this.paymentMethodTypeService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethodType[]>) => res.body ?? []))
      .pipe(
        map((paymentMethodTypes: IPaymentMethodType[]) =>
          this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
            paymentMethodTypes,
            this.paymentMethod?.paymentMethodType,
          ),
        ),
      )
      .subscribe((paymentMethodTypes: IPaymentMethodType[]) => this.paymentMethodTypesSharedCollection.set(paymentMethodTypes));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.paymentMethod?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
