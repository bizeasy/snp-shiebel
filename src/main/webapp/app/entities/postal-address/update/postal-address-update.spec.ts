import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IGeo } from 'app/entities/geo/geo.model';
import { GeoService } from 'app/entities/geo/service/geo.service';
import { IGeoPoint } from 'app/entities/geo-point/geo-point.model';
import { GeoPointService } from 'app/entities/geo-point/service/geo-point.service';
import { IPostalAddress } from '../postal-address.model';
import { PostalAddressService } from '../service/postal-address.service';

import { PostalAddressFormService } from './postal-address-form.service';
import { PostalAddressUpdate } from './postal-address-update';

describe('PostalAddress Management Update Component', () => {
  let comp: PostalAddressUpdate;
  let fixture: ComponentFixture<PostalAddressUpdate>;
  let activatedRoute: ActivatedRoute;
  let postalAddressFormService: PostalAddressFormService;
  let postalAddressService: PostalAddressService;
  let contactMechService: ContactMechService;
  let geoService: GeoService;
  let geoPointService: GeoPointService;

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

    fixture = TestBed.createComponent(PostalAddressUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    postalAddressFormService = TestBed.inject(PostalAddressFormService);
    postalAddressService = TestBed.inject(PostalAddressService);
    contactMechService = TestBed.inject(ContactMechService);
    geoService = TestBed.inject(GeoService);
    geoPointService = TestBed.inject(GeoPointService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ContactMech query and add missing value', () => {
      const postalAddress: IPostalAddress = { id: 32221 };
      const contactMech: IContactMech = { id: 5019 };
      postalAddress.contactMech = contactMech;

      const contactMechCollection: IContactMech[] = [{ id: 5019 }];
      vi.spyOn(contactMechService, 'query').mockReturnValue(of(new HttpResponse({ body: contactMechCollection })));
      const additionalContactMeches = [contactMech];
      const expectedCollection: IContactMech[] = [...additionalContactMeches, ...contactMechCollection];
      vi.spyOn(contactMechService, 'addContactMechToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ postalAddress });
      comp.ngOnInit();

      expect(contactMechService.query).toHaveBeenCalled();
      expect(contactMechService.addContactMechToCollectionIfMissing).toHaveBeenCalledWith(
        contactMechCollection,
        ...additionalContactMeches.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.contactMechesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Geo query and add missing value', () => {
      const postalAddress: IPostalAddress = { id: 32221 };
      const state: IGeo = { id: 16292 };
      postalAddress.state = state;
      const pincode: IGeo = { id: 16292 };
      postalAddress.pincode = pincode;
      const country: IGeo = { id: 16292 };
      postalAddress.country = country;

      const geoCollection: IGeo[] = [{ id: 16292 }];
      vi.spyOn(geoService, 'query').mockReturnValue(of(new HttpResponse({ body: geoCollection })));
      const additionalGeos = [state, pincode, country];
      const expectedCollection: IGeo[] = [...additionalGeos, ...geoCollection];
      vi.spyOn(geoService, 'addGeoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ postalAddress });
      comp.ngOnInit();

      expect(geoService.query).toHaveBeenCalled();
      expect(geoService.addGeoToCollectionIfMissing).toHaveBeenCalledWith(
        geoCollection,
        ...additionalGeos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.geosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call GeoPoint query and add missing value', () => {
      const postalAddress: IPostalAddress = { id: 32221 };
      const geoPoint: IGeoPoint = { id: 30719 };
      postalAddress.geoPoint = geoPoint;

      const geoPointCollection: IGeoPoint[] = [{ id: 30719 }];
      vi.spyOn(geoPointService, 'query').mockReturnValue(of(new HttpResponse({ body: geoPointCollection })));
      const additionalGeoPoints = [geoPoint];
      const expectedCollection: IGeoPoint[] = [...additionalGeoPoints, ...geoPointCollection];
      vi.spyOn(geoPointService, 'addGeoPointToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ postalAddress });
      comp.ngOnInit();

      expect(geoPointService.query).toHaveBeenCalled();
      expect(geoPointService.addGeoPointToCollectionIfMissing).toHaveBeenCalledWith(
        geoPointCollection,
        ...additionalGeoPoints.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.geoPointsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const postalAddress: IPostalAddress = { id: 32221 };
      const contactMech: IContactMech = { id: 5019 };
      postalAddress.contactMech = contactMech;
      const state: IGeo = { id: 16292 };
      postalAddress.state = state;
      const pincode: IGeo = { id: 16292 };
      postalAddress.pincode = pincode;
      const country: IGeo = { id: 16292 };
      postalAddress.country = country;
      const geoPoint: IGeoPoint = { id: 30719 };
      postalAddress.geoPoint = geoPoint;

      activatedRoute.data = of({ postalAddress });
      comp.ngOnInit();

      expect(comp.contactMechesSharedCollection()).toContainEqual(contactMech);
      expect(comp.geosSharedCollection()).toContainEqual(state);
      expect(comp.geosSharedCollection()).toContainEqual(pincode);
      expect(comp.geosSharedCollection()).toContainEqual(country);
      expect(comp.geoPointsSharedCollection()).toContainEqual(geoPoint);
      expect(comp.postalAddress).toEqual(postalAddress);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPostalAddress>();
      const postalAddress = { id: 25687 };
      vi.spyOn(postalAddressFormService, 'getPostalAddress').mockReturnValue(postalAddress);
      vi.spyOn(postalAddressService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ postalAddress });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(postalAddress);
      saveSubject.complete();

      // THEN
      expect(postalAddressFormService.getPostalAddress).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(postalAddressService.update).toHaveBeenCalledWith(expect.objectContaining(postalAddress));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPostalAddress>();
      const postalAddress = { id: 25687 };
      vi.spyOn(postalAddressFormService, 'getPostalAddress').mockReturnValue({ id: null });
      vi.spyOn(postalAddressService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ postalAddress: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(postalAddress);
      saveSubject.complete();

      // THEN
      expect(postalAddressFormService.getPostalAddress).toHaveBeenCalled();
      expect(postalAddressService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPostalAddress>();
      const postalAddress = { id: 25687 };
      vi.spyOn(postalAddressService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ postalAddress });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(postalAddressService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContactMech', () => {
      it('should forward to contactMechService', () => {
        const entity = { id: 5019 };
        const entity2 = { id: 29939 };
        vi.spyOn(contactMechService, 'compareContactMech');
        comp.compareContactMech(entity, entity2);
        expect(contactMechService.compareContactMech).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGeo', () => {
      it('should forward to geoService', () => {
        const entity = { id: 16292 };
        const entity2 = { id: 23406 };
        vi.spyOn(geoService, 'compareGeo');
        comp.compareGeo(entity, entity2);
        expect(geoService.compareGeo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGeoPoint', () => {
      it('should forward to geoPointService', () => {
        const entity = { id: 30719 };
        const entity2 = { id: 8540 };
        vi.spyOn(geoPointService, 'compareGeoPoint');
        comp.compareGeoPoint(entity, entity2);
        expect(geoPointService.compareGeoPoint).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
