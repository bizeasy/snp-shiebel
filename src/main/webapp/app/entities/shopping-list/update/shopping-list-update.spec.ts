import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { ShoppingListTypeService } from 'app/entities/shopping-list-type/service/shopping-list-type.service';
import { IShoppingListType } from 'app/entities/shopping-list-type/shopping-list-type.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { ShoppingListService } from '../service/shopping-list.service';
import { IShoppingList } from '../shopping-list.model';

import { ShoppingListFormService } from './shopping-list-form.service';
import { ShoppingListUpdate } from './shopping-list-update';

describe('ShoppingList Management Update Component', () => {
  let comp: ShoppingListUpdate;
  let fixture: ComponentFixture<ShoppingListUpdate>;
  let activatedRoute: ActivatedRoute;
  let shoppingListFormService: ShoppingListFormService;
  let shoppingListService: ShoppingListService;
  let shoppingListTypeService: ShoppingListTypeService;
  let partyService: PartyService;
  let statusService: StatusService;

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

    fixture = TestBed.createComponent(ShoppingListUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shoppingListFormService = TestBed.inject(ShoppingListFormService);
    shoppingListService = TestBed.inject(ShoppingListService);
    shoppingListTypeService = TestBed.inject(ShoppingListTypeService);
    partyService = TestBed.inject(PartyService);
    statusService = TestBed.inject(StatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ShoppingListType query and add missing value', () => {
      const shoppingList: IShoppingList = { id: 11571 };
      const shoppingListType: IShoppingListType = { id: 6209 };
      shoppingList.shoppingListType = shoppingListType;

      const shoppingListTypeCollection: IShoppingListType[] = [{ id: 6209 }];
      vi.spyOn(shoppingListTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: shoppingListTypeCollection })));
      const additionalShoppingListTypes = [shoppingListType];
      const expectedCollection: IShoppingListType[] = [...additionalShoppingListTypes, ...shoppingListTypeCollection];
      vi.spyOn(shoppingListTypeService, 'addShoppingListTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shoppingList });
      comp.ngOnInit();

      expect(shoppingListTypeService.query).toHaveBeenCalled();
      expect(shoppingListTypeService.addShoppingListTypeToCollectionIfMissing).toHaveBeenCalledWith(
        shoppingListTypeCollection,
        ...additionalShoppingListTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.shoppingListTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const shoppingList: IShoppingList = { id: 11571 };
      const party: IParty = { id: 24527 };
      shoppingList.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shoppingList });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const shoppingList: IShoppingList = { id: 11571 };
      const status: IStatus = { id: 2927 };
      shoppingList.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shoppingList });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const shoppingList: IShoppingList = { id: 11571 };
      const shoppingListType: IShoppingListType = { id: 6209 };
      shoppingList.shoppingListType = shoppingListType;
      const party: IParty = { id: 24527 };
      shoppingList.party = party;
      const status: IStatus = { id: 2927 };
      shoppingList.status = status;

      activatedRoute.data = of({ shoppingList });
      comp.ngOnInit();

      expect(comp.shoppingListTypesSharedCollection()).toContainEqual(shoppingListType);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.shoppingList).toEqual(shoppingList);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingList>();
      const shoppingList = { id: 18255 };
      vi.spyOn(shoppingListFormService, 'getShoppingList').mockReturnValue(shoppingList);
      vi.spyOn(shoppingListService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shoppingList);
      saveSubject.complete();

      // THEN
      expect(shoppingListFormService.getShoppingList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shoppingListService.update).toHaveBeenCalledWith(expect.objectContaining(shoppingList));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingList>();
      const shoppingList = { id: 18255 };
      vi.spyOn(shoppingListFormService, 'getShoppingList').mockReturnValue({ id: null });
      vi.spyOn(shoppingListService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(shoppingList);
      saveSubject.complete();

      // THEN
      expect(shoppingListFormService.getShoppingList).toHaveBeenCalled();
      expect(shoppingListService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IShoppingList>();
      const shoppingList = { id: 18255 };
      vi.spyOn(shoppingListService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shoppingList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shoppingListService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShoppingListType', () => {
      it('should forward to shoppingListTypeService', () => {
        const entity = { id: 6209 };
        const entity2 = { id: 25325 };
        vi.spyOn(shoppingListTypeService, 'compareShoppingListType');
        comp.compareShoppingListType(entity, entity2);
        expect(shoppingListTypeService.compareShoppingListType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
