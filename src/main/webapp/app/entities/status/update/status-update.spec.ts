import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { StatusCategoryService } from 'app/entities/status-category/service/status-category.service';
import { IStatusCategory } from 'app/entities/status-category/status-category.model';
import { StatusService } from '../service/status.service';
import { IStatus } from '../status.model';

import { StatusFormService } from './status-form.service';
import { StatusUpdate } from './status-update';

describe('Status Management Update Component', () => {
  let comp: StatusUpdate;
  let fixture: ComponentFixture<StatusUpdate>;
  let activatedRoute: ActivatedRoute;
  let statusFormService: StatusFormService;
  let statusService: StatusService;
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

    fixture = TestBed.createComponent(StatusUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusFormService = TestBed.inject(StatusFormService);
    statusService = TestBed.inject(StatusService);
    statusCategoryService = TestBed.inject(StatusCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call StatusCategory query and add missing value', () => {
      const status: IStatus = { id: 22202 };
      const category: IStatusCategory = { id: 17785 };
      status.category = category;

      const statusCategoryCollection: IStatusCategory[] = [{ id: 17785 }];
      vi.spyOn(statusCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCategoryCollection })));
      const additionalStatusCategories = [category];
      const expectedCollection: IStatusCategory[] = [...additionalStatusCategories, ...statusCategoryCollection];
      vi.spyOn(statusCategoryService, 'addStatusCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(statusCategoryService.query).toHaveBeenCalled();
      expect(statusCategoryService.addStatusCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        statusCategoryCollection,
        ...additionalStatusCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const status: IStatus = { id: 22202 };
      const category: IStatusCategory = { id: 17785 };
      status.category = category;

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(comp.statusCategoriesSharedCollection()).toContainEqual(category);
      expect(comp.status).toEqual(status);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatus>();
      const status = { id: 2927 };
      vi.spyOn(statusFormService, 'getStatus').mockReturnValue(status);
      vi.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(status);
      saveSubject.complete();

      // THEN
      expect(statusFormService.getStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusService.update).toHaveBeenCalledWith(expect.objectContaining(status));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IStatus>();
      const status = { id: 2927 };
      vi.spyOn(statusFormService, 'getStatus').mockReturnValue({ id: null });
      vi.spyOn(statusService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(status);
      saveSubject.complete();

      // THEN
      expect(statusFormService.getStatus).toHaveBeenCalled();
      expect(statusService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IStatus>();
      const status = { id: 2927 };
      vi.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStatusCategory', () => {
      it('should forward to statusCategoryService', () => {
        const entity = { id: 17785 };
        const entity2 = { id: 21985 };
        vi.spyOn(statusCategoryService, 'compareStatusCategory');
        comp.compareStatusCategory(entity, entity2);
        expect(statusCategoryService.compareStatusCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
