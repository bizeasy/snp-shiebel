import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { ILocationType } from 'app/entities/location-type/location-type.model';
import { LocationTypeService } from 'app/entities/location-type/service/location-type.service';
import { IFacilityLocation } from '../facility-location.model';
import { FacilityLocationService } from '../service/facility-location.service';

import { FacilityLocationFormService } from './facility-location-form.service';
import { FacilityLocationUpdate } from './facility-location-update';

describe('FacilityLocation Management Update Component', () => {
  let comp: FacilityLocationUpdate;
  let fixture: ComponentFixture<FacilityLocationUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityLocationFormService: FacilityLocationFormService;
  let facilityLocationService: FacilityLocationService;
  let facilityService: FacilityService;
  let locationTypeService: LocationTypeService;

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

    fixture = TestBed.createComponent(FacilityLocationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityLocationFormService = TestBed.inject(FacilityLocationFormService);
    facilityLocationService = TestBed.inject(FacilityLocationService);
    facilityService = TestBed.inject(FacilityService);
    locationTypeService = TestBed.inject(LocationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Facility query and add missing value', () => {
      const facilityLocation: IFacilityLocation = { id: 28655 };
      const facility: IFacility = { id: 4866 };
      facilityLocation.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityLocation });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call LocationType query and add missing value', () => {
      const facilityLocation: IFacilityLocation = { id: 28655 };
      const locationType: ILocationType = { id: 9674 };
      facilityLocation.locationType = locationType;

      const locationTypeCollection: ILocationType[] = [{ id: 9674 }];
      vi.spyOn(locationTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: locationTypeCollection })));
      const additionalLocationTypes = [locationType];
      const expectedCollection: ILocationType[] = [...additionalLocationTypes, ...locationTypeCollection];
      vi.spyOn(locationTypeService, 'addLocationTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityLocation });
      comp.ngOnInit();

      expect(locationTypeService.query).toHaveBeenCalled();
      expect(locationTypeService.addLocationTypeToCollectionIfMissing).toHaveBeenCalledWith(
        locationTypeCollection,
        ...additionalLocationTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.locationTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facilityLocation: IFacilityLocation = { id: 28655 };
      const facility: IFacility = { id: 4866 };
      facilityLocation.facility = facility;
      const locationType: ILocationType = { id: 9674 };
      facilityLocation.locationType = locationType;

      activatedRoute.data = of({ facilityLocation });
      comp.ngOnInit();

      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.locationTypesSharedCollection()).toContainEqual(locationType);
      expect(comp.facilityLocation).toEqual(facilityLocation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityLocation>();
      const facilityLocation = { id: 76 };
      vi.spyOn(facilityLocationFormService, 'getFacilityLocation').mockReturnValue(facilityLocation);
      vi.spyOn(facilityLocationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityLocation);
      saveSubject.complete();

      // THEN
      expect(facilityLocationFormService.getFacilityLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityLocationService.update).toHaveBeenCalledWith(expect.objectContaining(facilityLocation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityLocation>();
      const facilityLocation = { id: 76 };
      vi.spyOn(facilityLocationFormService, 'getFacilityLocation').mockReturnValue({ id: null });
      vi.spyOn(facilityLocationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityLocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityLocation);
      saveSubject.complete();

      // THEN
      expect(facilityLocationFormService.getFacilityLocation).toHaveBeenCalled();
      expect(facilityLocationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityLocation>();
      const facilityLocation = { id: 76 };
      vi.spyOn(facilityLocationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityLocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityLocationService.update).toHaveBeenCalled();
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

    describe('compareLocationType', () => {
      it('should forward to locationTypeService', () => {
        const entity = { id: 9674 };
        const entity2 = { id: 23011 };
        vi.spyOn(locationTypeService, 'compareLocationType');
        comp.compareLocationType(entity, entity2);
        expect(locationTypeService.compareLocationType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
