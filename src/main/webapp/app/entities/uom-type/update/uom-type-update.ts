import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { UomTypeService } from '../service/uom-type.service';
import { IUomType } from '../uom-type.model';

import { UomTypeFormGroup, UomTypeFormService } from './uom-type-form.service';

@Component({
  selector: 'sys-uom-type-update',
  templateUrl: './uom-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class UomTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  uomType: IUomType | null = null;

  protected uomTypeService = inject(UomTypeService);
  protected uomTypeFormService = inject(UomTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UomTypeFormGroup = this.uomTypeFormService.createUomTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uomType }) => {
      this.uomType = uomType;
      if (uomType) {
        this.updateForm(uomType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const uomType = this.uomTypeFormService.getUomType(this.editForm);
    if (uomType.id === null) {
      this.subscribeToSaveResponse(this.uomTypeService.create(uomType));
    } else {
      this.subscribeToSaveResponse(this.uomTypeService.update(uomType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IUomType | null>): void {
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

  protected updateForm(uomType: IUomType): void {
    this.uomType = uomType;
    this.uomTypeFormService.resetForm(this.editForm, uomType);
  }
}
