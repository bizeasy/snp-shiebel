import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacilityGroupType } from 'app/entities/facility-group-type/facility-group-type.model';
import { FacilityGroupTypeService } from 'app/entities/facility-group-type/service/facility-group-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityGroup } from '../facility-group.model';
import { FacilityGroupService } from '../service/facility-group.service';

import { FacilityGroupFormGroup, FacilityGroupFormService } from './facility-group-form.service';

@Component({
  selector: 'sys-facility-group-update',
  templateUrl: './facility-group-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityGroupUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityGroup: IFacilityGroup | null = null;

  facilityGroupTypesSharedCollection = signal<IFacilityGroupType[]>([]);
  facilityGroupsSharedCollection = signal<IFacilityGroup[]>([]);

  protected facilityGroupService = inject(FacilityGroupService);
  protected facilityGroupFormService = inject(FacilityGroupFormService);
  protected facilityGroupTypeService = inject(FacilityGroupTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityGroupFormGroup = this.facilityGroupFormService.createFacilityGroupFormGroup();

  compareFacilityGroupType = (o1: IFacilityGroupType | null, o2: IFacilityGroupType | null): boolean =>
    this.facilityGroupTypeService.compareFacilityGroupType(o1, o2);

  compareFacilityGroup = (o1: IFacilityGroup | null, o2: IFacilityGroup | null): boolean =>
    this.facilityGroupService.compareFacilityGroup(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityGroup }) => {
      this.facilityGroup = facilityGroup;
      if (facilityGroup) {
        this.updateForm(facilityGroup);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityGroup = this.facilityGroupFormService.getFacilityGroup(this.editForm);
    if (facilityGroup.id === null) {
      this.subscribeToSaveResponse(this.facilityGroupService.create(facilityGroup));
    } else {
      this.subscribeToSaveResponse(this.facilityGroupService.update(facilityGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityGroup | null>): void {
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

  protected updateForm(facilityGroup: IFacilityGroup): void {
    this.facilityGroup = facilityGroup;
    this.facilityGroupFormService.resetForm(this.editForm, facilityGroup);

    this.facilityGroupTypesSharedCollection.update(facilityGroupTypes =>
      this.facilityGroupTypeService.addFacilityGroupTypeToCollectionIfMissing<IFacilityGroupType>(
        facilityGroupTypes,
        facilityGroup.facilityGroupType,
      ),
    );
    this.facilityGroupsSharedCollection.update(facilityGroups =>
      this.facilityGroupService.addFacilityGroupToCollectionIfMissing<IFacilityGroup>(facilityGroups, facilityGroup.parentFacilityGroup),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facilityGroupTypeService
      .query()
      .pipe(map((res: HttpResponse<IFacilityGroupType[]>) => res.body ?? []))
      .pipe(
        map((facilityGroupTypes: IFacilityGroupType[]) =>
          this.facilityGroupTypeService.addFacilityGroupTypeToCollectionIfMissing<IFacilityGroupType>(
            facilityGroupTypes,
            this.facilityGroup?.facilityGroupType,
          ),
        ),
      )
      .subscribe((facilityGroupTypes: IFacilityGroupType[]) => this.facilityGroupTypesSharedCollection.set(facilityGroupTypes));

    this.facilityGroupService
      .query()
      .pipe(map((res: HttpResponse<IFacilityGroup[]>) => res.body ?? []))
      .pipe(
        map((facilityGroups: IFacilityGroup[]) =>
          this.facilityGroupService.addFacilityGroupToCollectionIfMissing<IFacilityGroup>(
            facilityGroups,
            this.facilityGroup?.parentFacilityGroup,
          ),
        ),
      )
      .subscribe((facilityGroups: IFacilityGroup[]) => this.facilityGroupsSharedCollection.set(facilityGroups));
  }
}
