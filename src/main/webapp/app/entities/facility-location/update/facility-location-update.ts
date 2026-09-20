import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { ILocationType } from 'app/entities/location-type/location-type.model';
import { LocationTypeService } from 'app/entities/location-type/service/location-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFacilityLocation } from '../facility-location.model';
import { FacilityLocationService } from '../service/facility-location.service';

import { FacilityLocationFormGroup, FacilityLocationFormService } from './facility-location-form.service';

@Component({
  selector: 'sys-facility-location-update',
  templateUrl: './facility-location-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class FacilityLocationUpdate implements OnInit {
  readonly isSaving = signal(false);
  facilityLocation: IFacilityLocation | null = null;

  facilitiesSharedCollection = signal<IFacility[]>([]);
  locationTypesSharedCollection = signal<ILocationType[]>([]);

  protected facilityLocationService = inject(FacilityLocationService);
  protected facilityLocationFormService = inject(FacilityLocationFormService);
  protected facilityService = inject(FacilityService);
  protected locationTypeService = inject(LocationTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacilityLocationFormGroup = this.facilityLocationFormService.createFacilityLocationFormGroup();

  compareFacility = (o1: IFacility | null, o2: IFacility | null): boolean => this.facilityService.compareFacility(o1, o2);

  compareLocationType = (o1: ILocationType | null, o2: ILocationType | null): boolean =>
    this.locationTypeService.compareLocationType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facilityLocation }) => {
      this.facilityLocation = facilityLocation;
      if (facilityLocation) {
        this.updateForm(facilityLocation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const facilityLocation = this.facilityLocationFormService.getFacilityLocation(this.editForm);
    if (facilityLocation.id === null) {
      this.subscribeToSaveResponse(this.facilityLocationService.create(facilityLocation));
    } else {
      this.subscribeToSaveResponse(this.facilityLocationService.update(facilityLocation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IFacilityLocation | null>): void {
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

  protected updateForm(facilityLocation: IFacilityLocation): void {
    this.facilityLocation = facilityLocation;
    this.facilityLocationFormService.resetForm(this.editForm, facilityLocation);

    this.facilitiesSharedCollection.update(facilities =>
      this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, facilityLocation.facility),
    );
    this.locationTypesSharedCollection.update(locationTypes =>
      this.locationTypeService.addLocationTypeToCollectionIfMissing<ILocationType>(locationTypes, facilityLocation.locationType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facilityService
      .query()
      .pipe(map((res: HttpResponse<IFacility[]>) => res.body ?? []))
      .pipe(
        map((facilities: IFacility[]) =>
          this.facilityService.addFacilityToCollectionIfMissing<IFacility>(facilities, this.facilityLocation?.facility),
        ),
      )
      .subscribe((facilities: IFacility[]) => this.facilitiesSharedCollection.set(facilities));

    this.locationTypeService
      .query()
      .pipe(map((res: HttpResponse<ILocationType[]>) => res.body ?? []))
      .pipe(
        map((locationTypes: ILocationType[]) =>
          this.locationTypeService.addLocationTypeToCollectionIfMissing<ILocationType>(locationTypes, this.facilityLocation?.locationType),
        ),
      )
      .subscribe((locationTypes: ILocationType[]) => this.locationTypesSharedCollection.set(locationTypes));
  }
}
