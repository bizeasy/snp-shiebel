import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPartyIdentificationType } from 'app/entities/party-identification-type/party-identification-type.model';
import { PartyIdentificationTypeService } from 'app/entities/party-identification-type/service/party-identification-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyIdentification } from '../party-identification.model';
import { PartyIdentificationService } from '../service/party-identification.service';

import { PartyIdentificationFormGroup, PartyIdentificationFormService } from './party-identification-form.service';

@Component({
  selector: 'sys-party-identification-update',
  templateUrl: './party-identification-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyIdentificationUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyIdentification: IPartyIdentification | null = null;

  partiesSharedCollection = signal<IParty[]>([]);
  partyIdentificationTypesSharedCollection = signal<IPartyIdentificationType[]>([]);

  protected partyIdentificationService = inject(PartyIdentificationService);
  protected partyIdentificationFormService = inject(PartyIdentificationFormService);
  protected partyService = inject(PartyService);
  protected partyIdentificationTypeService = inject(PartyIdentificationTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyIdentificationFormGroup = this.partyIdentificationFormService.createPartyIdentificationFormGroup();

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  comparePartyIdentificationType = (o1: IPartyIdentificationType | null, o2: IPartyIdentificationType | null): boolean =>
    this.partyIdentificationTypeService.comparePartyIdentificationType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyIdentification }) => {
      this.partyIdentification = partyIdentification;
      if (partyIdentification) {
        this.updateForm(partyIdentification);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyIdentification = this.partyIdentificationFormService.getPartyIdentification(this.editForm);
    if (partyIdentification.id === null) {
      this.subscribeToSaveResponse(this.partyIdentificationService.create(partyIdentification));
    } else {
      this.subscribeToSaveResponse(this.partyIdentificationService.update(partyIdentification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyIdentification | null>): void {
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

  protected updateForm(partyIdentification: IPartyIdentification): void {
    this.partyIdentification = partyIdentification;
    this.partyIdentificationFormService.resetForm(this.editForm, partyIdentification);

    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, partyIdentification.party),
    );
    this.partyIdentificationTypesSharedCollection.update(partyIdentificationTypes =>
      this.partyIdentificationTypeService.addPartyIdentificationTypeToCollectionIfMissing<IPartyIdentificationType>(
        partyIdentificationTypes,
        partyIdentification.type,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.partyIdentification?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.partyIdentificationTypeService
      .query()
      .pipe(map((res: HttpResponse<IPartyIdentificationType[]>) => res.body ?? []))
      .pipe(
        map((partyIdentificationTypes: IPartyIdentificationType[]) =>
          this.partyIdentificationTypeService.addPartyIdentificationTypeToCollectionIfMissing<IPartyIdentificationType>(
            partyIdentificationTypes,
            this.partyIdentification?.type,
          ),
        ),
      )
      .subscribe((partyIdentificationTypes: IPartyIdentificationType[]) =>
        this.partyIdentificationTypesSharedCollection.set(partyIdentificationTypes),
      );
  }
}
