import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPartyClassificationGroup } from 'app/entities/party-classification-group/party-classification-group.model';
import { PartyClassificationGroupService } from 'app/entities/party-classification-group/service/party-classification-group.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyClassification } from '../party-classification.model';
import { PartyClassificationService } from '../service/party-classification.service';

import { PartyClassificationFormGroup, PartyClassificationFormService } from './party-classification-form.service';

@Component({
  selector: 'sys-party-classification-update',
  templateUrl: './party-classification-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyClassificationUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyClassification: IPartyClassification | null = null;

  partiesSharedCollection = signal<IParty[]>([]);
  partyClassificationGroupsSharedCollection = signal<IPartyClassificationGroup[]>([]);

  protected partyClassificationService = inject(PartyClassificationService);
  protected partyClassificationFormService = inject(PartyClassificationFormService);
  protected partyService = inject(PartyService);
  protected partyClassificationGroupService = inject(PartyClassificationGroupService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyClassificationFormGroup = this.partyClassificationFormService.createPartyClassificationFormGroup();

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  comparePartyClassificationGroup = (o1: IPartyClassificationGroup | null, o2: IPartyClassificationGroup | null): boolean =>
    this.partyClassificationGroupService.comparePartyClassificationGroup(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyClassification }) => {
      this.partyClassification = partyClassification;
      if (partyClassification) {
        this.updateForm(partyClassification);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyClassification = this.partyClassificationFormService.getPartyClassification(this.editForm);
    if (partyClassification.id === null) {
      this.subscribeToSaveResponse(this.partyClassificationService.create(partyClassification));
    } else {
      this.subscribeToSaveResponse(this.partyClassificationService.update(partyClassification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyClassification | null>): void {
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

  protected updateForm(partyClassification: IPartyClassification): void {
    this.partyClassification = partyClassification;
    this.partyClassificationFormService.resetForm(this.editForm, partyClassification);

    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, partyClassification.party),
    );
    this.partyClassificationGroupsSharedCollection.update(partyClassificationGroups =>
      this.partyClassificationGroupService.addPartyClassificationGroupToCollectionIfMissing<IPartyClassificationGroup>(
        partyClassificationGroups,
        partyClassification.classificationGroup,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.partyClassification?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.partyClassificationGroupService
      .query()
      .pipe(map((res: HttpResponse<IPartyClassificationGroup[]>) => res.body ?? []))
      .pipe(
        map((partyClassificationGroups: IPartyClassificationGroup[]) =>
          this.partyClassificationGroupService.addPartyClassificationGroupToCollectionIfMissing<IPartyClassificationGroup>(
            partyClassificationGroups,
            this.partyClassification?.classificationGroup,
          ),
        ),
      )
      .subscribe((partyClassificationGroups: IPartyClassificationGroup[]) =>
        this.partyClassificationGroupsSharedCollection.set(partyClassificationGroups),
      );
  }
}
