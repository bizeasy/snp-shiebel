import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGeoAssocType } from '../geo-assoc-type.model';
import { GeoAssocTypeService } from '../service/geo-assoc-type.service';

import { GeoAssocTypeFormService } from './geo-assoc-type-form.service';
import { GeoAssocTypeUpdate } from './geo-assoc-type-update';

describe('GeoAssocType Management Update Component', () => {
  let comp: GeoAssocTypeUpdate;
  let fixture: ComponentFixture<GeoAssocTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let geoAssocTypeFormService: GeoAssocTypeFormService;
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

    fixture = TestBed.createComponent(GeoAssocTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    geoAssocTypeFormService = TestBed.inject(GeoAssocTypeFormService);
    geoAssocTypeService = TestBed.inject(GeoAssocTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const geoAssocType: IGeoAssocType = { id: 15746 };

      activatedRoute.data = of({ geoAssocType });
      comp.ngOnInit();

      expect(comp.geoAssocType).toEqual(geoAssocType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoAssocType>();
      const geoAssocType = { id: 9608 };
      vi.spyOn(geoAssocTypeFormService, 'getGeoAssocType').mockReturnValue(geoAssocType);
      vi.spyOn(geoAssocTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoAssocType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoAssocType);
      saveSubject.complete();

      // THEN
      expect(geoAssocTypeFormService.getGeoAssocType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(geoAssocTypeService.update).toHaveBeenCalledWith(expect.objectContaining(geoAssocType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoAssocType>();
      const geoAssocType = { id: 9608 };
      vi.spyOn(geoAssocTypeFormService, 'getGeoAssocType').mockReturnValue({ id: null });
      vi.spyOn(geoAssocTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoAssocType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(geoAssocType);
      saveSubject.complete();

      // THEN
      expect(geoAssocTypeFormService.getGeoAssocType).toHaveBeenCalled();
      expect(geoAssocTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IGeoAssocType>();
      const geoAssocType = { id: 9608 };
      vi.spyOn(geoAssocTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoAssocType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(geoAssocTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
