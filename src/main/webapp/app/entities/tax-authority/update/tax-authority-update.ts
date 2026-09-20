import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IGeo } from 'app/entities/geo/geo.model';
import { GeoService } from 'app/entities/geo/service/geo.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TaxAuthorityService } from '../service/tax-authority.service';
import { ITaxAuthority } from '../tax-authority.model';

import { TaxAuthorityFormGroup, TaxAuthorityFormService } from './tax-authority-form.service';

@Component({
  selector: 'sys-tax-authority-update',
  templateUrl: './tax-authority-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TaxAuthorityUpdate implements OnInit {
  readonly isSaving = signal(false);
  taxAuthority: ITaxAuthority | null = null;

  geosSharedCollection = signal<IGeo[]>([]);

  protected taxAuthorityService = inject(TaxAuthorityService);
  protected taxAuthorityFormService = inject(TaxAuthorityFormService);
  protected geoService = inject(GeoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TaxAuthorityFormGroup = this.taxAuthorityFormService.createTaxAuthorityFormGroup();

  compareGeo = (o1: IGeo | null, o2: IGeo | null): boolean => this.geoService.compareGeo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxAuthority }) => {
      this.taxAuthority = taxAuthority;
      if (taxAuthority) {
        this.updateForm(taxAuthority);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const taxAuthority = this.taxAuthorityFormService.getTaxAuthority(this.editForm);
    if (taxAuthority.id === null) {
      this.subscribeToSaveResponse(this.taxAuthorityService.create(taxAuthority));
    } else {
      this.subscribeToSaveResponse(this.taxAuthorityService.update(taxAuthority));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITaxAuthority | null>): void {
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

  protected updateForm(taxAuthority: ITaxAuthority): void {
    this.taxAuthority = taxAuthority;
    this.taxAuthorityFormService.resetForm(this.editForm, taxAuthority);

    this.geosSharedCollection.update(geos => this.geoService.addGeoToCollectionIfMissing<IGeo>(geos, taxAuthority.geo));
  }

  protected loadRelationshipsOptions(): void {
    this.geoService
      .query()
      .pipe(map((res: HttpResponse<IGeo[]>) => res.body ?? []))
      .pipe(map((geos: IGeo[]) => this.geoService.addGeoToCollectionIfMissing<IGeo>(geos, this.taxAuthority?.geo)))
      .subscribe((geos: IGeo[]) => this.geosSharedCollection.set(geos));
  }
}
