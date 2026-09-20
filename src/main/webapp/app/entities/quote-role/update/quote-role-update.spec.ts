import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IQuote } from 'app/entities/quote/quote.model';
import { QuoteService } from 'app/entities/quote/service/quote.service';
import { IQuoteItem } from 'app/entities/quote-item/quote-item.model';
import { QuoteItemService } from 'app/entities/quote-item/service/quote-item.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IQuoteRole } from '../quote-role.model';
import { QuoteRoleService } from '../service/quote-role.service';

import { QuoteRoleFormService } from './quote-role-form.service';
import { QuoteRoleUpdate } from './quote-role-update';

describe('QuoteRole Management Update Component', () => {
  let comp: QuoteRoleUpdate;
  let fixture: ComponentFixture<QuoteRoleUpdate>;
  let activatedRoute: ActivatedRoute;
  let quoteRoleFormService: QuoteRoleFormService;
  let quoteRoleService: QuoteRoleService;
  let quoteService: QuoteService;
  let quoteItemService: QuoteItemService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(QuoteRoleUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quoteRoleFormService = TestBed.inject(QuoteRoleFormService);
    quoteRoleService = TestBed.inject(QuoteRoleService);
    quoteService = TestBed.inject(QuoteService);
    quoteItemService = TestBed.inject(QuoteItemService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Quote query and add missing value', () => {
      const quoteRole: IQuoteRole = { id: 133 };
      const quote: IQuote = { id: 10209 };
      quoteRole.quote = quote;

      const quoteCollection: IQuote[] = [{ id: 10209 }];
      vi.spyOn(quoteService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteCollection })));
      const additionalQuotes = [quote];
      const expectedCollection: IQuote[] = [...additionalQuotes, ...quoteCollection];
      vi.spyOn(quoteService, 'addQuoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteRole });
      comp.ngOnInit();

      expect(quoteService.query).toHaveBeenCalled();
      expect(quoteService.addQuoteToCollectionIfMissing).toHaveBeenCalledWith(
        quoteCollection,
        ...additionalQuotes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quotesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call QuoteItem query and add missing value', () => {
      const quoteRole: IQuoteRole = { id: 133 };
      const quoteItem: IQuoteItem = { id: 28072 };
      quoteRole.quoteItem = quoteItem;

      const quoteItemCollection: IQuoteItem[] = [{ id: 28072 }];
      vi.spyOn(quoteItemService, 'query').mockReturnValue(of(new HttpResponse({ body: quoteItemCollection })));
      const additionalQuoteItems = [quoteItem];
      const expectedCollection: IQuoteItem[] = [...additionalQuoteItems, ...quoteItemCollection];
      vi.spyOn(quoteItemService, 'addQuoteItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteRole });
      comp.ngOnInit();

      expect(quoteItemService.query).toHaveBeenCalled();
      expect(quoteItemService.addQuoteItemToCollectionIfMissing).toHaveBeenCalledWith(
        quoteItemCollection,
        ...additionalQuoteItems.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.quoteItemsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const quoteRole: IQuoteRole = { id: 133 };
      const party: IParty = { id: 24527 };
      quoteRole.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteRole });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const quoteRole: IQuoteRole = { id: 133 };
      const roleType: IRoleType = { id: 17576 };
      quoteRole.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ quoteRole });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const quoteRole: IQuoteRole = { id: 133 };
      const quote: IQuote = { id: 10209 };
      quoteRole.quote = quote;
      const quoteItem: IQuoteItem = { id: 28072 };
      quoteRole.quoteItem = quoteItem;
      const party: IParty = { id: 24527 };
      quoteRole.party = party;
      const roleType: IRoleType = { id: 17576 };
      quoteRole.roleType = roleType;

      activatedRoute.data = of({ quoteRole });
      comp.ngOnInit();

      expect(comp.quotesSharedCollection()).toContainEqual(quote);
      expect(comp.quoteItemsSharedCollection()).toContainEqual(quoteItem);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.quoteRole).toEqual(quoteRole);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteRole>();
      const quoteRole = { id: 4674 };
      vi.spyOn(quoteRoleFormService, 'getQuoteRole').mockReturnValue(quoteRole);
      vi.spyOn(quoteRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteRole);
      saveSubject.complete();

      // THEN
      expect(quoteRoleFormService.getQuoteRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quoteRoleService.update).toHaveBeenCalledWith(expect.objectContaining(quoteRole));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteRole>();
      const quoteRole = { id: 4674 };
      vi.spyOn(quoteRoleFormService, 'getQuoteRole').mockReturnValue({ id: null });
      vi.spyOn(quoteRoleService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(quoteRole);
      saveSubject.complete();

      // THEN
      expect(quoteRoleFormService.getQuoteRole).toHaveBeenCalled();
      expect(quoteRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IQuoteRole>();
      const quoteRole = { id: 4674 };
      vi.spyOn(quoteRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quoteRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quoteRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuote', () => {
      it('should forward to quoteService', () => {
        const entity = { id: 10209 };
        const entity2 = { id: 23928 };
        vi.spyOn(quoteService, 'compareQuote');
        comp.compareQuote(entity, entity2);
        expect(quoteService.compareQuote).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuoteItem', () => {
      it('should forward to quoteItemService', () => {
        const entity = { id: 28072 };
        const entity2 = { id: 30045 };
        vi.spyOn(quoteItemService, 'compareQuoteItem');
        comp.compareQuoteItem(entity, entity2);
        expect(quoteItemService.compareQuoteItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoleType', () => {
      it('should forward to roleTypeService', () => {
        const entity = { id: 17576 };
        const entity2 = { id: 27052 };
        vi.spyOn(roleTypeService, 'compareRoleType');
        comp.compareRoleType(entity, entity2);
        expect(roleTypeService.compareRoleType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
