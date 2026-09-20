import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeoPoint } from '../geo-point.model';
import { GeoPointService } from '../service/geo-point.service';

import { GeoPointFormGroup, GeoPointFormService } from './geo-point-form.service';

@Component({
  selector: 'sys-geo-point-update',
  templateUrl: './geo-point-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class GeoPointUpdate implements OnInit {
  readonly isSaving = signal(false);
  geoPoint: IGeoPoint | null = null;

  protected geoPointService = inject(GeoPointService);
  protected geoPointFormService = inject(GeoPointFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GeoPointFormGroup = this.geoPointFormService.createGeoPointFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ geoPoint }) => {
      this.geoPoint = geoPoint;
      if (geoPoint) {
        this.updateForm(geoPoint);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const geoPoint = this.geoPointFormService.getGeoPoint(this.editForm);
    if (geoPoint.id === null) {
      this.subscribeToSaveResponse(this.geoPointService.create(geoPoint));
    } else {
      this.subscribeToSaveResponse(this.geoPointService.update(geoPoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IGeoPoint | null>): void {
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

  protected updateForm(geoPoint: IGeoPoint): void {
    this.geoPoint = geoPoint;
    this.geoPointFormService.resetForm(this.editForm, geoPoint);
  }
}
