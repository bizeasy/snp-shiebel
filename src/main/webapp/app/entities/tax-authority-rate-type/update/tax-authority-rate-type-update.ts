import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { TaxAuthorityService } from 'app/entities/tax-authority/service/tax-authority.service';
import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';
import { TaxSlabService } from 'app/entities/tax-slab/service/tax-slab.service';
import { ITaxSlab } from 'app/entities/tax-slab/tax-slab.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TaxAuthorityRateTypeService } from '../service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from '../tax-authority-rate-type.model';

import { TaxAuthorityRateTypeFormGroup, TaxAuthorityRateTypeFormService } from './tax-authority-rate-type-form.service';

@Component({
  selector: 'sys-tax-authority-rate-type-update',
  templateUrl: './tax-authority-rate-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TaxAuthorityRateTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  taxAuthorityRateType: ITaxAuthorityRateType | null = null;

  taxAuthoritiesSharedCollection = signal<ITaxAuthority[]>([]);
  taxSlabsSharedCollection = signal<ITaxSlab[]>([]);

  protected taxAuthorityRateTypeService = inject(TaxAuthorityRateTypeService);
  protected taxAuthorityRateTypeFormService = inject(TaxAuthorityRateTypeFormService);
  protected taxAuthorityService = inject(TaxAuthorityService);
  protected taxSlabService = inject(TaxSlabService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TaxAuthorityRateTypeFormGroup = this.taxAuthorityRateTypeFormService.createTaxAuthorityRateTypeFormGroup();

  compareTaxAuthority = (o1: ITaxAuthority | null, o2: ITaxAuthority | null): boolean =>
    this.taxAuthorityService.compareTaxAuthority(o1, o2);

  compareTaxSlab = (o1: ITaxSlab | null, o2: ITaxSlab | null): boolean => this.taxSlabService.compareTaxSlab(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxAuthorityRateType }) => {
      this.taxAuthorityRateType = taxAuthorityRateType;
      if (taxAuthorityRateType) {
        this.updateForm(taxAuthorityRateType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const taxAuthorityRateType = this.taxAuthorityRateTypeFormService.getTaxAuthorityRateType(this.editForm);
    if (taxAuthorityRateType.id === null) {
      this.subscribeToSaveResponse(this.taxAuthorityRateTypeService.create(taxAuthorityRateType));
    } else {
      this.subscribeToSaveResponse(this.taxAuthorityRateTypeService.update(taxAuthorityRateType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITaxAuthorityRateType | null>): void {
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

  protected updateForm(taxAuthorityRateType: ITaxAuthorityRateType): void {
    this.taxAuthorityRateType = taxAuthorityRateType;
    this.taxAuthorityRateTypeFormService.resetForm(this.editForm, taxAuthorityRateType);

    this.taxAuthoritiesSharedCollection.update(taxAuthorities =>
      this.taxAuthorityService.addTaxAuthorityToCollectionIfMissing<ITaxAuthority>(taxAuthorities, taxAuthorityRateType.taxAuthority),
    );
    this.taxSlabsSharedCollection.update(taxSlabs =>
      this.taxSlabService.addTaxSlabToCollectionIfMissing<ITaxSlab>(taxSlabs, taxAuthorityRateType.taxSlab),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.taxAuthorityService
      .query()
      .pipe(map((res: HttpResponse<ITaxAuthority[]>) => res.body ?? []))
      .pipe(
        map((taxAuthorities: ITaxAuthority[]) =>
          this.taxAuthorityService.addTaxAuthorityToCollectionIfMissing<ITaxAuthority>(
            taxAuthorities,
            this.taxAuthorityRateType?.taxAuthority,
          ),
        ),
      )
      .subscribe((taxAuthorities: ITaxAuthority[]) => this.taxAuthoritiesSharedCollection.set(taxAuthorities));

    this.taxSlabService
      .query()
      .pipe(map((res: HttpResponse<ITaxSlab[]>) => res.body ?? []))
      .pipe(
        map((taxSlabs: ITaxSlab[]) =>
          this.taxSlabService.addTaxSlabToCollectionIfMissing<ITaxSlab>(taxSlabs, this.taxAuthorityRateType?.taxSlab),
        ),
      )
      .subscribe((taxSlabs: ITaxSlab[]) => this.taxSlabsSharedCollection.set(taxSlabs));
  }
}
