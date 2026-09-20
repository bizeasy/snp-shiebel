import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestType } from '../cust-request-type.model';
import { CustRequestTypeService } from '../service/cust-request-type.service';

import { CustRequestTypeFormGroup, CustRequestTypeFormService } from './cust-request-type-form.service';

@Component({
  selector: 'sys-cust-request-type-update',
  templateUrl: './cust-request-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustRequestTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  custRequestType: ICustRequestType | null = null;

  custRequestTypesSharedCollection = signal<ICustRequestType[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected custRequestTypeService = inject(CustRequestTypeService);
  protected custRequestTypeFormService = inject(CustRequestTypeFormService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustRequestTypeFormGroup = this.custRequestTypeFormService.createCustRequestTypeFormGroup();

  compareCustRequestType = (o1: ICustRequestType | null, o2: ICustRequestType | null): boolean =>
    this.custRequestTypeService.compareCustRequestType(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ custRequestType }) => {
      this.custRequestType = custRequestType;
      if (custRequestType) {
        this.updateForm(custRequestType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const custRequestType = this.custRequestTypeFormService.getCustRequestType(this.editForm);
    if (custRequestType.id === null) {
      this.subscribeToSaveResponse(this.custRequestTypeService.create(custRequestType));
    } else {
      this.subscribeToSaveResponse(this.custRequestTypeService.update(custRequestType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustRequestType | null>): void {
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

  protected updateForm(custRequestType: ICustRequestType): void {
    this.custRequestType = custRequestType;
    this.custRequestTypeFormService.resetForm(this.editForm, custRequestType);

    this.custRequestTypesSharedCollection.update(custRequestTypes =>
      this.custRequestTypeService.addCustRequestTypeToCollectionIfMissing<ICustRequestType>(custRequestTypes, custRequestType.parent),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, custRequestType.party));
  }

  protected loadRelationshipsOptions(): void {
    this.custRequestTypeService
      .query()
      .pipe(map((res: HttpResponse<ICustRequestType[]>) => res.body ?? []))
      .pipe(
        map((custRequestTypes: ICustRequestType[]) =>
          this.custRequestTypeService.addCustRequestTypeToCollectionIfMissing<ICustRequestType>(
            custRequestTypes,
            this.custRequestType?.parent,
          ),
        ),
      )
      .subscribe((custRequestTypes: ICustRequestType[]) => this.custRequestTypesSharedCollection.set(custRequestTypes));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.custRequestType?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
