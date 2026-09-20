import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICatalogue } from '../catalogue.model';
import { CatalogueService } from '../service/catalogue.service';

import { CatalogueFormGroup, CatalogueFormService } from './catalogue-form.service';

@Component({
  selector: 'sys-catalogue-update',
  templateUrl: './catalogue-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CatalogueUpdate implements OnInit {
  readonly isSaving = signal(false);
  catalogue: ICatalogue | null = null;

  protected catalogueService = inject(CatalogueService);
  protected catalogueFormService = inject(CatalogueFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CatalogueFormGroup = this.catalogueFormService.createCatalogueFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogue }) => {
      this.catalogue = catalogue;
      if (catalogue) {
        this.updateForm(catalogue);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const catalogue = this.catalogueFormService.getCatalogue(this.editForm);
    if (catalogue.id === null) {
      this.subscribeToSaveResponse(this.catalogueService.create(catalogue));
    } else {
      this.subscribeToSaveResponse(this.catalogueService.update(catalogue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICatalogue | null>): void {
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

  protected updateForm(catalogue: ICatalogue): void {
    this.catalogue = catalogue;
    this.catalogueFormService.resetForm(this.editForm, catalogue);
  }
}
