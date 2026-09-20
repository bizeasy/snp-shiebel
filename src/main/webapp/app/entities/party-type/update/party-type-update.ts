import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyType } from '../party-type.model';
import { PartyTypeService } from '../service/party-type.service';

import { PartyTypeFormGroup, PartyTypeFormService } from './party-type-form.service';

@Component({
  selector: 'sys-party-type-update',
  templateUrl: './party-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyType: IPartyType | null = null;

  protected partyTypeService = inject(PartyTypeService);
  protected partyTypeFormService = inject(PartyTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyTypeFormGroup = this.partyTypeFormService.createPartyTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyType }) => {
      this.partyType = partyType;
      if (partyType) {
        this.updateForm(partyType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyType = this.partyTypeFormService.getPartyType(this.editForm);
    if (partyType.id === null) {
      this.subscribeToSaveResponse(this.partyTypeService.create(partyType));
    } else {
      this.subscribeToSaveResponse(this.partyTypeService.update(partyType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyType | null>): void {
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

  protected updateForm(partyType: IPartyType): void {
    this.partyType = partyType;
    this.partyTypeFormService.resetForm(this.editForm, partyType);
  }
}
