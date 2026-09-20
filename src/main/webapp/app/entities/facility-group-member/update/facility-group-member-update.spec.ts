import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IFacilityGroup } from 'app/entities/facility-group/facility-group.model';
import { FacilityGroupService } from 'app/entities/facility-group/service/facility-group.service';
import { IFacilityGroupMember } from '../facility-group-member.model';
import { FacilityGroupMemberService } from '../service/facility-group-member.service';

import { FacilityGroupMemberFormService } from './facility-group-member-form.service';
import { FacilityGroupMemberUpdate } from './facility-group-member-update';

describe('FacilityGroupMember Management Update Component', () => {
  let comp: FacilityGroupMemberUpdate;
  let fixture: ComponentFixture<FacilityGroupMemberUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityGroupMemberFormService: FacilityGroupMemberFormService;
  let facilityGroupMemberService: FacilityGroupMemberService;
  let facilityService: FacilityService;
  let facilityGroupService: FacilityGroupService;

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

    fixture = TestBed.createComponent(FacilityGroupMemberUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityGroupMemberFormService = TestBed.inject(FacilityGroupMemberFormService);
    facilityGroupMemberService = TestBed.inject(FacilityGroupMemberService);
    facilityService = TestBed.inject(FacilityService);
    facilityGroupService = TestBed.inject(FacilityGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Facility query and add missing value', () => {
      const facilityGroupMember: IFacilityGroupMember = { id: 16206 };
      const facility: IFacility = { id: 4866 };
      facilityGroupMember.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityGroupMember });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FacilityGroup query and add missing value', () => {
      const facilityGroupMember: IFacilityGroupMember = { id: 16206 };
      const facilityGroup: IFacilityGroup = { id: 19761 };
      facilityGroupMember.facilityGroup = facilityGroup;

      const facilityGroupCollection: IFacilityGroup[] = [{ id: 19761 }];
      vi.spyOn(facilityGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityGroupCollection })));
      const additionalFacilityGroups = [facilityGroup];
      const expectedCollection: IFacilityGroup[] = [...additionalFacilityGroups, ...facilityGroupCollection];
      vi.spyOn(facilityGroupService, 'addFacilityGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityGroupMember });
      comp.ngOnInit();

      expect(facilityGroupService.query).toHaveBeenCalled();
      expect(facilityGroupService.addFacilityGroupToCollectionIfMissing).toHaveBeenCalledWith(
        facilityGroupCollection,
        ...additionalFacilityGroups.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityGroupsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facilityGroupMember: IFacilityGroupMember = { id: 16206 };
      const facility: IFacility = { id: 4866 };
      facilityGroupMember.facility = facility;
      const facilityGroup: IFacilityGroup = { id: 19761 };
      facilityGroupMember.facilityGroup = facilityGroup;

      activatedRoute.data = of({ facilityGroupMember });
      comp.ngOnInit();

      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.facilityGroupsSharedCollection()).toContainEqual(facilityGroup);
      expect(comp.facilityGroupMember).toEqual(facilityGroupMember);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroupMember>();
      const facilityGroupMember = { id: 26141 };
      vi.spyOn(facilityGroupMemberFormService, 'getFacilityGroupMember').mockReturnValue(facilityGroupMember);
      vi.spyOn(facilityGroupMemberService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroupMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityGroupMember);
      saveSubject.complete();

      // THEN
      expect(facilityGroupMemberFormService.getFacilityGroupMember).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityGroupMemberService.update).toHaveBeenCalledWith(expect.objectContaining(facilityGroupMember));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroupMember>();
      const facilityGroupMember = { id: 26141 };
      vi.spyOn(facilityGroupMemberFormService, 'getFacilityGroupMember').mockReturnValue({ id: null });
      vi.spyOn(facilityGroupMemberService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroupMember: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityGroupMember);
      saveSubject.complete();

      // THEN
      expect(facilityGroupMemberFormService.getFacilityGroupMember).toHaveBeenCalled();
      expect(facilityGroupMemberService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroupMember>();
      const facilityGroupMember = { id: 26141 };
      vi.spyOn(facilityGroupMemberService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroupMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityGroupMemberService.update).toHaveBeenCalled();
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

    describe('compareFacilityGroup', () => {
      it('should forward to facilityGroupService', () => {
        const entity = { id: 19761 };
        const entity2 = { id: 10499 };
        vi.spyOn(facilityGroupService, 'compareFacilityGroup');
        comp.compareFacilityGroup(entity, entity2);
        expect(facilityGroupService.compareFacilityGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
