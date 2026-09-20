import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICustomTimePeriod } from '../custom-time-period.model';
import { CustomTimePeriodService } from '../service/custom-time-period.service';

import { CustomTimePeriodFormService } from './custom-time-period-form.service';
import { CustomTimePeriodUpdate } from './custom-time-period-update';

describe('CustomTimePeriod Management Update Component', () => {
  let comp: CustomTimePeriodUpdate;
  let fixture: ComponentFixture<CustomTimePeriodUpdate>;
  let activatedRoute: ActivatedRoute;
  let customTimePeriodFormService: CustomTimePeriodFormService;
  let customTimePeriodService: CustomTimePeriodService;

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

    fixture = TestBed.createComponent(CustomTimePeriodUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customTimePeriodFormService = TestBed.inject(CustomTimePeriodFormService);
    customTimePeriodService = TestBed.inject(CustomTimePeriodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const customTimePeriod: ICustomTimePeriod = { id: 17611 };

      activatedRoute.data = of({ customTimePeriod });
      comp.ngOnInit();

      expect(comp.customTimePeriod).toEqual(customTimePeriod);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomTimePeriod>();
      const customTimePeriod = { id: 19478 };
      vi.spyOn(customTimePeriodFormService, 'getCustomTimePeriod').mockReturnValue(customTimePeriod);
      vi.spyOn(customTimePeriodService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customTimePeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(customTimePeriod);
      saveSubject.complete();

      // THEN
      expect(customTimePeriodFormService.getCustomTimePeriod).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customTimePeriodService.update).toHaveBeenCalledWith(expect.objectContaining(customTimePeriod));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomTimePeriod>();
      const customTimePeriod = { id: 19478 };
      vi.spyOn(customTimePeriodFormService, 'getCustomTimePeriod').mockReturnValue({ id: null });
      vi.spyOn(customTimePeriodService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customTimePeriod: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(customTimePeriod);
      saveSubject.complete();

      // THEN
      expect(customTimePeriodFormService.getCustomTimePeriod).toHaveBeenCalled();
      expect(customTimePeriodService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICustomTimePeriod>();
      const customTimePeriod = { id: 19478 };
      vi.spyOn(customTimePeriodService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customTimePeriod });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customTimePeriodService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
