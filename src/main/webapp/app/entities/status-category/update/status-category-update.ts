import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { StatusCategoryService } from '../service/status-category.service';
import { IStatusCategory } from '../status-category.model';

import { StatusCategoryFormGroup, StatusCategoryFormService } from './status-category-form.service';

@Component({
  selector: 'sys-status-category-update',
  templateUrl: './status-category-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class StatusCategoryUpdate implements OnInit {
  readonly isSaving = signal(false);
  statusCategory: IStatusCategory | null = null;

  protected statusCategoryService = inject(StatusCategoryService);
  protected statusCategoryFormService = inject(StatusCategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StatusCategoryFormGroup = this.statusCategoryFormService.createStatusCategoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statusCategory }) => {
      this.statusCategory = statusCategory;
      if (statusCategory) {
        this.updateForm(statusCategory);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const statusCategory = this.statusCategoryFormService.getStatusCategory(this.editForm);
    if (statusCategory.id === null) {
      this.subscribeToSaveResponse(this.statusCategoryService.create(statusCategory));
    } else {
      this.subscribeToSaveResponse(this.statusCategoryService.update(statusCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IStatusCategory | null>): void {
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

  protected updateForm(statusCategory: IStatusCategory): void {
    this.statusCategory = statusCategory;
    this.statusCategoryFormService.resetForm(this.editForm, statusCategory);
  }
}
