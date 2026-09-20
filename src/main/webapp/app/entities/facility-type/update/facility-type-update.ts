import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityType } from '../facility-type.model';
import { FacilityTypeService } from '../service/facility-type.service';

import { FacilityTypeFormGroup, FacilityTypeFormService } from './facility-type-form.service';

@Component({
  selector: 'sys-facility-type-update',
  templateUrl: './facility-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityType: IFacilityType | null = null;

  protected facilityTypeService = inject(FacilityTypeService);
  protected facilityTypeFormService = inject(FacilityTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityTypeFormGroup = this.facilityTypeFormService.createFacilityTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityType }) => {
      this.facilityType = facilityType;
      if (facilityType) {
        this.updateForm(facilityType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityType = this.facilityTypeFormService.getFacilityType(this.editForm);
    if (facilityType.id === null) {
      this.subscribeToSaveResponse(this.facilityTypeService.create(facilityType));
    } else {
      this.subscribeToSaveResponse(this.facilityTypeService.update(facilityType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityType | null>): void {
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

  protected updateForm(facilityType: IFacilityType): void {
    this.facilityType = facilityType;
    this.facilityTypeFormService.resetForm(this.editForm, facilityType);
  }
}
