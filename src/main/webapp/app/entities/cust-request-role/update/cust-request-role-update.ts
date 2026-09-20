import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { CustRequestService } from 'app/entities/cust-request/service/cust-request.service';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { CustRequestItemService } from 'app/entities/cust-request-item/service/cust-request-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestRole } from '../cust-request-role.model';
import { CustRequestRoleService } from '../service/cust-request-role.service';

import { CustRequestRoleFormGroup, CustRequestRoleFormService } from './cust-request-role-form.service';

@Component({
  selector: 'sys-cust-request-role-update',
  templateUrl: './cust-request-role-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustRequestRoleUpdate implements OnInit {
  readonly isSaving = signal(false);
  custRequestRole: ICustRequestRole | null = null;

  custRequestsSharedCollection = signal<ICustRequest[]>([]);
  custRequestItemsSharedCollection = signal<ICustRequestItem[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected custRequestRoleService = inject(CustRequestRoleService);
  protected custRequestRoleFormService = inject(CustRequestRoleFormService);
  protected custRequestService = inject(CustRequestService);
  protected custRequestItemService = inject(CustRequestItemService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustRequestRoleFormGroup = this.custRequestRoleFormService.createCustRequestRoleFormGroup();

  compareCustRequest = (o1: ICustRequest | null, o2: ICustRequest | null): boolean => this.custRequestService.compareCustRequest(o1, o2);

  compareCustRequestItem = (o1: ICustRequestItem | null, o2: ICustRequestItem | null): boolean =>
    this.custRequestItemService.compareCustRequestItem(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ custRequestRole }) => {
      this.custRequestRole = custRequestRole;
      if (custRequestRole) {
        this.updateForm(custRequestRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const custRequestRole = this.custRequestRoleFormService.getCustRequestRole(this.editForm);
    if (custRequestRole.id === null) {
      this.subscribeToSaveResponse(this.custRequestRoleService.create(custRequestRole));
    } else {
      this.subscribeToSaveResponse(this.custRequestRoleService.update(custRequestRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustRequestRole | null>): void {
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

  protected updateForm(custRequestRole: ICustRequestRole): void {
    this.custRequestRole = custRequestRole;
    this.custRequestRoleFormService.resetForm(this.editForm, custRequestRole);

    this.custRequestsSharedCollection.update(custRequests =>
      this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, custRequestRole.custRequest),
    );
    this.custRequestItemsSharedCollection.update(custRequestItems =>
      this.custRequestItemService.addCustRequestItemToCollectionIfMissing<ICustRequestItem>(
        custRequestItems,
        custRequestRole.custRequestItem,
      ),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, custRequestRole.party));
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, custRequestRole.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.custRequestService
      .query()
      .pipe(map((res: HttpResponse<ICustRequest[]>) => res.body ?? []))
      .pipe(
        map((custRequests: ICustRequest[]) =>
          this.custRequestService.addCustRequestToCollectionIfMissing<ICustRequest>(custRequests, this.custRequestRole?.custRequest),
        ),
      )
      .subscribe((custRequests: ICustRequest[]) => this.custRequestsSharedCollection.set(custRequests));

    this.custRequestItemService
      .query()
      .pipe(map((res: HttpResponse<ICustRequestItem[]>) => res.body ?? []))
      .pipe(
        map((custRequestItems: ICustRequestItem[]) =>
          this.custRequestItemService.addCustRequestItemToCollectionIfMissing<ICustRequestItem>(
            custRequestItems,
            this.custRequestRole?.custRequestItem,
          ),
        ),
      )
      .subscribe((custRequestItems: ICustRequestItem[]) => this.custRequestItemsSharedCollection.set(custRequestItems));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.custRequestRole?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.custRequestRole?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
