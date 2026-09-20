import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPickList } from 'app/entities/pick-list/pick-list.model';
import { PickListService } from 'app/entities/pick-list/service/pick-list.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IPicklistRole } from '../picklist-role.model';
import { PicklistRoleService } from '../service/picklist-role.service';

import { PicklistRoleFormService } from './picklist-role-form.service';
import { PicklistRoleUpdate } from './picklist-role-update';

describe('PicklistRole Management Update Component', () => {
  let comp: PicklistRoleUpdate;
  let fixture: ComponentFixture<PicklistRoleUpdate>;
  let activatedRoute: ActivatedRoute;
  let picklistRoleFormService: PicklistRoleFormService;
  let picklistRoleService: PicklistRoleService;
  let pickListService: PickListService;
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

    fixture = TestBed.createComponent(PicklistRoleUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    picklistRoleFormService = TestBed.inject(PicklistRoleFormService);
    picklistRoleService = TestBed.inject(PicklistRoleService);
    pickListService = TestBed.inject(PickListService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PickList query and add missing value', () => {
      const picklistRole: IPicklistRole = { id: 28969 };
      const pickList: IPickList = { id: 15448 };
      picklistRole.pickList = pickList;

      const pickListCollection: IPickList[] = [{ id: 15448 }];
      vi.spyOn(pickListService, 'query').mockReturnValue(of(new HttpResponse({ body: pickListCollection })));
      const additionalPickLists = [pickList];
      const expectedCollection: IPickList[] = [...additionalPickLists, ...pickListCollection];
      vi.spyOn(pickListService, 'addPickListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ picklistRole });
      comp.ngOnInit();

      expect(pickListService.query).toHaveBeenCalled();
      expect(pickListService.addPickListToCollectionIfMissing).toHaveBeenCalledWith(
        pickListCollection,
        ...additionalPickLists.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.pickListsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const picklistRole: IPicklistRole = { id: 28969 };
      const party: IParty = { id: 24527 };
      picklistRole.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ picklistRole });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const picklistRole: IPicklistRole = { id: 28969 };
      const roleType: IRoleType = { id: 17576 };
      picklistRole.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ picklistRole });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const picklistRole: IPicklistRole = { id: 28969 };
      const pickList: IPickList = { id: 15448 };
      picklistRole.pickList = pickList;
      const party: IParty = { id: 24527 };
      picklistRole.party = party;
      const roleType: IRoleType = { id: 17576 };
      picklistRole.roleType = roleType;

      activatedRoute.data = of({ picklistRole });
      comp.ngOnInit();

      expect(comp.pickListsSharedCollection()).toContainEqual(pickList);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.picklistRole).toEqual(picklistRole);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPicklistRole>();
      const picklistRole = { id: 6650 };
      vi.spyOn(picklistRoleFormService, 'getPicklistRole').mockReturnValue(picklistRole);
      vi.spyOn(picklistRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picklistRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(picklistRole);
      saveSubject.complete();

      // THEN
      expect(picklistRoleFormService.getPicklistRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(picklistRoleService.update).toHaveBeenCalledWith(expect.objectContaining(picklistRole));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPicklistRole>();
      const picklistRole = { id: 6650 };
      vi.spyOn(picklistRoleFormService, 'getPicklistRole').mockReturnValue({ id: null });
      vi.spyOn(picklistRoleService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picklistRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(picklistRole);
      saveSubject.complete();

      // THEN
      expect(picklistRoleFormService.getPicklistRole).toHaveBeenCalled();
      expect(picklistRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPicklistRole>();
      const picklistRole = { id: 6650 };
      vi.spyOn(picklistRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picklistRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(picklistRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePickList', () => {
      it('should forward to pickListService', () => {
        const entity = { id: 15448 };
        const entity2 = { id: 863 };
        vi.spyOn(pickListService, 'comparePickList');
        comp.comparePickList(entity, entity2);
        expect(pickListService.comparePickList).toHaveBeenCalledWith(entity, entity2);
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
