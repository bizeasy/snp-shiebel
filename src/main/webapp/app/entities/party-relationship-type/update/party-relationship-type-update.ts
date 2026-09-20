import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPartyRelationshipType } from '../party-relationship-type.model';
import { PartyRelationshipTypeService } from '../service/party-relationship-type.service';

import { PartyRelationshipTypeFormGroup, PartyRelationshipTypeFormService } from './party-relationship-type-form.service';

@Component({
  selector: 'sys-party-relationship-type-update',
  templateUrl: './party-relationship-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PartyRelationshipTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  partyRelationshipType: IPartyRelationshipType | null = null;

  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected partyRelationshipTypeService = inject(PartyRelationshipTypeService);
  protected partyRelationshipTypeFormService = inject(PartyRelationshipTypeFormService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyRelationshipTypeFormGroup = this.partyRelationshipTypeFormService.createPartyRelationshipTypeFormGroup();

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyRelationshipType }) => {
      this.partyRelationshipType = partyRelationshipType;
      if (partyRelationshipType) {
        this.updateForm(partyRelationshipType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const partyRelationshipType = this.partyRelationshipTypeFormService.getPartyRelationshipType(this.editForm);
    if (partyRelationshipType.id === null) {
      this.subscribeToSaveResponse(this.partyRelationshipTypeService.create(partyRelationshipType));
    } else {
      this.subscribeToSaveResponse(this.partyRelationshipTypeService.update(partyRelationshipType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPartyRelationshipType | null>): void {
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

  protected updateForm(partyRelationshipType: IPartyRelationshipType): void {
    this.partyRelationshipType = partyRelationshipType;
    this.partyRelationshipTypeFormService.resetForm(this.editForm, partyRelationshipType);

    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(
        roleTypes,
        partyRelationshipType.roleTypeFrom,
        partyRelationshipType.roleTypeTo,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(
            roleTypes,
            this.partyRelationshipType?.roleTypeFrom,
            this.partyRelationshipType?.roleTypeTo,
          ),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
