import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICatalogueCategoryType } from '../catalogue-category-type.model';
import { CatalogueCategoryTypeService } from '../service/catalogue-category-type.service';

import { CatalogueCategoryTypeFormGroup, CatalogueCategoryTypeFormService } from './catalogue-category-type-form.service';

@Component({
  selector: 'sys-catalogue-category-type-update',
  templateUrl: './catalogue-category-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CatalogueCategoryTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  catalogueCategoryType: ICatalogueCategoryType | null = null;

  protected catalogueCategoryTypeService = inject(CatalogueCategoryTypeService);
  protected catalogueCategoryTypeFormService = inject(CatalogueCategoryTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CatalogueCategoryTypeFormGroup = this.catalogueCategoryTypeFormService.createCatalogueCategoryTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogueCategoryType }) => {
      this.catalogueCategoryType = catalogueCategoryType;
      if (catalogueCategoryType) {
        this.updateForm(catalogueCategoryType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const catalogueCategoryType = this.catalogueCategoryTypeFormService.getCatalogueCategoryType(this.editForm);
    if (catalogueCategoryType.id === null) {
      this.subscribeToSaveResponse(this.catalogueCategoryTypeService.create(catalogueCategoryType));
    } else {
      this.subscribeToSaveResponse(this.catalogueCategoryTypeService.update(catalogueCategoryType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICatalogueCategoryType | null>): void {
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

  protected updateForm(catalogueCategoryType: ICatalogueCategoryType): void {
    this.catalogueCategoryType = catalogueCategoryType;
    this.catalogueCategoryTypeFormService.resetForm(this.editForm, catalogueCategoryType);
  }
}
