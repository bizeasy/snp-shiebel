import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IPartyRelationshipType } from 'app/entities/party-relationship-type/party-relationship-type.model';
import { PartyRelationshipTypeService } from 'app/entities/party-relationship-type/service/party-relationship-type.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IPartyRelationship } from '../party-relationship.model';
import { PartyRelationshipService } from '../service/party-relationship.service';

import { PartyRelationshipFormService } from './party-relationship-form.service';
import { PartyRelationshipUpdate } from './party-relationship-update';

describe('PartyRelationship Management Update Component', () => {
  let comp: PartyRelationshipUpdate;
  let fixture: ComponentFixture<PartyRelationshipUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyRelationshipFormService: PartyRelationshipFormService;
  let partyRelationshipService: PartyRelationshipService;
  let partyService: PartyService;
  let roleTypeService: RoleTypeService;
  let partyRelationshipTypeService: PartyRelationshipTypeService;

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

    fixture = TestBed.createComponent(PartyRelationshipUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyRelationshipFormService = TestBed.inject(PartyRelationshipFormService);
    partyRelationshipService = TestBed.inject(PartyRelationshipService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);
    partyRelationshipTypeService = TestBed.inject(PartyRelationshipTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Party query and add missing value', () => {
      const partyRelationship: IPartyRelationship = { id: 31647 };
      const partyFrom: IParty = { id: 24527 };
      partyRelationship.partyFrom = partyFrom;
      const partyTo: IParty = { id: 24527 };
      partyRelationship.partyTo = partyTo;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [partyFrom, partyTo];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyRelationship });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const partyRelationship: IPartyRelationship = { id: 31647 };
      const roleTypeFrom: IRoleType = { id: 17576 };
      partyRelationship.roleTypeFrom = roleTypeFrom;
      const roleTypeTo: IRoleType = { id: 17576 };
      partyRelationship.roleTypeTo = roleTypeTo;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleTypeFrom, roleTypeTo];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyRelationship });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PartyRelationshipType query and add missing value', () => {
      const partyRelationship: IPartyRelationship = { id: 31647 };
      const relationshipType: IPartyRelationshipType = { id: 13343 };
      partyRelationship.relationshipType = relationshipType;

      const partyRelationshipTypeCollection: IPartyRelationshipType[] = [{ id: 13343 }];
      vi.spyOn(partyRelationshipTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: partyRelationshipTypeCollection })));
      const additionalPartyRelationshipTypes = [relationshipType];
      const expectedCollection: IPartyRelationshipType[] = [...additionalPartyRelationshipTypes, ...partyRelationshipTypeCollection];
      vi.spyOn(partyRelationshipTypeService, 'addPartyRelationshipTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partyRelationship });
      comp.ngOnInit();

      expect(partyRelationshipTypeService.query).toHaveBeenCalled();
      expect(partyRelationshipTypeService.addPartyRelationshipTypeToCollectionIfMissing).toHaveBeenCalledWith(
        partyRelationshipTypeCollection,
        ...additionalPartyRelationshipTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partyRelationshipTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partyRelationship: IPartyRelationship = { id: 31647 };
      const partyFrom: IParty = { id: 24527 };
      partyRelationship.partyFrom = partyFrom;
      const partyTo: IParty = { id: 24527 };
      partyRelationship.partyTo = partyTo;
      const roleTypeFrom: IRoleType = { id: 17576 };
      partyRelationship.roleTypeFrom = roleTypeFrom;
      const roleTypeTo: IRoleType = { id: 17576 };
      partyRelationship.roleTypeTo = roleTypeTo;
      const relationshipType: IPartyRelationshipType = { id: 13343 };
      partyRelationship.relationshipType = relationshipType;

      activatedRoute.data = of({ partyRelationship });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection()).toContainEqual(partyFrom);
      expect(comp.partiesSharedCollection()).toContainEqual(partyTo);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleTypeFrom);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleTypeTo);
      expect(comp.partyRelationshipTypesSharedCollection()).toContainEqual(relationshipType);
      expect(comp.partyRelationship).toEqual(partyRelationship);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRelationship>();
      const partyRelationship = { id: 18196 };
      vi.spyOn(partyRelationshipFormService, 'getPartyRelationship').mockReturnValue(partyRelationship);
      vi.spyOn(partyRelationshipService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRelationship });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyRelationship);
      saveSubject.complete();

      // THEN
      expect(partyRelationshipFormService.getPartyRelationship).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyRelationshipService.update).toHaveBeenCalledWith(expect.objectContaining(partyRelationship));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRelationship>();
      const partyRelationship = { id: 18196 };
      vi.spyOn(partyRelationshipFormService, 'getPartyRelationship').mockReturnValue({ id: null });
      vi.spyOn(partyRelationshipService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRelationship: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partyRelationship);
      saveSubject.complete();

      // THEN
      expect(partyRelationshipFormService.getPartyRelationship).toHaveBeenCalled();
      expect(partyRelationshipService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartyRelationship>();
      const partyRelationship = { id: 18196 };
      vi.spyOn(partyRelationshipService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partyRelationship });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyRelationshipService.update).toHaveBeenCalled();
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

    describe('comparePartyRelationshipType', () => {
      it('should forward to partyRelationshipTypeService', () => {
        const entity = { id: 13343 };
        const entity2 = { id: 5094 };
        vi.spyOn(partyRelationshipTypeService, 'comparePartyRelationshipType');
        comp.comparePartyRelationshipType(entity, entity2);
        expect(partyRelationshipTypeService.comparePartyRelationshipType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
