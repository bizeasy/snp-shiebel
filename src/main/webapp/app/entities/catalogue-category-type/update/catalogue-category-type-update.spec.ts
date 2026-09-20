import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICatalogueCategoryType } from '../catalogue-category-type.model';
import { CatalogueCategoryTypeService } from '../service/catalogue-category-type.service';

import { CatalogueCategoryTypeFormService } from './catalogue-category-type-form.service';
import { CatalogueCategoryTypeUpdate } from './catalogue-category-type-update';

describe('CatalogueCategoryType Management Update Component', () => {
  let comp: CatalogueCategoryTypeUpdate;
  let fixture: ComponentFixture<CatalogueCategoryTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let catalogueCategoryTypeFormService: CatalogueCategoryTypeFormService;
  let catalogueCategoryTypeService: CatalogueCategoryTypeService;

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

    fixture = TestBed.createComponent(CatalogueCategoryTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogueCategoryTypeFormService = TestBed.inject(CatalogueCategoryTypeFormService);
    catalogueCategoryTypeService = TestBed.inject(CatalogueCategoryTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const catalogueCategoryType: ICatalogueCategoryType = { id: 16331 };

      activatedRoute.data = of({ catalogueCategoryType });
      comp.ngOnInit();

      expect(comp.catalogueCategoryType).toEqual(catalogueCategoryType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogueCategoryType>();
      const catalogueCategoryType = { id: 27421 };
      vi.spyOn(catalogueCategoryTypeFormService, 'getCatalogueCategoryType').mockReturnValue(catalogueCategoryType);
      vi.spyOn(catalogueCategoryTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogueCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(catalogueCategoryType);
      saveSubject.complete();

      // THEN
      expect(catalogueCategoryTypeFormService.getCatalogueCategoryType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogueCategoryTypeService.update).toHaveBeenCalledWith(expect.objectContaining(catalogueCategoryType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogueCategoryType>();
      const catalogueCategoryType = { id: 27421 };
      vi.spyOn(catalogueCategoryTypeFormService, 'getCatalogueCategoryType').mockReturnValue({ id: null });
      vi.spyOn(catalogueCategoryTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogueCategoryType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(catalogueCategoryType);
      saveSubject.complete();

      // THEN
      expect(catalogueCategoryTypeFormService.getCatalogueCategoryType).toHaveBeenCalled();
      expect(catalogueCategoryTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogueCategoryType>();
      const catalogueCategoryType = { id: 27421 };
      vi.spyOn(catalogueCategoryTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogueCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogueCategoryTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
