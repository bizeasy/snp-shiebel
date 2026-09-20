import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeoType } from '../geo-type.model';
import { GeoTypeService } from '../service/geo-type.service';

import { GeoTypeFormGroup, GeoTypeFormService } from './geo-type-form.service';

@Component({
  selector: 'sys-geo-type-update',
  templateUrl: './geo-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class GeoTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  geoType: IGeoType | null = null;

  protected geoTypeService = inject(GeoTypeService);
  protected geoTypeFormService = inject(GeoTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GeoTypeFormGroup = this.geoTypeFormService.createGeoTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ geoType }) => {
      this.geoType = geoType;
      if (geoType) {
        this.updateForm(geoType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const geoType = this.geoTypeFormService.getGeoType(this.editForm);
    if (geoType.id === null) {
      this.subscribeToSaveResponse(this.geoTypeService.create(geoType));
    } else {
      this.subscribeToSaveResponse(this.geoTypeService.update(geoType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IGeoType | null>): void {
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

  protected updateForm(geoType: IGeoType): void {
    this.geoType = geoType;
    this.geoTypeFormService.resetForm(this.editForm, geoType);
  }
}
