import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyClassificationType } from '../party-classification-type.model';
import { PartyClassificationTypeService } from '../service/party-classification-type.service';

import { PartyClassificationTypeFormGroup, PartyClassificationTypeFormService } from './party-classification-type-form.service';

@Component({
  selector: 'sys-party-classification-type-update',
  templateUrl: './party-classification-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyClassificationTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyClassificationType: IPartyClassificationType | null = null;

  protected partyClassificationTypeService = inject(PartyClassificationTypeService);
  protected partyClassificationTypeFormService = inject(PartyClassificationTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyClassificationTypeFormGroup = this.partyClassificationTypeFormService.createPartyClassificationTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyClassificationType }) => {
      this.partyClassificationType = partyClassificationType;
      if (partyClassificationType) {
        this.updateForm(partyClassificationType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyClassificationType = this.partyClassificationTypeFormService.getPartyClassificationType(this.editForm);
    if (partyClassificationType.id === null) {
      this.subscribeToSaveResponse(this.partyClassificationTypeService.create(partyClassificationType));
    } else {
      this.subscribeToSaveResponse(this.partyClassificationTypeService.update(partyClassificationType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyClassificationType | null>): void {
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

  protected updateForm(partyClassificationType: IPartyClassificationType): void {
    this.partyClassificationType = partyClassificationType;
    this.partyClassificationTypeFormService.resetForm(this.editForm, partyClassificationType);
  }
}
