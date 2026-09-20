import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IFacilityGroup } from 'app/entities/facility-group/facility-group.model';
import { FacilityGroupService } from 'app/entities/facility-group/service/facility-group.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityGroupMember } from '../facility-group-member.model';
import { FacilityGroupMemberService } from '../service/facility-group-member.service';

import { FacilityGroupMemberFormGroup, FacilityGroupMemberFormService } from './facility-group-member-form.service';

@Component({
  selector: 'sys-facility-group-member-update',
  templateUrl: './facility-group-member-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityGroupMemberUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityGroupMember: IFacilityGroupMember | null = null;

  facilitiesSharedCollection = signal<IFacility[]>([]);
  facilityGroupsSharedCollection = signal<IFacilityGroup[]>([]);

  protected facilityGroupMemberService = inject(FacilityGroupMemberService);
  protected facilityGroupMemberFormService = inject(FacilityGroupMemberFormService);
  protected facilityService = inject(FacilityService);
  protected facilityGroupService = inject(FacilityGroupService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityGroupMemberFormGroup = this.facilityGroupMemberFormService.createFacilityGroupMemberFormGroup();

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareFacilityGroup = (o1: IFacilityGroup | null, o2: IFacilityGroup | null): boolean =>
    this.facilityGroupService.compareFacilityGroup(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityGroupMember }) => {
      this.facilityGroupMember = facilityGroupMember;
      if (facilityGroupMember) {
        this.updateForm(facilityGroupMember);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityGroupMember = this.facilityGroupMemberFormService.getFacilityGroupMember(this.editForm);
    if (facilityGroupMember.id === null) {
      this.subscribeToSaveResponse(this.facilityGroupMemberService.create(facilityGroupMember));
    } else {
      this.subscribeToSaveResponse(this.facilityGroupMemberService.update(facilityGroupMember));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityGroupMember | null>): void {
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

  protected updateForm(facilityGroupMember: IFacilityGroupMember): void {
    this.facilityGroupMember = facilityGroupMember;
    this.facilityGroupMemberFormService.resetForm(this.editForm, facilityGroupMember);

    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, facilityGroupMember.facility),
    );
    this.facilityGroupsSharedCollection.update(facilityGroups =>
      this.facilityGroupService.addFacilityGroupToCollectionIfMissing<IFacilityGroup>(facilityGroups, facilityGroupMember.facilityGroup),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.facilityGroupMember?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.facilityGroupService
      .query()
      .pipe(map((res: HttpResponse<IFacilityGroup[]>) => res.body ?? []))
      .pipe(
        map((facilityGroups: IFacilityGroup[]) =>
          this.facilityGroupService.addFacilityGroupToCollectionIfMissing<IFacilityGroup>(
            facilityGroups,
            this.facilityGroupMember?.facilityGroup,
          ),
        ),
      )
      .subscribe((facilityGroups: IFacilityGroup[]) => this.facilityGroupsSharedCollection.set(facilityGroups));
  }
}
