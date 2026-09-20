import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IGeo } from 'app/entities/geo/geo.model';
import { GeoService } from 'app/entities/geo/service/geo.service';
import { IGeoAssocType } from 'app/entities/geo-assoc-type/geo-assoc-type.model';
import { GeoAssocTypeService } from 'app/entities/geo-assoc-type/service/geo-assoc-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGeoAssoc } from '../geo-assoc.model';
import { GeoAssocService } from '../service/geo-assoc.service';

import { GeoAssocFormGroup, GeoAssocFormService } from './geo-assoc-form.service';

@Component({
  selector: 'sys-geo-assoc-update',
  templateUrl: './geo-assoc-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class GeoAssocUpdate implements OnInit {
  readonly isSaving = signal(false);
  geoAssoc: IGeoAssoc | null = null;

  geosSharedCollection = signal<IGeo[]>([]);
  geoAssocTypesSharedCollection = signal<IGeoAssocType[]>([]);

  protected geoAssocService = inject(GeoAssocService);
  protected geoAssocFormService = inject(GeoAssocFormService);
  protected geoService = inject(GeoService);
  protected geoAssocTypeService = inject(GeoAssocTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GeoAssocFormGroup = this.geoAssocFormService.createGeoAssocFormGroup();

  compareGeo = (o1: IGeo | null, o2: IGeo | null): boolean => this.geoService.compareGeo(o1, o2);

  compareGeoAssocType = (o1: IGeoAssocType | null, o2: IGeoAssocType | null): boolean =>
    this.geoAssocTypeService.compareGeoAssocType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ geoAssoc }) => {
      this.geoAssoc = geoAssoc;
      if (geoAssoc) {
        this.updateForm(geoAssoc);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const geoAssoc = this.geoAssocFormService.getGeoAssoc(this.editForm);
    if (geoAssoc.id === null) {
      this.subscribeToSaveResponse(this.geoAssocService.create(geoAssoc));
    } else {
      this.subscribeToSaveResponse(this.geoAssocService.update(geoAssoc));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IGeoAssoc | null>): void {
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

  protected updateForm(geoAssoc: IGeoAssoc): void {
    this.geoAssoc = geoAssoc;
    this.geoAssocFormService.resetForm(this.editForm, geoAssoc);

    this.geosSharedCollection.update(geos => this.geoService.addGeoToCollectionIfMissing<IGeo>(geos, geoAssoc.geo, geoAssoc.geoTo));
    this.geoAssocTypesSharedCollection.update(geoAssocTypes =>
      this.geoAssocTypeService.addGeoAssocTypeToCollectionIfMissing<IGeoAssocType>(geoAssocTypes, geoAssoc.geoAssocType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.geoService
      .query()
      .pipe(map((res: HttpResponse<IGeo[]>) => res.body ?? []))
      .pipe(map((geos: IGeo[]) => this.geoService.addGeoToCollectionIfMissing<IGeo>(geos, this.geoAssoc?.geo, this.geoAssoc?.geoTo)))
      .subscribe((geos: IGeo[]) => this.geosSharedCollection.set(geos));

    this.geoAssocTypeService
      .query()
      .pipe(map((res: HttpResponse<IGeoAssocType[]>) => res.body ?? []))
      .pipe(
        map((geoAssocTypes: IGeoAssocType[]) =>
          this.geoAssocTypeService.addGeoAssocTypeToCollectionIfMissing<IGeoAssocType>(geoAssocTypes, this.geoAssoc?.geoAssocType),
        ),
      )
      .subscribe((geoAssocTypes: IGeoAssocType[]) => this.geoAssocTypesSharedCollection.set(geoAssocTypes));
  }
}
