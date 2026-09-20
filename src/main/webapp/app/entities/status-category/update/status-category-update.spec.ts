import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { StatusCategoryService } from '../service/status-category.service';
import { IStatusCategory } from '../status-category.model';

import { StatusCategoryFormService } from './status-category-form.service';
import { StatusCategoryUpdate } from './status-category-update';

describe('StatusCategory Management Update Component', () => {
  let comp: StatusCategoryUpdate;
  let fixture: ComponentFixture<StatusCategoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let statusCategoryFormService: StatusCategoryFormService;
  let statusCategoryService: StatusCategoryService;

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

    fixture = TestBed.createComponent(StatusCategoryUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusCategoryFormService = TestBed.inject(StatusCategoryFormService);
    statusCategoryService = TestBed.inject(StatusCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const statusCategory: IStatusCategory = { id: 21985 };

      activatedRoute.data = of({ statusCategory });
      comp.ngOnInit();

      expect(comp.statusCategory).toEqual(statusCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatusCategory>();
      const statusCategory = { id: 17785 };
      vi.spyOn(statusCategoryFormService, 'getStatusCategory').mockReturnValue(statusCategory);
      vi.spyOn(statusCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(statusCategory);
      saveSubject.complete();

      // THEN
      expect(statusCategoryFormService.getStatusCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(statusCategory));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatusCategory>();
      const statusCategory = { id: 17785 };
      vi.spyOn(statusCategoryFormService, 'getStatusCategory').mockReturnValue({ id: null });
      vi.spyOn(statusCategoryService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(statusCategory);
      saveSubject.complete();

      // THEN
      expect(statusCategoryFormService.getStatusCategory).toHaveBeenCalled();
      expect(statusCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IStatusCategory>();
      const statusCategory = { id: 17785 };
      vi.spyOn(statusCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
