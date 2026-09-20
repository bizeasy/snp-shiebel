import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityGroupType } from '../facility-group-type.model';
import { FacilityGroupTypeService } from '../service/facility-group-type.service';

import { FacilityGroupTypeFormGroup, FacilityGroupTypeFormService } from './facility-group-type-form.service';

@Component({
  selector: 'sys-facility-group-type-update',
  templateUrl: './facility-group-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityGroupTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityGroupType: IFacilityGroupType | null = null;

  protected facilityGroupTypeService = inject(FacilityGroupTypeService);
  protected facilityGroupTypeFormService = inject(FacilityGroupTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityGroupTypeFormGroup = this.facilityGroupTypeFormService.createFacilityGroupTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityGroupType }) => {
      this.facilityGroupType = facilityGroupType;
      if (facilityGroupType) {
        this.updateForm(facilityGroupType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityGroupType = this.facilityGroupTypeFormService.getFacilityGroupType(this.editForm);
    if (facilityGroupType.id === null) {
      this.subscribeToSaveResponse(this.facilityGroupTypeService.create(facilityGroupType));
    } else {
      this.subscribeToSaveResponse(this.facilityGroupTypeService.update(facilityGroupType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityGroupType | null>): void {
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

  protected updateForm(facilityGroupType: IFacilityGroupType): void {
    this.facilityGroupType = facilityGroupType;
    this.facilityGroupTypeFormService.resetForm(this.editForm, facilityGroupType);
  }
}
