import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ILot } from '../lot.model';
import { LotService } from '../service/lot.service';

import { LotFormGroup, LotFormService } from './lot-form.service';

@Component({
  selector: 'sys-lot-update',
  templateUrl: './lot-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class LotUpdate implements OnInit {
  readonly isSaving = signal(false);
  lot: ILot | null = null;

  protected lotService = inject(LotService);
  protected lotFormService = inject(LotFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LotFormGroup = this.lotFormService.createLotFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lot }) => {
      this.lot = lot;
      if (lot) {
        this.updateForm(lot);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const lot = this.lotFormService.getLot(this.editForm);
    if (lot.id === null) {
      this.subscribeToSaveResponse(this.lotService.create(lot));
    } else {
      this.subscribeToSaveResponse(this.lotService.update(lot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ILot | null>): void {
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

  protected updateForm(lot: ILot): void {
    this.lot = lot;
    this.lotFormService.resetForm(this.editForm, lot);
  }
}
