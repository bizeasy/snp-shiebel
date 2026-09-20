import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { IPaymentGatewayResponse } from 'app/entities/payment-gateway-response/payment-gateway-response.model';
import { PaymentGatewayResponseService } from 'app/entities/payment-gateway-response/service/payment-gateway-response.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { PaymentMethodTypeService } from 'app/entities/payment-method-type/service/payment-method-type.service';
import { IPaymentType } from 'app/entities/payment-type/payment-type.model';
import { PaymentTypeService } from 'app/entities/payment-type/service/payment-type.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';

import { PaymentFormGroup, PaymentFormService } from './payment-form.service';

@Component({
  selector: 'sys-payment-update',
  templateUrl: './payment-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PaymentUpdate implements OnInit {
  readonly isSaving = signal(false);
  payment: IPayment | null = null;

  paymentTypesSharedCollection = signal<IPaymentType[]>([]);
  paymentMethodTypesSharedCollection = signal<IPaymentMethodType[]>([]);
  paymentMethodsSharedCollection = signal<IPaymentMethod[]>([]);
  paymentGatewayResponsesSharedCollection = signal<IPaymentGatewayResponse[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);

  protected paymentService = inject(PaymentService);
  protected paymentFormService = inject(PaymentFormService);
  protected paymentTypeService = inject(PaymentTypeService);
  protected paymentMethodTypeService = inject(PaymentMethodTypeService);
  protected paymentMethodService = inject(PaymentMethodService);
  protected paymentGatewayResponseService = inject(PaymentGatewayResponseService);
  protected statusService = inject(StatusService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentFormGroup = this.paymentFormService.createPaymentFormGroup();

  comparePaymentType = (o1: IPaymentType | null, o2: IPaymentType | null): boolean => this.paymentTypeService.comparePaymentType(o1, o2);

  comparePaymentMethodType = (o1: IPaymentMethodType | null, o2: IPaymentMethodType | null): boolean =>
    this.paymentMethodTypeService.comparePaymentMethodType(o1, o2);

  comparePaymentMethod = (o1: IPaymentMethod | null, o2: IPaymentMethod | null): boolean =>
    this.paymentMethodService.comparePaymentMethod(o1, o2);

  comparePaymentGatewayResponse = (o1: IPaymentGatewayResponse | null, o2: IPaymentGatewayResponse | null): boolean =>
    this.paymentGatewayResponseService.comparePaymentGatewayResponse(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.payment = payment;
      if (payment) {
        this.updateForm(payment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const payment = this.paymentFormService.getPayment(this.editForm);
    if (payment.id === null) {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPayment | null>): void {
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

  protected updateForm(payment: IPayment): void {
    this.payment = payment;
    this.paymentFormService.resetForm(this.editForm, payment);

    this.paymentTypesSharedCollection.update(paymentTypes =>
      this.paymentTypeService.addPaymentTypeToCollectionIfMissing<IPaymentType>(paymentTypes, payment.paymentType),
    );
    this.paymentMethodTypesSharedCollection.update(paymentMethodTypes =>
      this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
        paymentMethodTypes,
        payment.paymentMethodType,
      ),
    );
    this.paymentMethodsSharedCollection.update(paymentMethods =>
      this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(paymentMethods, payment.paymentMethod),
    );
    this.paymentGatewayResponsesSharedCollection.update(paymentGatewayResponses =>
      this.paymentGatewayResponseService.addPaymentGatewayResponseToCollectionIfMissing<IPaymentGatewayResponse>(
        paymentGatewayResponses,
        payment.paymentGatewayResponse,
      ),
    );
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, payment.status));
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, payment.fromParty, payment.toParty),
    );
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, payment.roleType),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, payment.currencyUom));
  }

  protected loadRelationshipsOptions(): void {
    this.paymentTypeService
      .query()
      .pipe(map((res: HttpResponse<IPaymentType[]>) => res.body ?? []))
      .pipe(
        map((paymentTypes: IPaymentType[]) =>
          this.paymentTypeService.addPaymentTypeToCollectionIfMissing<IPaymentType>(paymentTypes, this.payment?.paymentType),
        ),
      )
      .subscribe((paymentTypes: IPaymentType[]) => this.paymentTypesSharedCollection.set(paymentTypes));

    this.paymentMethodTypeService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethodType[]>) => res.body ?? []))
      .pipe(
        map((paymentMethodTypes: IPaymentMethodType[]) =>
          this.paymentMethodTypeService.addPaymentMethodTypeToCollectionIfMissing<IPaymentMethodType>(
            paymentMethodTypes,
            this.payment?.paymentMethodType,
          ),
        ),
      )
      .subscribe((paymentMethodTypes: IPaymentMethodType[]) => this.paymentMethodTypesSharedCollection.set(paymentMethodTypes));

    this.paymentMethodService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethod[]>) => res.body ?? []))
      .pipe(
        map((paymentMethods: IPaymentMethod[]) =>
          this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(paymentMethods, this.payment?.paymentMethod),
        ),
      )
      .subscribe((paymentMethods: IPaymentMethod[]) => this.paymentMethodsSharedCollection.set(paymentMethods));

    this.paymentGatewayResponseService
      .query()
      .pipe(map((res: HttpResponse<IPaymentGatewayResponse[]>) => res.body ?? []))
      .pipe(
        map((paymentGatewayResponses: IPaymentGatewayResponse[]) =>
          this.paymentGatewayResponseService.addPaymentGatewayResponseToCollectionIfMissing<IPaymentGatewayResponse>(
            paymentGatewayResponses,
            this.payment?.paymentGatewayResponse,
          ),
        ),
      )
      .subscribe((paymentGatewayResponses: IPaymentGatewayResponse[]) =>
        this.paymentGatewayResponsesSharedCollection.set(paymentGatewayResponses),
      );

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.payment?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.payment?.fromParty, this.payment?.toParty),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.payment?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.payment?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
