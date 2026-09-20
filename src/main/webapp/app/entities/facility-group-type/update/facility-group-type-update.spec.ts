import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFacilityGroupType } from '../facility-group-type.model';
import { FacilityGroupTypeService } from '../service/facility-group-type.service';

import { FacilityGroupTypeFormService } from './facility-group-type-form.service';
import { FacilityGroupTypeUpdate } from './facility-group-type-update';

describe('FacilityGroupType Management Update Component', () => {
  let comp: FacilityGroupTypeUpdate;
  let fixture: ComponentFixture<FacilityGroupTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let facilityGroupTypeFormService: FacilityGroupTypeFormService;
  let facilityGroupTypeService: FacilityGroupTypeService;

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

    fixture = TestBed.createComponent(FacilityGroupTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facilityGroupTypeFormService = TestBed.inject(FacilityGroupTypeFormService);
    facilityGroupTypeService = TestBed.inject(FacilityGroupTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const facilityGroupType: IFacilityGroupType = { id: 23372 };

      activatedRoute.data = of({ facilityGroupType });
      comp.ngOnInit();

      expect(comp.facilityGroupType).toEqual(facilityGroupType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroupType>();
      const facilityGroupType = { id: 7510 };
      vi.spyOn(facilityGroupTypeFormService, 'getFacilityGroupType').mockReturnValue(facilityGroupType);
      vi.spyOn(facilityGroupTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroupType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityGroupType);
      saveSubject.complete();

      // THEN
      expect(facilityGroupTypeFormService.getFacilityGroupType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facilityGroupTypeService.update).toHaveBeenCalledWith(expect.objectContaining(facilityGroupType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroupType>();
      const facilityGroupType = { id: 7510 };
      vi.spyOn(facilityGroupTypeFormService, 'getFacilityGroupType').mockReturnValue({ id: null });
      vi.spyOn(facilityGroupTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroupType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(facilityGroupType);
      saveSubject.complete();

      // THEN
      expect(facilityGroupTypeFormService.getFacilityGroupType).toHaveBeenCalled();
      expect(facilityGroupTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFacilityGroupType>();
      const facilityGroupType = { id: 7510 };
      vi.spyOn(facilityGroupTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facilityGroupType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facilityGroupTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
