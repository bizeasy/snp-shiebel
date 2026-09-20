import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { ShoppingListTypeService } from 'app/entities/shopping-list-type/service/shopping-list-type.service';
import { IShoppingListType } from 'app/entities/shopping-list-type/shopping-list-type.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShoppingListService } from '../service/shopping-list.service';
import { IShoppingList } from '../shopping-list.model';

import { ShoppingListFormGroup, ShoppingListFormService } from './shopping-list-form.service';

@Component({
  selector: 'sys-shopping-list-update',
  templateUrl: './shopping-list-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShoppingListUpdate implements OnInit {
  readonly isSaving = signal(false);
  shoppingList: IShoppingList | null = null;

  shoppingListTypesSharedCollection = signal<IShoppingListType[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);

  protected shoppingListService = inject(ShoppingListService);
  protected shoppingListFormService = inject(ShoppingListFormService);
  protected shoppingListTypeService = inject(ShoppingListTypeService);
  protected partyService = inject(PartyService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShoppingListFormGroup = this.shoppingListFormService.createShoppingListFormGroup();

  compareShoppingListType = (o1: IShoppingListType | null, o2: IShoppingListType | null): boolean =>
    this.shoppingListTypeService.compareShoppingListType(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shoppingList }) => {
      this.shoppingList = shoppingList;
      if (shoppingList) {
        this.updateForm(shoppingList);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shoppingList = this.shoppingListFormService.getShoppingList(this.editForm);
    if (shoppingList.id === null) {
      this.subscribeToSaveResponse(this.shoppingListService.create(shoppingList));
    } else {
      this.subscribeToSaveResponse(this.shoppingListService.update(shoppingList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShoppingList | null>): void {
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

  protected updateForm(shoppingList: IShoppingList): void {
    this.shoppingList = shoppingList;
    this.shoppingListFormService.resetForm(this.editForm, shoppingList);

    this.shoppingListTypesSharedCollection.update(shoppingListTypes =>
      this.shoppingListTypeService.addShoppingListTypeToCollectionIfMissing<IShoppingListType>(
        shoppingListTypes,
        shoppingList.shoppingListType,
      ),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, shoppingList.party));
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, shoppingList.status),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shoppingListTypeService
      .query()
      .pipe(map((res: HttpResponse<IShoppingListType[]>) => res.body ?? []))
      .pipe(
        map((shoppingListTypes: IShoppingListType[]) =>
          this.shoppingListTypeService.addShoppingListTypeToCollectionIfMissing<IShoppingListType>(
            shoppingListTypes,
            this.shoppingList?.shoppingListType,
          ),
        ),
      )
      .subscribe((shoppingListTypes: IShoppingListType[]) => this.shoppingListTypesSharedCollection.set(shoppingListTypes));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.shoppingList?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.shoppingList?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
