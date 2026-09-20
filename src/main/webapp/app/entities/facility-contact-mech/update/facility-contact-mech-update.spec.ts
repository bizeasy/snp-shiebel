import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { ContactMechPurposeService } from 'app/entities/contact-mech-purpose/service/contact-mech-purpose.service';
import { IFacility } from 'app/entities/facility/facility.model';
import { FacilityService } from 'app/entities/facility/service/facility.service';
import { IFacilityContactMech } from '../facility-contact-mech.model';
import { FacilityContactMechService } from '../service/facility-contact-mech.service';

import { FacilityContactMechFormService } from './facility-contact-mech-form.service';
import { FacilityContactMechUpdate } from './facility-contact-mech-update';

describe('FacilityContactMech Management Update Component', () => {
  let comp: FacilityContactMechUpdate;
  let fixture: ComponentFixture<FacilityContactMechUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityContactMechFormService: FacilityContactMechFormService;
  let facilityContactMechService: FacilityContactMechService;
  let facilityService: FacilityService;
  let contactMechService: ContactMechService;
  let contactMechPurposeService: ContactMechPurposeService;

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

    fixture = TestBed.createComponent(FacilityContactMechUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityContactMechFormService = TestBed.inject(FacilityContactMechFormService);
    facilityContactMechService = TestBed.inject(FacilityContactMechService);
    facilityService = TestBed.inject(FacilityService);
    contactMechService = TestBed.inject(ContactMechService);
    contactMechPurposeService = TestBed.inject(ContactMechPurposeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Facility query and add missing value', () => {
      const facilityContactMech: IFacilityContactMech = { id: 18977 };
      const facility: IFacility = { id: 4866 };
      facilityContactMech.facility = facility;

      const facilityCollection: IFacility[] = [{ id: 4866 }];
      vi.spyOn(facilityService, 'query').mockReturnValue(of(new HttpResponse({ body: facilityCollection })));
      const additionalFacilities = [facility];
      const expectedCollection: IFacility[] = [...additionalFacilities, ...facilityCollection];
      vi.spyOn(facilityService, 'addFacilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityContactMech });
      comp.ngOnInit();

      expect(facilityService.query).toHaveBeenCalled();
      expect(facilityService.addFacilityToCollectionIfMissing).toHaveBeenCalledWith(
        facilityCollection,
        ...additionalFacilities.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.facilitiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMech query and add missing value', () => {
      const facilityContactMech: IFacilityContactMech = { id: 18977 };
      const contactMech: IContactMech = { id: 5019 };
      facilityContactMech.contactMech = contactMech;

      const contactMechCollection: IContactMech[] = [{ id: 5019 }];
      vi.spyOn(contactMechService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechCollection })));
      const additionalContactMeches = [contactMech];
      const expectedCollection: IContactMech[] = [...additionalContactMeches, ...contactMechCollection];
      vi.spyOn(contactMechService, 'addContactMechToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityContactMech });
      comp.ngOnInit();

      expect(contactMechService.query).toHaveBeenCalled();
      expect(contactMechService.addContactMechToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechCollection,
        ...additionalContactMeches.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ContactMechPurpose query and add missing value', () => {
      const facilityContactMech: IFacilityContactMech = { id: 18977 };
      const contactMechPurpose: IContactMechPurpose = { id: 25033 };
      facilityContactMech.contactMechPurpose = contactMechPurpose;

      const contactMechPurposeCollection: IContactMechPurpose[] = [{ id: 25033 }];
      vi.spyOn(contactMechPurposeService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechPurposeCollection })));
      const additionalContactMechPurposes = [contactMechPurpose];
      const expectedCollection: IContactMechPurpose[] = [...additionalContactMechPurposes, ...contactMechPurposeCollection];
      vi.spyOn(contactMechPurposeService, 'addContactMechPurposeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facilityContactMech });
      comp.ngOnInit();

      expect(contactMechPurposeService.query).toHaveBeenCalled();
      expect(contactMechPurposeService.addContactMechPurposeToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechPurposeCollection,
        ...additionalContactMechPurposes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechPurposesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facilityContactMech: IFacilityContactMech = { id: 18977 };
      const facility: IFacility = { id: 4866 };
      facilityContactMech.facility = facility;
      const contactMech: IContactMech = { id: 5019 };
      facilityContactMech.contactMech = contactMech;
      const contactMechPurpose: IContactMechPurpose = { id: 25033 };
      facilityContactMech.contactMechPurpose = contactMechPurpose;

      activatedRoute.data = of({ facilityContactMech });
      comp.ngOnInit();

      expect(comp.facilitiesSharedCollection()).toContainEqual(facility);
      expect(comp.contactMechesSharedCollection()).toContainEqual(contactMech);
      expect(comp.contactMechPurposesSharedCollection()).toContainEqual(contactMechPurpose);
      expect(comp.facilityContactMech).toEqual(facilityContactMech);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityContactMech>();
      const facilityContactMech = { id: 21991 };
      vi.spyOn(facilityContactMechFormService, 'getFacilityContactMech').mockReturnValue(facilityContactMech);
      vi.spyOn(facilityContactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityContactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityContactMech);
      saveSubject.complete();

      // THEN
      expect(facilityContactMechFormService.getFacilityContactMech).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityContactMechService.update).toHaveBeenCalledWith(expect.objectContaining(facilityContactMech));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityContactMech>();
      const facilityContactMech = { id: 21991 };
      vi.spyOn(facilityContactMechFormService, 'getFacilityContactMech').mockReturnValue({ id: null });
      vi.spyOn(facilityContactMechService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityContactMech: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityContactMech);
      saveSubject.complete();

      // THEN
      expect(facilityContactMechFormService.getFacilityContactMech).toHaveBeenCalled();
      expect(facilityContactMechService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityContactMech>();
      const facilityContactMech = { id: 21991 };
      vi.spyOn(facilityContactMechService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityContactMech });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityContactMechService.update).toHaveBeenCalled();
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

    describe('compareContactMech', () => {
      it('should forward to contactMechService', () => {
        const entity = { id: 5019 };
        const entity2 = { id: 29939 };
        vi.spyOn(contactMechService, 'compareContactMech');
        comp.compareContactMech(entity, entity2);
        expect(contactMechService.compareContactMech).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareContactMechPurpose', () => {
      it('should forward to contactMechPurposeService', () => {
        const entity = { id: 25033 };
        const entity2 = { id: 10745 };
        vi.spyOn(contactMechPurposeService, 'compareContactMechPurpose');
        comp.compareContactMechPurpose(entity, entity2);
        expect(contactMechPurposeService.compareContactMechPurpose).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
