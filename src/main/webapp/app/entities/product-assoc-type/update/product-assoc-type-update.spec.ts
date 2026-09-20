import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductAssocType } from '../product-assoc-type.model';
import { ProductAssocTypeService } from '../service/product-assoc-type.service';

import { ProductAssocTypeFormService } from './product-assoc-type-form.service';
import { ProductAssocTypeUpdate } from './product-assoc-type-update';

describe('ProductAssocType Management Update Component', () => {
  let comp: ProductAssocTypeUpdate;
  let fixture: ComponentFixture<ProductAssocTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let productAssocTypeFormService: ProductAssocTypeFormService;
  let productAssocTypeService: ProductAssocTypeService;

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

    fixture = TestBed.createComponent(ProductAssocTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productAssocTypeFormService = TestBed.inject(ProductAssocTypeFormService);
    productAssocTypeService = TestBed.inject(ProductAssocTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productAssocType: IProductAssocType = { id: 4250 };

      activatedRoute.data = of({ productAssocType });
      comp.ngOnInit();

      expect(comp.productAssocType).toEqual(productAssocType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAssocType>();
      const productAssocType = { id: 15174 };
      vi.spyOn(productAssocTypeFormService, 'getProductAssocType').mockReturnValue(productAssocType);
      vi.spyOn(productAssocTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAssocType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productAssocType);
      saveSubject.complete();

      // THEN
      expect(productAssocTypeFormService.getProductAssocType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productAssocTypeService.update).toHaveBeenCalledWith(expect.objectContaining(productAssocType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAssocType>();
      const productAssocType = { id: 15174 };
      vi.spyOn(productAssocTypeFormService, 'getProductAssocType').mockReturnValue({ id: null });
      vi.spyOn(productAssocTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAssocType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productAssocType);
      saveSubject.complete();

      // THEN
      expect(productAssocTypeFormService.getProductAssocType).toHaveBeenCalled();
      expect(productAssocTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductAssocType>();
      const productAssocType = { id: 15174 };
      vi.spyOn(productAssocTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productAssocType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productAssocTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
