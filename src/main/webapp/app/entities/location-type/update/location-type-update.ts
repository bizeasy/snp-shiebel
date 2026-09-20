import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ILocationType } from '../location-type.model';
import { LocationTypeService } from '../service/location-type.service';

import { LocationTypeFormGroup, LocationTypeFormService } from './location-type-form.service';

@Component({
  selector: 'sys-location-type-update',
  templateUrl: './location-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class LocationTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  locationType: ILocationType | null = null;

  protected locationTypeService = inject(LocationTypeService);
  protected locationTypeFormService = inject(LocationTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LocationTypeFormGroup = this.locationTypeFormService.createLocationTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationType }) => {
      this.locationType = locationType;
      if (locationType) {
        this.updateForm(locationType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const locationType = this.locationTypeFormService.getLocationType(this.editForm);
    if (locationType.id === null) {
      this.subscribeToSaveResponse(this.locationTypeService.create(locationType));
    } else {
      this.subscribeToSaveResponse(this.locationTypeService.update(locationType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ILocationType | null>): void {
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

  protected updateForm(locationType: ILocationType): void {
    this.locationType = locationType;
    this.locationTypeFormService.resetForm(this.editForm, locationType);
  }
}
