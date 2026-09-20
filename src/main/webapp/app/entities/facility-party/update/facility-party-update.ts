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
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityParty } from '../facility-party.model';
import { FacilityPartyService } from '../service/facility-party.service';

import { FacilityPartyFormGroup, FacilityPartyFormService } from './facility-party-form.service';

@Component({
  selector: 'sys-facility-party-update',
  templateUrl: './facility-party-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityPartyUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityParty: IFacilityParty | null = null;

  facilitiesSharedCollection = signal<IFacility[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected facilityPartyService = inject(FacilityPartyService);
  protected facilityPartyFormService = inject(FacilityPartyFormService);
  protected facilityService = inject(FacilityService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityPartyFormGroup = this.facilityPartyFormService.createFacilityPartyFormGroup();

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityParty }) => {
      this.facilityParty = facilityParty;
      if (facilityParty) {
        this.updateForm(facilityParty);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityParty = this.facilityPartyFormService.getFacilityParty(this.editForm);
    if (facilityParty.id === null) {
      this.subscribeToSaveResponse(this.facilityPartyService.create(facilityParty));
    } else {
      this.subscribeToSaveResponse(this.facilityPartyService.update(facilityParty));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityParty | null>): void {
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

  protected updateForm(facilityParty: IFacilityParty): void {
    this.facilityParty = facilityParty;
    this.facilityPartyFormService.resetForm(this.editForm, facilityParty);

    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, facilityParty.facility),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, facilityParty.party));
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, facilityParty.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.facilityParty?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.facilityParty?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.facilityParty?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
