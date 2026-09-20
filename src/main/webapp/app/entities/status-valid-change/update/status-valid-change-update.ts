import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { StatusValidChangeService } from '../service/status-valid-change.service';
import { IStatusValidChange } from '../status-valid-change.model';

import { StatusValidChangeFormGroup, StatusValidChangeFormService } from './status-valid-change-form.service';

@Component({
  selector: 'sys-status-valid-change-update',
  templateUrl: './status-valid-change-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class StatusValidChangeUpdate implements OnInit {
  readonly isSaving = signal(false);
  statusValidChange: IStatusValidChange | null = null;

  statusesSharedCollection = signal<IStatus[]>([]);

  protected statusValidChangeService = inject(StatusValidChangeService);
  protected statusValidChangeFormService = inject(StatusValidChangeFormService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StatusValidChangeFormGroup = this.statusValidChangeFormService.createStatusValidChangeFormGroup();

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statusValidChange }) => {
      this.statusValidChange = statusValidChange;
      if (statusValidChange) {
        this.updateForm(statusValidChange);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const statusValidChange = this.statusValidChangeFormService.getStatusValidChange(this.editForm);
    if (statusValidChange.id === null) {
      this.subscribeToSaveResponse(this.statusValidChangeService.create(statusValidChange));
    } else {
      this.subscribeToSaveResponse(this.statusValidChangeService.update(statusValidChange));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IStatusValidChange | null>): void {
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

  protected updateForm(statusValidChange: IStatusValidChange): void {
    this.statusValidChange = statusValidChange;
    this.statusValidChangeFormService.resetForm(this.editForm, statusValidChange);

    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, statusValidChange.status, statusValidChange.statusTo),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(
        map((statuses: IStatus[]) =>
          this.statusService.addStatusToCollectionIfMissing<IStatus>(
            statuses,
            this.statusValidChange?.status,
            this.statusValidChange?.statusTo,
          ),
        ),
      )
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
