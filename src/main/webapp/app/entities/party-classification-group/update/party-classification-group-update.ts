import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IPartyClassificationType } from 'app/entities/party-classification-type/party-classification-type.model';
import { PartyClassificationTypeService } from 'app/entities/party-classification-type/service/party-classification-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyClassificationGroup } from '../party-classification-group.model';
import { PartyClassificationGroupService } from '../service/party-classification-group.service';

import { PartyClassificationGroupFormGroup, PartyClassificationGroupFormService } from './party-classification-group-form.service';

@Component({
  selector: 'sys-party-classification-group-update',
  templateUrl: './party-classification-group-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyClassificationGroupUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyClassificationGroup: IPartyClassificationGroup | null = null;

  partyClassificationTypesSharedCollection = signal<IPartyClassificationType[]>([]);

  protected partyClassificationGroupService = inject(PartyClassificationGroupService);
  protected partyClassificationGroupFormService = inject(PartyClassificationGroupFormService);
  protected partyClassificationTypeService = inject(PartyClassificationTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyClassificationGroupFormGroup = this.partyClassificationGroupFormService.createPartyClassificationGroupFormGroup();

  comparePartyClassificationType = (o1: IPartyClassificationType | null, o2: IPartyClassificationType | null): boolean =>
    this.partyClassificationTypeService.comparePartyClassificationType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyClassificationGroup }) => {
      this.partyClassificationGroup = partyClassificationGroup;
      if (partyClassificationGroup) {
        this.updateForm(partyClassificationGroup);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyClassificationGroup = this.partyClassificationGroupFormService.getPartyClassificationGroup(this.editForm);
    if (partyClassificationGroup.id === null) {
      this.subscribeToSaveResponse(this.partyClassificationGroupService.create(partyClassificationGroup));
    } else {
      this.subscribeToSaveResponse(this.partyClassificationGroupService.update(partyClassificationGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyClassificationGroup | null>): void {
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

  protected updateForm(partyClassificationGroup: IPartyClassificationGroup): void {
    this.partyClassificationGroup = partyClassificationGroup;
    this.partyClassificationGroupFormService.resetForm(this.editForm, partyClassificationGroup);

    this.partyClassificationTypesSharedCollection.update(partyClassificationTypes =>
      this.partyClassificationTypeService.addPartyClassificationTypeToCollectionIfMissing<IPartyClassificationType>(
        partyClassificationTypes,
        partyClassificationGroup.classificationType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyClassificationTypeService
      .query()
      .pipe(map((res: HttpResponse<IPartyClassificationType[]>) => res.body ?? []))
      .pipe(
        map((partyClassificationTypes: IPartyClassificationType[]) =>
          this.partyClassificationTypeService.addPartyClassificationTypeToCollectionIfMissing<IPartyClassificationType>(
            partyClassificationTypes,
            this.partyClassificationGroup?.classificationType,
          ),
        ),
      )
      .subscribe((partyClassificationTypes: IPartyClassificationType[]) =>
        this.partyClassificationTypesSharedCollection.set(partyClassificationTypes),
      );
  }
}
