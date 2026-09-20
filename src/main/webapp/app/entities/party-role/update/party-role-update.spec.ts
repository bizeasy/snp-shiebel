import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IPartyRole } from '../party-role.model';
import { PartyRoleService } from '../service/party-role.service';

import { PartyRoleFormService } from './party-role-form.service';
import { PartyRoleUpdate } from './party-role-update';

describe('PartyRole Management Update Component', () => {
  let comp: PartyRoleUpdate;
  let fixture: ComponentFixture<PartyRoleUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyRoleFormService: PartyRoleFormService;
  let partyRoleService: PartyRoleService;
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

    fixture = TestBed.createComponent(PartyRoleUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyRoleFormService = TestBed.inject(PartyRoleFormService);
    partyRoleService = TestBed.inject(PartyRoleService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Party query and add missing value', () => {
      const partyRole: IPartyRole = { id: 29421 };
      const party: IParty = { id: 24527 };
      partyRole.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyRole });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const partyRole: IPartyRole = { id: 29421 };
      const roleType: IRoleType = { id: 17576 };
      partyRole.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyRole });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partyRole: IPartyRole = { id: 29421 };
      const party: IParty = { id: 24527 };
      partyRole.party = party;
      const roleType: IRoleType = { id: 17576 };
      partyRole.roleType = roleType;

      activatedRoute.data = of({ partyRole });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.partyRole).toEqual(partyRole);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRole>();
      const partyRole = { id: 18921 };
      vi.spyOn(partyRoleFormService, 'getPartyRole').mockReturnValue(partyRole);
      vi.spyOn(partyRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyRole);
      saveSubject.complete();

      // THEN
      expect(partyRoleFormService.getPartyRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyRoleService.update).toHaveBeenCalledWith(expect.objectContaining(partyRole));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRole>();
      const partyRole = { id: 18921 };
      vi.spyOn(partyRoleFormService, 'getPartyRole').mockReturnValue({ id: null });
      vi.spyOn(partyRoleService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyRole);
      saveSubject.complete();

      // THEN
      expect(partyRoleFormService.getPartyRole).toHaveBeenCalled();
      expect(partyRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRole>();
      const partyRole = { id: 18921 };
      vi.spyOn(partyRoleService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
