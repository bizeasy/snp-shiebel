import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacilityGroupType } from 'app/entities/facility-group-type/facility-group-type.model';
import { FacilityGroupTypeService } from 'app/entities/facility-group-type/service/facility-group-type.service';
import { IFacilityGroup } from '../facility-group.model';
import { FacilityGroupService } from '../service/facility-group.service';

import { FacilityGroupFormService } from './facility-group-form.service';
import { FacilityGroupUpdate } from './facility-group-update';

describe('FacilityGroup Management Update Component', () => {
  let comp: FacilityGroupUpdate;
  let fixture: ComponentFixture<FacilityGroupUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityGroupFormService: FacilityGroupFormService;
  let facilityGroupService: FacilityGroupService;
  let facilityGroupTypeService: FacilityGroupTypeService;

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

    fixture = TestBed.createComponent(FacilityGroupUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityGroupFormService = TestBed.inject(FacilityGroupFormService);
    facilityGroupService = TestBed.inject(FacilityGroupService);
    facilityGroupTypeService = TestBed.inject(FacilityGroupTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call FacilityGroupType query and add missing value', () => {
      const facilityGroup: IFacilityGroup = { id: 10499 };
      const facilityGroupType: IFacilityGroupType = { id: 7510 };
      facilityGroup.facilityGroupType = facilityGroupType;

      const facilityGroupTypeCollection: IFacilityGroupType[] = [{ id: 7510 }];
      vi.spyOn(facilityGroupTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityGroupTypeCollection })));
      const additionalFacilityGroupTypes = [facilityGroupType];
      const expectedCollection: IFacilityGroupType[] = [...additionalFacilityGroupTypes, ...facilityGroupTypeCollection];
      vi.spyOn(facilityGroupTypeService, 'addFacilityGroupTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityGroup });
      comp.ngOnInit();

      expect(facilityGroupTypeService.query).toHaveBeenCalled();
      expect(facilityGroupTypeService.addFacilityGroupTypeToCollectionIfMissing).toHaveBeenCalledWith(
        facilityGroupTypeCollection,
        ...additionalFacilityGroupTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityGroupTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call FacilityGroup query and add missing value', () => {
      const facilityGroup: IFacilityGroup = { id: 10499 };
      const parentFacilityGroup: IFacilityGroup = { id: 19761 };
      facilityGroup.parentFacilityGroup = parentFacilityGroup;

      const facilityGroupCollection: IFacilityGroup[] = [{ id: 19761 }];
      vi.spyOn(facilityGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityGroupCollection })));
      const additionalFacilityGroups = [parentFacilityGroup];
      const expectedCollection: IFacilityGroup[] = [...additionalFacilityGroups, ...facilityGroupCollection];
      vi.spyOn(facilityGroupService, 'addFacilityGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityGroup });
      comp.ngOnInit();

      expect(facilityGroupService.query).toHaveBeenCalled();
      expect(facilityGroupService.addFacilityGroupToCollectionIfMissing).toHaveBeenCalledWith(
        facilityGroupCollection,
        ...additionalFacilityGroups.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilityGroupsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facilityGroup: IFacilityGroup = { id: 10499 };
      const facilityGroupType: IFacilityGroupType = { id: 7510 };
      facilityGroup.facilityGroupType = facilityGroupType;
      const parentFacilityGroup: IFacilityGroup = { id: 19761 };
      facilityGroup.parentFacilityGroup = parentFacilityGroup;

      activatedRoute.data = of({ facilityGroup });
      comp.ngOnInit();

      expect(comp.facilityGroupTypesSharedCollection()).toContainEqual(facilityGroupType);
      expect(comp.facilityGroupsSharedCollection()).toContainEqual(parentFacilityGroup);
      expect(comp.facilityGroup).toEqual(facilityGroup);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroup>();
      const facilityGroup = { id: 19761 };
      vi.spyOn(facilityGroupFormService, 'getFacilityGroup').mockReturnValue(facilityGroup);
      vi.spyOn(facilityGroupService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityGroup);
      saveSubject.complete();

      // THEN
      expect(facilityGroupFormService.getFacilityGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityGroupService.update).toHaveBeenCalledWith(expect.objectContaining(facilityGroup));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroup>();
      const facilityGroup = { id: 19761 };
      vi.spyOn(facilityGroupFormService, 'getFacilityGroup').mockReturnValue({ id: null });
      vi.spyOn(facilityGroupService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityGroup);
      saveSubject.complete();

      // THEN
      expect(facilityGroupFormService.getFacilityGroup).toHaveBeenCalled();
      expect(facilityGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroup>();
      const facilityGroup = { id: 19761 };
      vi.spyOn(facilityGroupService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFacilityGroupType', () => {
      it('should forward to facilityGroupTypeService', () => {
        const entity = { id: 7510 };
        const entity2 = { id: 23372 };
        vi.spyOn(facilityGroupTypeService, 'compareFacilityGroupType');
        comp.compareFacilityGroupType(entity, entity2);
        expect(facilityGroupTypeService.compareFacilityGroupType).toHaveBeenCalledWith(entity, entity2);
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
