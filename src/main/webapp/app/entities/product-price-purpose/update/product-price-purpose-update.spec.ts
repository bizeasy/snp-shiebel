import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductPricePurpose } from '../product-price-purpose.model';
import { ProductPricePurposeService } from '../service/product-price-purpose.service';

import { ProductPricePurposeFormService } from './product-price-purpose-form.service';
import { ProductPricePurposeUpdate } from './product-price-purpose-update';

describe('ProductPricePurpose Management Update Component', () => {
  let comp: ProductPricePurposeUpdate;
  let fixture: ComponentFixture<ProductPricePurposeUpdate>;
  let activatedRoute: ActivatedRoute;
  let productPricePurposeFormService: ProductPricePurposeFormService;
  let productPricePurposeService: ProductPricePurposeService;

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

    fixture = TestBed.createComponent(ProductPricePurposeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productPricePurposeFormService = TestBed.inject(ProductPricePurposeFormService);
    productPricePurposeService = TestBed.inject(ProductPricePurposeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productPricePurpose: IProductPricePurpose = { id: 15993 };

      activatedRoute.data = of({ productPricePurpose });
      comp.ngOnInit();

      expect(comp.productPricePurpose).toEqual(productPricePurpose);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPricePurpose>();
      const productPricePurpose = { id: 8537 };
      vi.spyOn(productPricePurposeFormService, 'getProductPricePurpose').mockReturnValue(productPricePurpose);
      vi.spyOn(productPricePurposeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPricePurpose });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productPricePurpose);
      saveSubject.complete();

      // THEN
      expect(productPricePurposeFormService.getProductPricePurpose).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productPricePurposeService.update).toHaveBeenCalledWith(expect.objectContaining(productPricePurpose));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPricePurpose>();
      const productPricePurpose = { id: 8537 };
      vi.spyOn(productPricePurposeFormService, 'getProductPricePurpose').mockReturnValue({ id: null });
      vi.spyOn(productPricePurposeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPricePurpose: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productPricePurpose);
      saveSubject.complete();

      // THEN
      expect(productPricePurposeFormService.getProductPricePurpose).toHaveBeenCalled();
      expect(productPricePurposeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductPricePurpose>();
      const productPricePurpose = { id: 8537 };
      vi.spyOn(productPricePurposeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPricePurpose });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productPricePurposeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
