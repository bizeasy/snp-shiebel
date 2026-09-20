import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeoAssocType } from '../geo-assoc-type.model';
import { GeoAssocTypeService } from '../service/geo-assoc-type.service';

import { GeoAssocTypeFormGroup, GeoAssocTypeFormService } from './geo-assoc-type-form.service';

@Component({
  selector: 'sys-geo-assoc-type-update',
  templateUrl: './geo-assoc-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class GeoAssocTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  geoAssocType: IGeoAssocType | null = null;

  protected geoAssocTypeService = inject(GeoAssocTypeService);
  protected geoAssocTypeFormService = inject(GeoAssocTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GeoAssocTypeFormGroup = this.geoAssocTypeFormService.createGeoAssocTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ geoAssocType }) => {
      this.geoAssocType = geoAssocType;
      if (geoAssocType) {
        this.updateForm(geoAssocType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const geoAssocType = this.geoAssocTypeFormService.getGeoAssocType(this.editForm);
    if (geoAssocType.id === null) {
      this.subscribeToSaveResponse(this.geoAssocTypeService.create(geoAssocType));
    } else {
      this.subscribeToSaveResponse(this.geoAssocTypeService.update(geoAssocType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IGeoAssocType | null>): void {
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

  protected updateForm(geoAssocType: IGeoAssocType): void {
    this.geoAssocType = geoAssocType;
    this.geoAssocTypeFormService.resetForm(this.editForm, geoAssocType);
  }
}
