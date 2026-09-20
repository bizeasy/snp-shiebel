import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacilityType } from '../facility-type.model';
import { FacilityTypeService } from '../service/facility-type.service';

import { FacilityTypeFormService } from './facility-type-form.service';
import { FacilityTypeUpdate } from './facility-type-update';

describe('FacilityType Management Update Component', () => {
  let comp: FacilityTypeUpdate;
  let fixture: ComponentFixture<FacilityTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityTypeFormService: FacilityTypeFormService;
  let facilityTypeService: FacilityTypeService;

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

    fixture = TestBed.createComponent(FacilityTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityTypeFormService = TestBed.inject(FacilityTypeFormService);
    facilityTypeService = TestBed.inject(FacilityTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const facilityType: IFacilityType = { id: 4814 };

      activatedRoute.data = of({ facilityType });
      comp.ngOnInit();

      expect(comp.facilityType).toEqual(facilityType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityType>();
      const facilityType = { id: 18255 };
      vi.spyOn(facilityTypeFormService, 'getFacilityType').mockReturnValue(facilityType);
      vi.spyOn(facilityTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityType);
      saveSubject.complete();

      // THEN
      expect(facilityTypeFormService.getFacilityType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityTypeService.update).toHaveBeenCalledWith(expect.objectContaining(facilityType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityType>();
      const facilityType = { id: 18255 };
      vi.spyOn(facilityTypeFormService, 'getFacilityType').mockReturnValue({ id: null });
      vi.spyOn(facilityTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityType);
      saveSubject.complete();

      // THEN
      expect(facilityTypeFormService.getFacilityType).toHaveBeenCalled();
      expect(facilityTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityType>();
      const facilityType = { id: 18255 };
      vi.spyOn(facilityTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
