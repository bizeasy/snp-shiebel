import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGeoType } from 'app/entities/geo-type/geo-type.model';
import { GeoTypeService } from 'app/entities/geo-type/service/geo-type.service';
import { IGeo } from '../geo.model';
import { GeoService } from '../service/geo.service';

import { GeoFormService } from './geo-form.service';
import { GeoUpdate } from './geo-update';

describe('Geo Management Update Component', () => {
  let comp: GeoUpdate;
  let fixture: ComponentFixture<GeoUpdate>;
  let activatedRoute: ActivatedRoute;
  let geoFormService: GeoFormService;
  let geoService: GeoService;
  let geoTypeService: GeoTypeService;

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

    fixture = TestBed.createComponent(GeoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    geoFormService = TestBed.inject(GeoFormService);
    geoService = TestBed.inject(GeoService);
    geoTypeService = TestBed.inject(GeoTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call GeoType query and add missing value', () => {
      const geo: IGeo = { id: 23406 };
      const geoType: IGeoType = { id: 1453 };
      geo.geoType = geoType;

      const geoTypeCollection: IGeoType[] = [{ id: 1453 }];
      vi.spyOn(geoTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: geoTypeCollection })));
      const additionalGeoTypes = [geoType];
      const expectedCollection: IGeoType[] = [...additionalGeoTypes, ...geoTypeCollection];
      vi.spyOn(geoTypeService, 'addGeoTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ geo });
      comp.ngOnInit();

      expect(geoTypeService.query).toHaveBeenCalled();
      expect(geoTypeService.addGeoTypeToCollectionIfMissing).toHaveBeenCalledWith(
        geoTypeCollection,
        ...additionalGeoTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.geoTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const geo: IGeo = { id: 23406 };
      const geoType: IGeoType = { id: 1453 };
      geo.geoType = geoType;

      activatedRoute.data = of({ geo });
      comp.ngOnInit();

      expect(comp.geoTypesSharedCollection()).toContainEqual(geoType);
      expect(comp.geo).toEqual(geo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeo>();
      const geo = { id: 16292 };
      vi.spyOn(geoFormService, 'getGeo').mockReturnValue(geo);
      vi.spyOn(geoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geo);
      saveSubject.complete();

      // THEN
      expect(geoFormService.getGeo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(geoService.update).toHaveBeenCalledWith(expect.objectContaining(geo));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeo>();
      const geo = { id: 16292 };
      vi.spyOn(geoFormService, 'getGeo').mockReturnValue({ id: null });
      vi.spyOn(geoService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geo);
      saveSubject.complete();

      // THEN
      expect(geoFormService.getGeo).toHaveBeenCalled();
      expect(geoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IGeo>();
      const geo = { id: 16292 };
      vi.spyOn(geoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(geoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGeoType', () => {
      it('should forward to geoTypeService', () => {
        const entity = { id: 1453 };
        const entity2 = { id: 7532 };
        vi.spyOn(geoTypeService, 'compareGeoType');
        comp.compareGeoType(entity, entity2);
        expect(geoTypeService.compareGeoType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
