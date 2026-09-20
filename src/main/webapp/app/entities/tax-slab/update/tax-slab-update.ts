import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TaxSlabService } from '../service/tax-slab.service';
import { ITaxSlab } from '../tax-slab.model';

import { TaxSlabFormGroup, TaxSlabFormService } from './tax-slab-form.service';

@Component({
  selector: 'sys-tax-slab-update',
  templateUrl: './tax-slab-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TaxSlabUpdate implements OnInit {
  readonly isSaving = signal(false);
  taxSlab: ITaxSlab | null = null;

  protected taxSlabService = inject(TaxSlabService);
  protected taxSlabFormService = inject(TaxSlabFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TaxSlabFormGroup = this.taxSlabFormService.createTaxSlabFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxSlab }) => {
      this.taxSlab = taxSlab;
      if (taxSlab) {
        this.updateForm(taxSlab);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const taxSlab = this.taxSlabFormService.getTaxSlab(this.editForm);
    if (taxSlab.id === null) {
      this.subscribeToSaveResponse(this.taxSlabService.create(taxSlab));
    } else {
      this.subscribeToSaveResponse(this.taxSlabService.update(taxSlab));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITaxSlab | null>): void {
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

  protected updateForm(taxSlab: ITaxSlab): void {
    this.taxSlab = taxSlab;
    this.taxSlabFormService.resetForm(this.editForm, taxSlab);
  }
}
