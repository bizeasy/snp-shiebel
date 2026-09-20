import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyIdentificationType } from '../party-identification-type.model';
import { PartyIdentificationTypeService } from '../service/party-identification-type.service';

import { PartyIdentificationTypeFormGroup, PartyIdentificationTypeFormService } from './party-identification-type-form.service';

@Component({
  selector: 'sys-party-identification-type-update',
  templateUrl: './party-identification-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyIdentificationTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyIdentificationType: IPartyIdentificationType | null = null;

  protected partyIdentificationTypeService = inject(PartyIdentificationTypeService);
  protected partyIdentificationTypeFormService = inject(PartyIdentificationTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyIdentificationTypeFormGroup = this.partyIdentificationTypeFormService.createPartyIdentificationTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyIdentificationType }) => {
      this.partyIdentificationType = partyIdentificationType;
      if (partyIdentificationType) {
        this.updateForm(partyIdentificationType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyIdentificationType = this.partyIdentificationTypeFormService.getPartyIdentificationType(this.editForm);
    if (partyIdentificationType.id === null) {
      this.subscribeToSaveResponse(this.partyIdentificationTypeService.create(partyIdentificationType));
    } else {
      this.subscribeToSaveResponse(this.partyIdentificationTypeService.update(partyIdentificationType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyIdentificationType | null>): void {
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

  protected updateForm(partyIdentificationType: IPartyIdentificationType): void {
    this.partyIdentificationType = partyIdentificationType;
    this.partyIdentificationTypeFormService.resetForm(this.editForm, partyIdentificationType);
  }
}
