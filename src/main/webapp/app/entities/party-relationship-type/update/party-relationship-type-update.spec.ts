import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IPartyRelationshipType } from '../party-relationship-type.model';
import { PartyRelationshipTypeService } from '../service/party-relationship-type.service';

import { PartyRelationshipTypeFormService } from './party-relationship-type-form.service';
import { PartyRelationshipTypeUpdate } from './party-relationship-type-update';

describe('PartyRelationshipType Management Update Component', () => {
  let comp: PartyRelationshipTypeUpdate;
  let fixture: ComponentFixture<PartyRelationshipTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyRelationshipTypeFormService: PartyRelationshipTypeFormService;
  let partyRelationshipTypeService: PartyRelationshipTypeService;
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

    fixture = TestBed.createComponent(PartyRelationshipTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyRelationshipTypeFormService = TestBed.inject(PartyRelationshipTypeFormService);
    partyRelationshipTypeService = TestBed.inject(PartyRelationshipTypeService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call RoleType query and add missing value', () => {
      const partyRelationshipType: IPartyRelationshipType = { id: 5094 };
      const roleTypeFrom: IRoleType = { id: 17576 };
      partyRelationshipType.roleTypeFrom = roleTypeFrom;
      const roleTypeTo: IRoleType = { id: 17576 };
      partyRelationshipType.roleTypeTo = roleTypeTo;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleTypeFrom, roleTypeTo];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyRelationshipType });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partyRelationshipType: IPartyRelationshipType = { id: 5094 };
      const roleTypeFrom: IRoleType = { id: 17576 };
      partyRelationshipType.roleTypeFrom = roleTypeFrom;
      const roleTypeTo: IRoleType = { id: 17576 };
      partyRelationshipType.roleTypeTo = roleTypeTo;

      activatedRoute.data = of({ partyRelationshipType });
      comp.ngOnInit();

      expect(comp.roleTypesSharedCollection()).toContainEqual(roleTypeFrom);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleTypeTo);
      expect(comp.partyRelationshipType).toEqual(partyRelationshipType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRelationshipType>();
      const partyRelationshipType = { id: 13343 };
      vi.spyOn(partyRelationshipTypeFormService, 'getPartyRelationshipType').mockReturnValue(partyRelationshipType);
      vi.spyOn(partyRelationshipTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRelationshipType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyRelationshipType);
      saveSubject.complete();

      // THEN
      expect(partyRelationshipTypeFormService.getPartyRelationshipType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyRelationshipTypeService.update).toHaveBeenCalledWith(expect.objectContaining(partyRelationshipType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRelationshipType>();
      const partyRelationshipType = { id: 13343 };
      vi.spyOn(partyRelationshipTypeFormService, 'getPartyRelationshipType').mockReturnValue({ id: null });
      vi.spyOn(partyRelationshipTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRelationshipType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyRelationshipType);
      saveSubject.complete();

      // THEN
      expect(partyRelationshipTypeFormService.getPartyRelationshipType).toHaveBeenCalled();
      expect(partyRelationshipTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRelationshipType>();
      const partyRelationshipType = { id: 13343 };
      vi.spyOn(partyRelationshipTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRelationshipType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyRelationshipTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
