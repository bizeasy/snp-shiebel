import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPickList } from '../pick-list.model';
import { PickListService } from '../service/pick-list.service';

import { PickListFormGroup, PickListFormService } from './pick-list-form.service';

@Component({
  selector: 'sys-pick-list-update',
  templateUrl: './pick-list-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PickListUpdate implements OnInit {
  readonly isSaving = signal(false);
  pickList: IPickList | null = null;

  facilitiesSharedCollection = signal<IFacility[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);

  protected pickListService = inject(PickListService);
  protected pickListFormService = inject(PickListFormService);
  protected facilityService = inject(FacilityService);
  protected statusService = inject(StatusService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PickListFormGroup = this.pickListFormService.createPickListFormGroup();

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pickList }) => {
      this.pickList = pickList;
      if (pickList) {
        this.updateForm(pickList);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const pickList = this.pickListFormService.getPickList(this.editForm);
    if (pickList.id === null) {
      this.subscribeToSaveResponse(this.pickListService.create(pickList));
    } else {
      this.subscribeToSaveResponse(this.pickListService.update(pickList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPickList | null>): void {
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

  protected updateForm(pickList: IPickList): void {
    this.pickList = pickList;
    this.pickListFormService.resetForm(this.editForm, pickList);

    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, pickList.facility),
    );
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, pickList.status));
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, pickList.party));
  }

  protected loadRelationshipsOptions(): void {
    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.pickList?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.pickList?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.pickList?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
