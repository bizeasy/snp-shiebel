import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IReturnHeaderType } from '../return-header-type.model';
import { ReturnHeaderTypeService } from '../service/return-header-type.service';

import { ReturnHeaderTypeFormService } from './return-header-type-form.service';
import { ReturnHeaderTypeUpdate } from './return-header-type-update';

describe('ReturnHeaderType Management Update Component', () => {
  let comp: ReturnHeaderTypeUpdate;
  let fixture: ComponentFixture<ReturnHeaderTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let returnHeaderTypeFormService: ReturnHeaderTypeFormService;
  let returnHeaderTypeService: ReturnHeaderTypeService;

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

    fixture = TestBed.createComponent(ReturnHeaderTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    returnHeaderTypeFormService = TestBed.inject(ReturnHeaderTypeFormService);
    returnHeaderTypeService = TestBed.inject(ReturnHeaderTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const returnHeaderType: IReturnHeaderType = { id: 26920 };

      activatedRoute.data = of({ returnHeaderType });
      comp.ngOnInit();

      expect(comp.returnHeaderType).toEqual(returnHeaderType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnHeaderType>();
      const returnHeaderType = { id: 14220 };
      vi.spyOn(returnHeaderTypeFormService, 'getReturnHeaderType').mockReturnValue(returnHeaderType);
      vi.spyOn(returnHeaderTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnHeaderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnHeaderType);
      saveSubject.complete();

      // THEN
      expect(returnHeaderTypeFormService.getReturnHeaderType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(returnHeaderTypeService.update).toHaveBeenCalledWith(expect.objectContaining(returnHeaderType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnHeaderType>();
      const returnHeaderType = { id: 14220 };
      vi.spyOn(returnHeaderTypeFormService, 'getReturnHeaderType').mockReturnValue({ id: null });
      vi.spyOn(returnHeaderTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnHeaderType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(returnHeaderType);
      saveSubject.complete();

      // THEN
      expect(returnHeaderTypeFormService.getReturnHeaderType).toHaveBeenCalled();
      expect(returnHeaderTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IReturnHeaderType>();
      const returnHeaderType = { id: 14220 };
      vi.spyOn(returnHeaderTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ returnHeaderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(returnHeaderTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
