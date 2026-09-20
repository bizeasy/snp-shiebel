import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { StatusCategoryService } from 'app/entities/status-category/service/status-category.service';
import { IStatusCategory } from 'app/entities/status-category/status-category.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { StatusService } from '../service/status.service';
import { IStatus } from '../status.model';

import { StatusFormGroup, StatusFormService } from './status-form.service';

@Component({
  selector: 'sys-status-update',
  templateUrl: './status-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class StatusUpdate implements OnInit {
  readonly isSaving = signal(false);
  status: IStatus | null = null;

  statusCategoriesSharedCollection = signal<IStatusCategory[]>([]);

  protected statusService = inject(StatusService);
  protected statusFormService = inject(StatusFormService);
  protected statusCategoryService = inject(StatusCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StatusFormGroup = this.statusFormService.createStatusFormGroup();

  compareStatusCategory = (o1: IStatusCategory | null, o2: IStatusCategory | null): boolean =>
    this.statusCategoryService.compareStatusCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ status }) => {
      this.status = status;
      if (status) {
        this.updateForm(status);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const status = this.statusFormService.getStatus(this.editForm);
    if (status.id === null) {
      this.subscribeToSaveResponse(this.statusService.create(status));
    } else {
      this.subscribeToSaveResponse(this.statusService.update(status));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IStatus | null>): void {
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

  protected updateForm(status: IStatus): void {
    this.status = status;
    this.statusFormService.resetForm(this.editForm, status);

    this.statusCategoriesSharedCollection.update(statusCategories =>
      this.statusCategoryService.addStatusCategoryToCollectionIfMissing<IStatusCategory>(statusCategories, status.category),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.statusCategoryService
      .query()
      .pipe(map((res: HttpResponse<IStatusCategory[]>) => res.body ?? []))
      .pipe(
        map((statusCategories: IStatusCategory[]) =>
          this.statusCategoryService.addStatusCategoryToCollectionIfMissing<IStatusCategory>(statusCategories, this.status?.category),
        ),
      )
      .subscribe((statusCategories: IStatusCategory[]) => this.statusCategoriesSharedCollection.set(statusCategories));
  }
}
