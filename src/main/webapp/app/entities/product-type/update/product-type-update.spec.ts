import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

import { ProductTypeFormService } from './product-type-form.service';
import { ProductTypeUpdate } from './product-type-update';

describe('ProductType Management Update Component', () => {
  let comp: ProductTypeUpdate;
  let fixture: ComponentFixture<ProductTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let productTypeFormService: ProductTypeFormService;
  let productTypeService: ProductTypeService;

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

    fixture = TestBed.createComponent(ProductTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productTypeFormService = TestBed.inject(ProductTypeFormService);
    productTypeService = TestBed.inject(ProductTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productType: IProductType = { id: 20361 };

      activatedRoute.data = of({ productType });
      comp.ngOnInit();

      expect(comp.productType).toEqual(productType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductType>();
      const productType = { id: 28647 };
      vi.spyOn(productTypeFormService, 'getProductType').mockReturnValue(productType);
      vi.spyOn(productTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productType);
      saveSubject.complete();

      // THEN
      expect(productTypeFormService.getProductType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productTypeService.update).toHaveBeenCalledWith(expect.objectContaining(productType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductType>();
      const productType = { id: 28647 };
      vi.spyOn(productTypeFormService, 'getProductType').mockReturnValue({ id: null });
      vi.spyOn(productTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productType);
      saveSubject.complete();

      // THEN
      expect(productTypeFormService.getProductType).toHaveBeenCalled();
      expect(productTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductType>();
      const productType = { id: 28647 };
      vi.spyOn(productTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
