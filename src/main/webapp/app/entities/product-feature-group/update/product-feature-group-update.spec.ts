import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductFeatureGroup } from '../product-feature-group.model';
import { ProductFeatureGroupService } from '../service/product-feature-group.service';

import { ProductFeatureGroupFormService } from './product-feature-group-form.service';
import { ProductFeatureGroupUpdate } from './product-feature-group-update';

describe('ProductFeatureGroup Management Update Component', () => {
  let comp: ProductFeatureGroupUpdate;
  let fixture: ComponentFixture<ProductFeatureGroupUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureGroupFormService: ProductFeatureGroupFormService;
  let productFeatureGroupService: ProductFeatureGroupService;

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

    fixture = TestBed.createComponent(ProductFeatureGroupUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureGroupFormService = TestBed.inject(ProductFeatureGroupFormService);
    productFeatureGroupService = TestBed.inject(ProductFeatureGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const productFeatureGroup: IProductFeatureGroup = { id: 27826 };

      activatedRoute.data = of({ productFeatureGroup });
      comp.ngOnInit();

      expect(comp.productFeatureGroup).toEqual(productFeatureGroup);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureGroup>();
      const productFeatureGroup = { id: 11544 };
      vi.spyOn(productFeatureGroupFormService, 'getProductFeatureGroup').mockReturnValue(productFeatureGroup);
      vi.spyOn(productFeatureGroupService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureGroup);
      saveSubject.complete();

      // THEN
      expect(productFeatureGroupFormService.getProductFeatureGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureGroupService.update).toHaveBeenCalledWith(expect.objectContaining(productFeatureGroup));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureGroup>();
      const productFeatureGroup = { id: 11544 };
      vi.spyOn(productFeatureGroupFormService, 'getProductFeatureGroup').mockReturnValue({ id: null });
      vi.spyOn(productFeatureGroupService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureGroup);
      saveSubject.complete();

      // THEN
      expect(productFeatureGroupFormService.getProductFeatureGroup).toHaveBeenCalled();
      expect(productFeatureGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureGroup>();
      const productFeatureGroup = { id: 11544 };
      vi.spyOn(productFeatureGroupService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
