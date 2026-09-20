import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGeo } from 'app/entities/geo/geo.model';
import { GeoService } from 'app/entities/geo/service/geo.service';
import { IGeoAssocType } from 'app/entities/geo-assoc-type/geo-assoc-type.model';
import { GeoAssocTypeService } from 'app/entities/geo-assoc-type/service/geo-assoc-type.service';
import { IGeoAssoc } from '../geo-assoc.model';
import { GeoAssocService } from '../service/geo-assoc.service';

import { GeoAssocFormService } from './geo-assoc-form.service';
import { GeoAssocUpdate } from './geo-assoc-update';

describe('GeoAssoc Management Update Component', () => {
  let comp: GeoAssocUpdate;
  let fixture: ComponentFixture<GeoAssocUpdate>;
  let activatedRoute: ActivatedRoute;
  let geoAssocFormService: GeoAssocFormService;
  let geoAssocService: GeoAssocService;
  let geoService: GeoService;
  let geoAssocTypeService: GeoAssocTypeService;

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

    fixture = TestBed.createComponent(GeoAssocUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    geoAssocFormService = TestBed.inject(GeoAssocFormService);
    geoAssocService = TestBed.inject(GeoAssocService);
    geoService = TestBed.inject(GeoService);
    geoAssocTypeService = TestBed.inject(GeoAssocTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Geo query and add missing value', () => {
      const geoAssoc: IGeoAssoc = { id: 16124 };
      const geo: IGeo = { id: 16292 };
      geoAssoc.geo = geo;
      const geoTo: IGeo = { id: 16292 };
      geoAssoc.geoTo = geoTo;

      const geoCollection: IGeo[] = [{ id: 16292 }];
      vi.spyOn(geoService, 'query').mockReturnValue(of(new HttpResponse({ body: geoCollection })));
      const additionalGeos = [geo, geoTo];
      const expectedCollection: IGeo[] = [...additionalGeos, ...geoCollection];
      vi.spyOn(geoService, 'addGeoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ geoAssoc });
      comp.ngOnInit();

      expect(geoService.query).toHaveBeenCalled();
      expect(geoService.addGeoToCollectionIfMissing).toHaveBeenCalledWith(
        geoCollection,
        ...additionalGeos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.geosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call GeoAssocType query and add missing value', () => {
      const geoAssoc: IGeoAssoc = { id: 16124 };
      const geoAssocType: IGeoAssocType = { id: 9608 };
      geoAssoc.geoAssocType = geoAssocType;

      const geoAssocTypeCollection: IGeoAssocType[] = [{ id: 9608 }];
      vi.spyOn(geoAssocTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: geoAssocTypeCollection })));
      const additionalGeoAssocTypes = [geoAssocType];
      const expectedCollection: IGeoAssocType[] = [...additionalGeoAssocTypes, ...geoAssocTypeCollection];
      vi.spyOn(geoAssocTypeService, 'addGeoAssocTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ geoAssoc });
      comp.ngOnInit();

      expect(geoAssocTypeService.query).toHaveBeenCalled();
      expect(geoAssocTypeService.addGeoAssocTypeToCollectionIfMissing).toHaveBeenCalledWith(
        geoAssocTypeCollection,
        ...additionalGeoAssocTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.geoAssocTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const geoAssoc: IGeoAssoc = { id: 16124 };
      const geo: IGeo = { id: 16292 };
      geoAssoc.geo = geo;
      const geoTo: IGeo = { id: 16292 };
      geoAssoc.geoTo = geoTo;
      const geoAssocType: IGeoAssocType = { id: 9608 };
      geoAssoc.geoAssocType = geoAssocType;

      activatedRoute.data = of({ geoAssoc });
      comp.ngOnInit();

      expect(comp.geosSharedCollection()).toContainEqual(geo);
      expect(comp.geosSharedCollection()).toContainEqual(geoTo);
      expect(comp.geoAssocTypesSharedCollection()).toContainEqual(geoAssocType);
      expect(comp.geoAssoc).toEqual(geoAssoc);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoAssoc>();
      const geoAssoc = { id: 5823 };
      vi.spyOn(geoAssocFormService, 'getGeoAssoc').mockReturnValue(geoAssoc);
      vi.spyOn(geoAssocService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoAssoc });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoAssoc);
      saveSubject.complete();

      // THEN
      expect(geoAssocFormService.getGeoAssoc).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(geoAssocService.update).toHaveBeenCalledWith(expect.objectContaining(geoAssoc));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoAssoc>();
      const geoAssoc = { id: 5823 };
      vi.spyOn(geoAssocFormService, 'getGeoAssoc').mockReturnValue({ id: null });
      vi.spyOn(geoAssocService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoAssoc: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoAssoc);
      saveSubject.complete();

      // THEN
      expect(geoAssocFormService.getGeoAssoc).toHaveBeenCalled();
      expect(geoAssocService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoAssoc>();
      const geoAssoc = { id: 5823 };
      vi.spyOn(geoAssocService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoAssoc });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(geoAssocService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGeo', () => {
      it('should forward to geoService', () => {
        const entity = { id: 16292 };
        const entity2 = { id: 23406 };
        vi.spyOn(geoService, 'compareGeo');
        comp.compareGeo(entity, entity2);
        expect(geoService.compareGeo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGeoAssocType', () => {
      it('should forward to geoAssocTypeService', () => {
        const entity = { id: 9608 };
        const entity2 = { id: 15746 };
        vi.spyOn(geoAssocTypeService, 'compareGeoAssocType');
        comp.compareGeoAssocType(entity, entity2);
        expect(geoAssocTypeService.compareGeoAssocType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
