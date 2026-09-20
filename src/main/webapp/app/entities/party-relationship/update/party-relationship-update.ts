import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPartyRelationshipType } from 'app/entities/party-relationship-type/party-relationship-type.model';
import { PartyRelationshipTypeService } from 'app/entities/party-relationship-type/service/party-relationship-type.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyRelationship } from '../party-relationship.model';
import { PartyRelationshipService } from '../service/party-relationship.service';

import { PartyRelationshipFormGroup, PartyRelationshipFormService } from './party-relationship-form.service';

@Component({
  selector: 'sys-party-relationship-update',
  templateUrl: './party-relationship-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyRelationshipUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyRelationship: IPartyRelationship | null = null;

  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);
  partyRelationshipTypesSharedCollection = signal<IPartyRelationshipType[]>([]);

  protected partyRelationshipService = inject(PartyRelationshipService);
  protected partyRelationshipFormService = inject(PartyRelationshipFormService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected partyRelationshipTypeService = inject(PartyRelationshipTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyRelationshipFormGroup = this.partyRelationshipFormService.createPartyRelationshipFormGroup();

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  comparePartyRelationshipType = (o1: IPartyRelationshipType | null, o2: IPartyRelationshipType | null): boolean =>
    this.partyRelationshipTypeService.comparePartyRelationshipType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyRelationship }) => {
      this.partyRelationship = partyRelationship;
      if (partyRelationship) {
        this.updateForm(partyRelationship);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyRelationship = this.partyRelationshipFormService.getPartyRelationship(this.editForm);
    if (partyRelationship.id === null) {
      this.subscribeToSaveResponse(this.partyRelationshipService.create(partyRelationship));
    } else {
      this.subscribeToSaveResponse(this.partyRelationshipService.update(partyRelationship));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyRelationship | null>): void {
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

  protected updateForm(partyRelationship: IPartyRelationship): void {
    this.partyRelationship = partyRelationship;
    this.partyRelationshipFormService.resetForm(this.editForm, partyRelationship);

    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, partyRelationship.partyFrom, partyRelationship.partyTo),
    );
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(
        roleTypes,
        partyRelationship.roleTypeFrom,
        partyRelationship.roleTypeTo,
      ),
    );
    this.partyRelationshipTypesSharedCollection.update(partyRelationshipTypes =>
      this.partyRelationshipTypeService.addPartyRelationshipTypeToCollectionIfMissing<IPartyRelationshipType>(
        partyRelationshipTypes,
        partyRelationship.relationshipType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(
            parties,
            this.partyRelationship?.partyFrom,
            this.partyRelationship?.partyTo,
          ),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(
            roleTypes,
            this.partyRelationship?.roleTypeFrom,
            this.partyRelationship?.roleTypeTo,
          ),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));

    this.partyRelationshipTypeService
      .query()
      .pipe(map((res: HttpResponse<IPartyRelationshipType[]>) => res.body ?? []))
      .pipe(
        map((partyRelationshipTypes: IPartyRelationshipType[]) =>
          this.partyRelationshipTypeService.addPartyRelationshipTypeToCollectionIfMissing<IPartyRelationshipType>(
            partyRelationshipTypes,
            this.partyRelationship?.relationshipType,
          ),
        ),
      )
      .subscribe((partyRelationshipTypes: IPartyRelationshipType[]) =>
        this.partyRelationshipTypesSharedCollection.set(partyRelationshipTypes),
      );
  }
}
