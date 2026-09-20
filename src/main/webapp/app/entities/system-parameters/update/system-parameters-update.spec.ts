import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { SystemParametersService } from '../service/system-parameters.service';
import { ISystemParameters } from '../system-parameters.model';

import { SystemParametersFormService } from './system-parameters-form.service';
import { SystemParametersUpdate } from './system-parameters-update';

describe('SystemParameters Management Update Component', () => {
  let comp: SystemParametersUpdate;
  let fixture: ComponentFixture<SystemParametersUpdate>;
  let activatedRoute: ActivatedRoute;
  let systemParametersFormService: SystemParametersFormService;
  let systemParametersService: SystemParametersService;

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

    fixture = TestBed.createComponent(SystemParametersUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    systemParametersFormService = TestBed.inject(SystemParametersFormService);
    systemParametersService = TestBed.inject(SystemParametersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const systemParameters: ISystemParameters = { id: 594 };

      activatedRoute.data = of({ systemParameters });
      comp.ngOnInit();

      expect(comp.systemParameters).toEqual(systemParameters);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISystemParameters>();
      const systemParameters = { id: 7684 };
      vi.spyOn(systemParametersFormService, 'getSystemParameters').mockReturnValue(systemParameters);
      vi.spyOn(systemParametersService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemParameters });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(systemParameters);
      saveSubject.complete();

      // THEN
      expect(systemParametersFormService.getSystemParameters).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(systemParametersService.update).toHaveBeenCalledWith(expect.objectContaining(systemParameters));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISystemParameters>();
      const systemParameters = { id: 7684 };
      vi.spyOn(systemParametersFormService, 'getSystemParameters').mockReturnValue({ id: null });
      vi.spyOn(systemParametersService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemParameters: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(systemParameters);
      saveSubject.complete();

      // THEN
      expect(systemParametersFormService.getSystemParameters).toHaveBeenCalled();
      expect(systemParametersService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ISystemParameters>();
      const systemParameters = { id: 7684 };
      vi.spyOn(systemParametersService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemParameters });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(systemParametersService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
