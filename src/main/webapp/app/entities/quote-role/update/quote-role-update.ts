import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { QuoteItemService } from 'app/entities/quote-item/service/quote-item.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IQuoteRole } from '../quote-role.model';
import { QuoteRoleService } from '../service/quote-role.service';

import { QuoteRoleFormGroup, QuoteRoleFormService } from './quote-role-form.service';

@Component({
  selector: 'sys-quote-role-update',
  templateUrl: './quote-role-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class QuoteRoleUpdate implements OnInit {
  readonly isSaving = signal(false);
  quoteRole: IQuoteRole | null = null;

  quotesSharedCollection = signal<IQuote[]>([]);
  quoteItemsSharedCollection = signal<IQuoteItem[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  roleTypesSharedCollection = signal<IRoleType[]>([]);

  protected quoteRoleService = inject(QuoteRoleService);
  protected quoteRoleFormService = inject(QuoteRoleFormService);
  protected quoteService = inject(QuoteService);
  protected quoteItemService = inject(QuoteItemService);
  protected partyService = inject(PartyService);
  protected roleTypeService = inject(RoleTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: QuoteRoleFormGroup = this.quoteRoleFormService.createQuoteRoleFormGroup();

  compareQuote = (o1: IQuote | null, o2: IQuote | null): boolean => this.quoteService.compareQuote(o1, o2);

  compareQuoteItem = (o1: IQuoteItem | null, o2: IQuoteItem | null): boolean => this.quoteItemService.compareQuoteItem(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareRoleType = (o1: IRoleType | null, o2: IRoleType | null): boolean => this.roleTypeService.compareRoleType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quoteRole }) => {
      this.quoteRole = quoteRole;
      if (quoteRole) {
        this.updateForm(quoteRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const quoteRole = this.quoteRoleFormService.getQuoteRole(this.editForm);
    if (quoteRole.id === null) {
      this.subscribeToSaveResponse(this.quoteRoleService.create(quoteRole));
    } else {
      this.subscribeToSaveResponse(this.quoteRoleService.update(quoteRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IQuoteRole | null>): void {
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

  protected updateForm(quoteRole: IQuoteRole): void {
    this.quoteRole = quoteRole;
    this.quoteRoleFormService.resetForm(this.editForm, quoteRole);

    this.quotesSharedCollection.update(quotes => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, quoteRole.quote));
    this.quoteItemsSharedCollection.update(quoteItems =>
      this.quoteItemService.addQuoteItemToCollectionIfMissing<IQuoteItem>(quoteItems, quoteRole.quoteItem),
    );
    this.partiesSharedCollection.update(parties => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, quoteRole.party));
    this.roleTypesSharedCollection.update(roleTypes =>
      this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, quoteRole.roleType),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.quoteService
      .query()
      .pipe(map((res: HttpResponse<IQuote[]>) => res.body ?? []))
      .pipe(map((quotes: IQuote[]) => this.quoteService.addQuoteToCollectionIfMissing<IQuote>(quotes, this.quoteRole?.quote)))
      .subscribe((quotes: IQuote[]) => this.quotesSharedCollection.set(quotes));

    this.quoteItemService
      .query()
      .pipe(map((res: HttpResponse<IQuoteItem[]>) => res.body ?? []))
      .pipe(
        map((quoteItems: IQuoteItem[]) =>
          this.quoteItemService.addQuoteItemToCollectionIfMissing<IQuoteItem>(quoteItems, this.quoteRole?.quoteItem),
        ),
      )
      .subscribe((quoteItems: IQuoteItem[]) => this.quoteItemsSharedCollection.set(quoteItems));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.quoteRole?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.roleTypeService
      .query()
      .pipe(map((res: HttpResponse<IRoleType[]>) => res.body ?? []))
      .pipe(
        map((roleTypes: IRoleType[]) =>
          this.roleTypeService.addRoleTypeToCollectionIfMissing<IRoleType>(roleTypes, this.quoteRole?.roleType),
        ),
      )
      .subscribe((roleTypes: IRoleType[]) => this.roleTypesSharedCollection.set(roleTypes));
  }
}
