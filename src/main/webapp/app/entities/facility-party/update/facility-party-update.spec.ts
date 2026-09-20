import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { RoleTypeService } from 'app/entities/role-type/service/role-type.service';
import { IFacilityParty } from '../facility-party.model';
import { FacilityPartyService } from '../service/facility-party.service';

import { FacilityPartyFormService } from './facility-party-form.service';
import { FacilityPartyUpdate } from './facility-party-update';

describe('FacilityParty Management Update Component', () => {
  let comp: FacilityPartyUpdate;
  let fixture: ComponentFixture<FacilityPartyUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityPartyFormService: FacilityPartyFormService;
  let facilityPartyService: FacilityPartyService;
  let facilityService: FacilityService;
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

    fixture = TestBed.createComponent(FacilityPartyUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityPartyFormService = TestBed.inject(FacilityPartyFormService);
    facilityPartyService = TestBed.inject(FacilityPartyService);
    facilityService = TestBed.inject(FacilityService);
    partyService = TestBed.inject(PartyService);
    roleTypeService = TestBed.inject(RoleTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Facility query and add missing value', () => {
      const facilityParty: IFacilityParty = { id: 15320 };
      const facility: IFacility = { id: 4866 };
      facilityParty.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityParty });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Party query and add missing value', () => {
      const facilityParty: IFacilityParty = { id: 15320 };
      const party: IParty = { id: 24527 };
      facilityParty.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityParty });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call RoleType query and add missing value', () => {
      const facilityParty: IFacilityParty = { id: 15320 };
      const roleType: IRoleType = { id: 17576 };
      facilityParty.roleType = roleType;

      const roleTypeCollection: IRoleType[] = [{ id: 17576 }];
      vi.spyOn(roleTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roleTypeCollection })));
      const additionalRoleTypes = [roleType];
      const expectedCollection: IRoleType[] = [...additionalRoleTypes, ...roleTypeCollection];
      vi.spyOn(roleTypeService, 'addRoleTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityParty });
      comp.ngOnInit();

      expect(roleTypeService.query).toHaveBeenCalled();
      expect(roleTypeService.addRoleTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roleTypeCollection,
        ...additionalRoleTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.roleTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facilityParty: IFacilityParty = { id: 15320 };
      const facility: IFacility = { id: 4866 };
      facilityParty.facility = facility;
      const party: IParty = { id: 24527 };
      facilityParty.party = party;
      const roleType: IRoleType = { id: 17576 };
      facilityParty.roleType = roleType;

      activatedRoute.data = of({ facilityParty });
      comp.ngOnInit();

      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.roleTypesSharedCollection()).toContainEqual(roleType);
      expect(comp.facilityParty).toEqual(facilityParty);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityParty>();
      const facilityParty = { id: 9556 };
      vi.spyOn(facilityPartyFormService, 'getFacilityParty').mockReturnValue(facilityParty);
      vi.spyOn(facilityPartyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityParty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityParty);
      saveSubject.complete();

      // THEN
      expect(facilityPartyFormService.getFacilityParty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityPartyService.update).toHaveBeenCalledWith(expect.objectContaining(facilityParty));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityParty>();
      const facilityParty = { id: 9556 };
      vi.spyOn(facilityPartyFormService, 'getFacilityParty').mockReturnValue({ id: null });
      vi.spyOn(facilityPartyService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityParty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityParty);
      saveSubject.complete();

      // THEN
      expect(facilityPartyFormService.getFacilityParty).toHaveBeenCalled();
      expect(facilityPartyService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityParty>();
      const facilityParty = { id: 9556 };
      vi.spyOn(facilityPartyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityParty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityPartyService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFacility', () => {
      it('should forward to facilityService', () => {
        const entity = { id: 4866 };
        const entity2 = { id: 6958 };
        vi.spyOn(facilityService, 'compareFacility');
        comp.compareFacility(entity, entity2);
        expect(facilityService.compareFacility).toHaveBeenCalledWith(entity, entity2);
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
