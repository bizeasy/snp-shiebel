import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILocationType } from '../location-type.model';
import { LocationTypeService } from '../service/location-type.service';

import { LocationTypeFormService } from './location-type-form.service';
import { LocationTypeUpdate } from './location-type-update';

describe('LocationType Management Update Component', () => {
  let comp: LocationTypeUpdate;
  let fixture: ComponentFixture<LocationTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let locationTypeFormService: LocationTypeFormService;
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

    fixture = TestBed.createComponent(LocationTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    locationTypeFormService = TestBed.inject(LocationTypeFormService);
    locationTypeService = TestBed.inject(LocationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const locationType: ILocationType = { id: 23011 };

      activatedRoute.data = of({ locationType });
      comp.ngOnInit();

      expect(comp.locationType).toEqual(locationType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILocationType>();
      const locationType = { id: 9674 };
      vi.spyOn(locationTypeFormService, 'getLocationType').mockReturnValue(locationType);
      vi.spyOn(locationTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(locationType);
      saveSubject.complete();

      // THEN
      expect(locationTypeFormService.getLocationType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(locationTypeService.update).toHaveBeenCalledWith(expect.objectContaining(locationType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILocationType>();
      const locationType = { id: 9674 };
      vi.spyOn(locationTypeFormService, 'getLocationType').mockReturnValue({ id: null });
      vi.spyOn(locationTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locationType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(locationType);
      saveSubject.complete();

      // THEN
      expect(locationTypeFormService.getLocationType).toHaveBeenCalled();
      expect(locationTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ILocationType>();
      const locationType = { id: 9674 };
      vi.spyOn(locationTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(locationTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
