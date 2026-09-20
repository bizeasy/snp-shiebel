import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { SystemParametersService } from '../service/system-parameters.service';
import { ISystemParameters } from '../system-parameters.model';

import { SystemParametersFormGroup, SystemParametersFormService } from './system-parameters-form.service';

@Component({
  selector: 'sys-system-parameters-update',
  templateUrl: './system-parameters-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class SystemParametersUpdate implements OnInit {
  readonly isSaving = signal(false);
  systemParameters: ISystemParameters | null = null;

  protected systemParametersService = inject(SystemParametersService);
  protected systemParametersFormService = inject(SystemParametersFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SystemParametersFormGroup = this.systemParametersFormService.createSystemParametersFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemParameters }) => {
      this.systemParameters = systemParameters;
      if (systemParameters) {
        this.updateForm(systemParameters);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const systemParameters = this.systemParametersFormService.getSystemParameters(this.editForm);
    if (systemParameters.id === null) {
      this.subscribeToSaveResponse(this.systemParametersService.create(systemParameters));
    } else {
      this.subscribeToSaveResponse(this.systemParametersService.update(systemParameters));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ISystemParameters | null>): void {
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

  protected updateForm(systemParameters: ISystemParameters): void {
    this.systemParameters = systemParameters;
    this.systemParametersFormService.resetForm(this.editForm, systemParameters);
  }
}
