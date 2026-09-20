import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductPriceType } from '../product-price-type.model';
import { ProductPriceTypeService } from '../service/product-price-type.service';

import { ProductPriceTypeFormService } from './product-price-type-form.service';
import { ProductPriceTypeUpdate } from './product-price-type-update';

describe('ProductPriceType Management Update Component', () => {
  let comp: ProductPriceTypeUpdate;
  let fixture: ComponentFixture<ProductPriceTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let productPriceTypeFormService: ProductPriceTypeFormService;
  let productPriceTypeService: ProductPriceTypeService;

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

    fixture = TestBed.createComponent(ProductPriceTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productPriceTypeFormService = TestBed.inject(ProductPriceTypeFormService);
    productPriceTypeService = TestBed.inject(ProductPriceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productPriceType: IProductPriceType = { id: 31296 };

      activatedRoute.data = of({ productPriceType });
      comp.ngOnInit();

      expect(comp.productPriceType).toEqual(productPriceType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPriceType>();
      const productPriceType = { id: 18810 };
      vi.spyOn(productPriceTypeFormService, 'getProductPriceType').mockReturnValue(productPriceType);
      vi.spyOn(productPriceTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPriceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productPriceType);
      saveSubject.complete();

      // THEN
      expect(productPriceTypeFormService.getProductPriceType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productPriceTypeService.update).toHaveBeenCalledWith(expect.objectContaining(productPriceType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPriceType>();
      const productPriceType = { id: 18810 };
      vi.spyOn(productPriceTypeFormService, 'getProductPriceType').mockReturnValue({ id: null });
      vi.spyOn(productPriceTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPriceType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productPriceType);
      saveSubject.complete();

      // THEN
      expect(productPriceTypeFormService.getProductPriceType).toHaveBeenCalled();
      expect(productPriceTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPriceType>();
      const productPriceType = { id: 18810 };
      vi.spyOn(productPriceTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPriceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productPriceTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
