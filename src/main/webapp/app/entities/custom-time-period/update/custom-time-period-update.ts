import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustomTimePeriod } from '../custom-time-period.model';
import { CustomTimePeriodService } from '../service/custom-time-period.service';

import { CustomTimePeriodFormGroup, CustomTimePeriodFormService } from './custom-time-period-form.service';

@Component({
  selector: 'sys-custom-time-period-update',
  templateUrl: './custom-time-period-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class CustomTimePeriodUpdate implements OnInit {
  readonly isSaving = signal(false);
  customTimePeriod: ICustomTimePeriod | null = null;

  protected customTimePeriodService = inject(CustomTimePeriodService);
  protected customTimePeriodFormService = inject(CustomTimePeriodFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomTimePeriodFormGroup = this.customTimePeriodFormService.createCustomTimePeriodFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customTimePeriod }) => {
      this.customTimePeriod = customTimePeriod;
      if (customTimePeriod) {
        this.updateForm(customTimePeriod);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const customTimePeriod = this.customTimePeriodFormService.getCustomTimePeriod(this.editForm);
    if (customTimePeriod.id === null) {
      this.subscribeToSaveResponse(this.customTimePeriodService.create(customTimePeriod));
    } else {
      this.subscribeToSaveResponse(this.customTimePeriodService.update(customTimePeriod));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustomTimePeriod | null>): void {
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

  protected updateForm(customTimePeriod: ICustomTimePeriod): void {
    this.customTimePeriod = customTimePeriod;
    this.customTimePeriodFormService.resetForm(this.editForm, customTimePeriod);
  }
}
