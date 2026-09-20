import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IReturnHeaderType } from 'app/entities/return-header-type/return-header-type.model';
import { ReturnHeaderTypeService } from 'app/entities/return-header-type/service/return-header-type.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IReturnHeader } from '../return-header.model';
import { ReturnHeaderService } from '../service/return-header.service';

import { ReturnHeaderFormGroup, ReturnHeaderFormService } from './return-header-form.service';

@Component({
  selector: 'sys-return-header-update',
  templateUrl: './return-header-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ReturnHeaderUpdate implements OnInit {
  readonly isSaving = signal(false);
  returnHeader: IReturnHeader | null = null;

  returnHeaderTypesSharedCollection = signal<IReturnHeaderType[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  paymentMethodsSharedCollection = signal<IPaymentMethod[]>([]);
  contactMechesSharedCollection = signal<IContactMech[]>([]);
  facilitiesSharedCollection = signal<IFacility[]>([]);
  uomsSharedCollection = signal<IUom[]>([]);

  protected returnHeaderService = inject(ReturnHeaderService);
  protected returnHeaderFormService = inject(ReturnHeaderFormService);
  protected returnHeaderTypeService = inject(ReturnHeaderTypeService);
  protected statusService = inject(StatusService);
  protected partyService = inject(PartyService);
  protected paymentMethodService = inject(PaymentMethodService);
  protected contactMechService = inject(ContactMechService);
  protected facilityService = inject(FacilityService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReturnHeaderFormGroup = this.returnHeaderFormService.createReturnHeaderFormGroup();

  compareReturnHeaderType = (o1: IReturnHeaderType | null, o2: IReturnHeaderType | null): boolean =>
    this.returnHeaderTypeService.compareReturnHeaderType(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  comparePaymentMethod = (o1: IPaymentMethod | null, o2: IPaymentMethod | null): boolean =>
    this.paymentMethodService.comparePaymentMethod(o1, o2);

  compareContactMech = (o1: IContactMech | null, o2: IContactMech | null): boolean => this.contactMechService.compareContactMech(o1, o2);

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnHeader }) => {
      this.returnHeader = returnHeader;
      if (returnHeader) {
        this.updateForm(returnHeader);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const returnHeader = this.returnHeaderFormService.getReturnHeader(this.editForm);
    if (returnHeader.id === null) {
      this.subscribeToSaveResponse(this.returnHeaderService.create(returnHeader));
    } else {
      this.subscribeToSaveResponse(this.returnHeaderService.update(returnHeader));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IReturnHeader | null>): void {
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

  protected updateForm(returnHeader: IReturnHeader): void {
    this.returnHeader = returnHeader;
    this.returnHeaderFormService.resetForm(this.editForm, returnHeader);

    this.returnHeaderTypesSharedCollection.update(returnHeaderTypes =>
      this.returnHeaderTypeService.addReturnHeaderTypeToCollectionIfMissing<IReturnHeaderType>(returnHeaderTypes, returnHeader.returnType),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, returnHeader.status),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, returnHeader.fromParty, returnHeader.toParty),
    );
    this.paymentMethodsSharedCollection.update(paymentMethods =>
      this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(paymentMethods, returnHeader.paymentMethod),
    );
    this.contactMechesSharedCollection.update(contactMeches =>
      this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, returnHeader.originContactMech),
    );
    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, returnHeader.destinationFacility),
    );
    this.uomsSharedCollection.update(uoms => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, returnHeader.currencyUom));
  }

  protected loadRelationshipsOptions(): void {
    this.returnHeaderTypeService
      .query()
      .pipe(map((res: HttpResponse<IReturnHeaderType[]>) => res.body ?? []))
      .pipe(
        map((returnHeaderTypes: IReturnHeaderType[]) =>
          this.returnHeaderTypeService.addReturnHeaderTypeToCollectionIfMissing<IReturnHeaderType>(
            returnHeaderTypes,
            this.returnHeader?.returnType,
          ),
        ),
      )
      .subscribe((returnHeaderTypes: IReturnHeaderType[]) => this.returnHeaderTypesSharedCollection.set(returnHeaderTypes));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.returnHeader?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.returnHeader?.fromParty, this.returnHeader?.toParty),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.paymentMethodService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethod[]>) => res.body ?? []))
      .pipe(
        map((paymentMethods: IPaymentMethod[]) =>
          this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(paymentMethods, this.returnHeader?.paymentMethod),
        ),
      )
      .subscribe((paymentMethods: IPaymentMethod[]) => this.paymentMethodsSharedCollection.set(paymentMethods));

    this.contactMechService
      .query()
      .pipe(map((res: HttpResponse<IContactMech[]>) => res.body ?? []))
      .pipe(
        map((contactMeches: IContactMech[]) =>
          this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, this.returnHeader?.originContactMech),
        ),
      )
      .subscribe((contactMeches: IContactMech[]) => this.contactMechesSharedCollection.set(contactMeches));

    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.returnHeader?.destinationFacility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(map((uoms: IUom[]) => this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.returnHeader?.currencyUom)))
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
