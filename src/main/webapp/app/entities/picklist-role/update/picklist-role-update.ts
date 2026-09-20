import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPickList } from 'app/entities/pick-list/pick-list.model';
import { PickListService } from 'app/entities/pick-list/service/pick-list.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPicklistRole } from '../picklist-role.model';
import { PicklistRoleService } from '../service/picklist-role.service';

import { PicklistRoleFormGroup, PicklistRoleFormService } from './picklist-role-form.service';

@Component({
  selector: 'sys-picklist-role-update',
  templateUrl: './picklist-role-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PicklistRoleUpdate implements OnInit {
  readonly isSaving = signal(false);
  picklistRole: IPicklistRole | null = null;

  pickListsSharedCollection = signal<IPickList[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected picklistRoleService = inject(PicklistRoleService);
  protected picklistRoleFormService = inject(PicklistRoleFormService);
  protected pickListService = inject(PickListService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PicklistRoleFormGroup = this.picklistRoleFormService.createPicklistRoleFormGroup();

  comparePickList = (o1: IPickList | null, o2: IPickList | null): boolean => this.pickListService.comparePickList(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ picklistRole }) => {
      this.picklistRole = picklistRole;
      if (picklistRole) {
        this.updateForm(picklistRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const picklistRole = this.picklistRoleFormService.getPicklistRole(this.editForm);
    if (picklistRole.id === null) {
      this.subscribeToSaveResponse(this.picklistRoleService.create(picklistRole));
    } else {
      this.subscribeToSaveResponse(this.picklistRoleService.update(picklistRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPicklistRole | null>): void {
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

  protected updateForm(picklistRole: IPicklistRole): void {
    this.picklistRole = picklistRole;
    this.picklistRoleFormService.resetForm(this.editForm, picklistRole);

    this.pickListsSharedCollection.update(pickLists =>
      this.pickListService.addPickListToCollectionIfMissing<IPickList>(pickLists, picklistRole.pickList),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, picklistRole.party));
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, picklistRole.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pickListService
      .query()
      .pipe(map((res: HttpResponse<IPickList[]>) => res.body ?? []))
      .pipe(
        map((pickLists: IPickList[]) =>
          this.pickListService.addPickListToCollectionIfMissing<IPickList>(pickLists, this.picklistRole?.pickList),
        ),
      )
      .subscribe((pickLists: IPickList[]) => this.pickListsSharedCollection.set(pickLists));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.picklistRole?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.picklistRole?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
