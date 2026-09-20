import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOtpAuth } from '../otp-auth.model';
import { OtpAuthService } from '../service/otp-auth.service';

import { OtpAuthFormGroup, OtpAuthFormService } from './otp-auth-form.service';

@Component({
  selector: 'sys-otp-auth-update',
  templateUrl: './otp-auth-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OtpAuthUpdate implements OnInit {
  readonly isSaving = signal(false);
  otpAuth: IOtpAuth | null = null;

  partiesSharedCollection = signal<IParty[]>([]);

  protected otpAuthService = inject(OtpAuthService);
  protected otpAuthFormService = inject(OtpAuthFormService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OtpAuthFormGroup = this.otpAuthFormService.createOtpAuthFormGroup();

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ otpAuth }) => {
      this.otpAuth = otpAuth;
      if (otpAuth) {
        this.updateForm(otpAuth);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const otpAuth = this.otpAuthFormService.getOtpAuth(this.editForm);
    if (otpAuth.id === null) {
      this.subscribeToSaveResponse(this.otpAuthService.create(otpAuth));
    } else {
      this.subscribeToSaveResponse(this.otpAuthService.update(otpAuth));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOtpAuth | null>): void {
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

  protected updateForm(otpAuth: IOtpAuth): void {
    this.otpAuth = otpAuth;
    this.otpAuthFormService.resetForm(this.editForm, otpAuth);

    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, otpAuth.party));
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.otpAuth?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
