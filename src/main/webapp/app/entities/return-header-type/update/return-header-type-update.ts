import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IReturnHeaderType } from '../return-header-type.model';
import { ReturnHeaderTypeService } from '../service/return-header-type.service';

import { ReturnHeaderTypeFormGroup, ReturnHeaderTypeFormService } from './return-header-type-form.service';

@Component({
  selector: 'sys-return-header-type-update',
  templateUrl: './return-header-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ReturnHeaderTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  returnHeaderType: IReturnHeaderType | null = null;

  protected returnHeaderTypeService = inject(ReturnHeaderTypeService);
  protected returnHeaderTypeFormService = inject(ReturnHeaderTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReturnHeaderTypeFormGroup = this.returnHeaderTypeFormService.createReturnHeaderTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnHeaderType }) => {
      this.returnHeaderType = returnHeaderType;
      if (returnHeaderType) {
        this.updateForm(returnHeaderType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const returnHeaderType = this.returnHeaderTypeFormService.getReturnHeaderType(this.editForm);
    if (returnHeaderType.id === null) {
      this.subscribeToSaveResponse(this.returnHeaderTypeService.create(returnHeaderType));
    } else {
      this.subscribeToSaveResponse(this.returnHeaderTypeService.update(returnHeaderType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IReturnHeaderType | null>): void {
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

  protected updateForm(returnHeaderType: IReturnHeaderType): void {
    this.returnHeaderType = returnHeaderType;
    this.returnHeaderTypeFormService.resetForm(this.editForm, returnHeaderType);
  }
}
