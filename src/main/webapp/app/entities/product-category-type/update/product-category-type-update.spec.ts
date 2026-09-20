import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductCategoryType } from '../product-category-type.model';
import { ProductCategoryTypeService } from '../service/product-category-type.service';

import { ProductCategoryTypeFormService } from './product-category-type-form.service';
import { ProductCategoryTypeUpdate } from './product-category-type-update';

describe('ProductCategoryType Management Update Component', () => {
  let comp: ProductCategoryTypeUpdate;
  let fixture: ComponentFixture<ProductCategoryTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let productCategoryTypeFormService: ProductCategoryTypeFormService;
  let productCategoryTypeService: ProductCategoryTypeService;

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

    fixture = TestBed.createComponent(ProductCategoryTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productCategoryTypeFormService = TestBed.inject(ProductCategoryTypeFormService);
    productCategoryTypeService = TestBed.inject(ProductCategoryTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productCategoryType: IProductCategoryType = { id: 32619 };

      activatedRoute.data = of({ productCategoryType });
      comp.ngOnInit();

      expect(comp.productCategoryType).toEqual(productCategoryType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategoryType>();
      const productCategoryType = { id: 10914 };
      vi.spyOn(productCategoryTypeFormService, 'getProductCategoryType').mockReturnValue(productCategoryType);
      vi.spyOn(productCategoryTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productCategoryType);
      saveSubject.complete();

      // THEN
      expect(productCategoryTypeFormService.getProductCategoryType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productCategoryTypeService.update).toHaveBeenCalledWith(expect.objectContaining(productCategoryType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategoryType>();
      const productCategoryType = { id: 10914 };
      vi.spyOn(productCategoryTypeFormService, 'getProductCategoryType').mockReturnValue({ id: null });
      vi.spyOn(productCategoryTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategoryType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productCategoryType);
      saveSubject.complete();

      // THEN
      expect(productCategoryTypeFormService.getProductCategoryType).toHaveBeenCalled();
      expect(productCategoryTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductCategoryType>();
      const productCategoryType = { id: 10914 };
      vi.spyOn(productCategoryTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategoryType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productCategoryTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
