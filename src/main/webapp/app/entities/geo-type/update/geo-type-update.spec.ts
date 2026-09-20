import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGeoType } from '../geo-type.model';
import { GeoTypeService } from '../service/geo-type.service';

import { GeoTypeFormService } from './geo-type-form.service';
import { GeoTypeUpdate } from './geo-type-update';

describe('GeoType Management Update Component', () => {
  let comp: GeoTypeUpdate;
  let fixture: ComponentFixture<GeoTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let geoTypeFormService: GeoTypeFormService;
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

    fixture = TestBed.createComponent(GeoTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    geoTypeFormService = TestBed.inject(GeoTypeFormService);
    geoTypeService = TestBed.inject(GeoTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const geoType: IGeoType = { id: 7532 };

      activatedRoute.data = of({ geoType });
      comp.ngOnInit();

      expect(comp.geoType).toEqual(geoType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoType>();
      const geoType = { id: 1453 };
      vi.spyOn(geoTypeFormService, 'getGeoType').mockReturnValue(geoType);
      vi.spyOn(geoTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoType);
      saveSubject.complete();

      // THEN
      expect(geoTypeFormService.getGeoType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(geoTypeService.update).toHaveBeenCalledWith(expect.objectContaining(geoType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoType>();
      const geoType = { id: 1453 };
      vi.spyOn(geoTypeFormService, 'getGeoType').mockReturnValue({ id: null });
      vi.spyOn(geoTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoType);
      saveSubject.complete();

      // THEN
      expect(geoTypeFormService.getGeoType).toHaveBeenCalled();
      expect(geoTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoType>();
      const geoType = { id: 1453 };
      vi.spyOn(geoTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(geoTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
