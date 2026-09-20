import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IGeoType } from 'app/entities/geo-type/geo-type.model';
import { GeoTypeService } from 'app/entities/geo-type/service/geo-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeo } from '../geo.model';
import { GeoService } from '../service/geo.service';

import { GeoFormGroup, GeoFormService } from './geo-form.service';

@Component({
  selector: 'sys-geo-update',
  templateUrl: './geo-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class GeoUpdate implements OnInit {
  readonly isSaving = signal(false);
  geo: IGeo | null = null;

  geoTypesSharedCollection = signal<IGeoType[]>([]);

  protected geoService = inject(GeoService);
  protected geoFormService = inject(GeoFormService);
  protected geoTypeService = inject(GeoTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GeoFormGroup = this.geoFormService.createGeoFormGroup();

  compareGeoType = (o1: IGeoType | null, o2: IGeoType | null): boolean => this.geoTypeService.compareGeoType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ geo }) => {
      this.geo = geo;
      if (geo) {
        this.updateForm(geo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const geo = this.geoFormService.getGeo(this.editForm);
    if (geo.id === null) {
      this.subscribeToSaveResponse(this.geoService.create(geo));
    } else {
      this.subscribeToSaveResponse(this.geoService.update(geo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IGeo | null>): void {
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

  protected updateForm(geo: IGeo): void {
    this.geo = geo;
    this.geoFormService.resetForm(this.editForm, geo);

    this.geoTypesSharedCollection.update(geoTypes => this.geoTypeService.addGeoTypeToCollectionIfMissing<IGeoType>(geoTypes, geo.geoType));
  }

  protected loadRelationshipsOptions(): void {
    this.geoTypeService
      .query()
      .pipe(map((res: HttpResponse<IGeoType[]>) => res.body ?? []))
      .pipe(map((geoTypes: IGeoType[]) => this.geoTypeService.addGeoTypeToCollectionIfMissing<IGeoType>(geoTypes, this.geo?.geoType)))
      .subscribe((geoTypes: IGeoType[]) => this.geoTypesSharedCollection.set(geoTypes));
  }
}
