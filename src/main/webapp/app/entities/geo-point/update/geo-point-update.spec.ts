import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGeoPoint } from '../geo-point.model';
import { GeoPointService } from '../service/geo-point.service';

import { GeoPointFormService } from './geo-point-form.service';
import { GeoPointUpdate } from './geo-point-update';

describe('GeoPoint Management Update Component', () => {
  let comp: GeoPointUpdate;
  let fixture: ComponentFixture<GeoPointUpdate>;
  let activatedRoute: ActivatedRoute;
  let geoPointFormService: GeoPointFormService;
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

    fixture = TestBed.createComponent(GeoPointUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    geoPointFormService = TestBed.inject(GeoPointFormService);
    geoPointService = TestBed.inject(GeoPointService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const geoPoint: IGeoPoint = { id: 8540 };

      activatedRoute.data = of({ geoPoint });
      comp.ngOnInit();

      expect(comp.geoPoint).toEqual(geoPoint);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoPoint>();
      const geoPoint = { id: 30719 };
      vi.spyOn(geoPointFormService, 'getGeoPoint').mockReturnValue(geoPoint);
      vi.spyOn(geoPointService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoPoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoPoint);
      saveSubject.complete();

      // THEN
      expect(geoPointFormService.getGeoPoint).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(geoPointService.update).toHaveBeenCalledWith(expect.objectContaining(geoPoint));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoPoint>();
      const geoPoint = { id: 30719 };
      vi.spyOn(geoPointFormService, 'getGeoPoint').mockReturnValue({ id: null });
      vi.spyOn(geoPointService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoPoint: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoPoint);
      saveSubject.complete();

      // THEN
      expect(geoPointFormService.getGeoPoint).toHaveBeenCalled();
      expect(geoPointService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoPoint>();
      const geoPoint = { id: 30719 };
      vi.spyOn(geoPointService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoPoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(geoPointService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
