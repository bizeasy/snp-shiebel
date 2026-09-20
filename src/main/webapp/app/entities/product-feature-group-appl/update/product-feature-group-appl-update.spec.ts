import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IProductFeature } from 'app/entities/product-feature/product-feature.model';
import { ProductFeatureService } from 'app/entities/product-feature/service/product-feature.service';
import { IProductFeatureGroup } from 'app/entities/product-feature-group/product-feature-group.model';
import { ProductFeatureGroupService } from 'app/entities/product-feature-group/service/product-feature-group.service';
import { IProductFeatureGroupAppl } from '../product-feature-group-appl.model';
import { ProductFeatureGroupApplService } from '../service/product-feature-group-appl.service';

import { ProductFeatureGroupApplFormService } from './product-feature-group-appl-form.service';
import { ProductFeatureGroupApplUpdate } from './product-feature-group-appl-update';

describe('ProductFeatureGroupAppl Management Update Component', () => {
  let comp: ProductFeatureGroupApplUpdate;
  let fixture: ComponentFixture<ProductFeatureGroupApplUpdate>;
  let activatedRoute: ActivatedRoute;
  let productFeatureGroupApplFormService: ProductFeatureGroupApplFormService;
  let productFeatureGroupApplService: ProductFeatureGroupApplService;
  let productFeatureGroupService: ProductFeatureGroupService;
  let productFeatureService: ProductFeatureService;

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

    fixture = TestBed.createComponent(ProductFeatureGroupApplUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFeatureGroupApplFormService = TestBed.inject(ProductFeatureGroupApplFormService);
    productFeatureGroupApplService = TestBed.inject(ProductFeatureGroupApplService);
    productFeatureGroupService = TestBed.inject(ProductFeatureGroupService);
    productFeatureService = TestBed.inject(ProductFeatureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ProductFeatureGroup query and add missing value', () => {
      const productFeatureGroupAppl: IProductFeatureGroupAppl = { id: 7518 };
      const productFeatureGroup: IProductFeatureGroup = { id: 11544 };
      productFeatureGroupAppl.productFeatureGroup = productFeatureGroup;

      const productFeatureGroupCollection: IProductFeatureGroup[] = [{ id: 11544 }];
      vi.spyOn(productFeatureGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureGroupCollection })));
      const additionalProductFeatureGroups = [productFeatureGroup];
      const expectedCollection: IProductFeatureGroup[] = [...additionalProductFeatureGroups, ...productFeatureGroupCollection];
      vi.spyOn(productFeatureGroupService, 'addProductFeatureGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeatureGroupAppl });
      comp.ngOnInit();

      expect(productFeatureGroupService.query).toHaveBeenCalled();
      expect(productFeatureGroupService.addProductFeatureGroupToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureGroupCollection,
        ...additionalProductFeatureGroups.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeatureGroupsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductFeature query and add missing value', () => {
      const productFeatureGroupAppl: IProductFeatureGroupAppl = { id: 7518 };
      const productFeature: IProductFeature = { id: 30957 };
      productFeatureGroupAppl.productFeature = productFeature;

      const productFeatureCollection: IProductFeature[] = [{ id: 30957 }];
      vi.spyOn(productFeatureService, 'query').mockReturnValue(of(new HttpResponse({ body: productFeatureCollection })));
      const additionalProductFeatures = [productFeature];
      const expectedCollection: IProductFeature[] = [...additionalProductFeatures, ...productFeatureCollection];
      vi.spyOn(productFeatureService, 'addProductFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productFeatureGroupAppl });
      comp.ngOnInit();

      expect(productFeatureService.query).toHaveBeenCalled();
      expect(productFeatureService.addProductFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        productFeatureCollection,
        ...additionalProductFeatures.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productFeaturesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productFeatureGroupAppl: IProductFeatureGroupAppl = { id: 7518 };
      const productFeatureGroup: IProductFeatureGroup = { id: 11544 };
      productFeatureGroupAppl.productFeatureGroup = productFeatureGroup;
      const productFeature: IProductFeature = { id: 30957 };
      productFeatureGroupAppl.productFeature = productFeature;

      activatedRoute.data = of({ productFeatureGroupAppl });
      comp.ngOnInit();

      expect(comp.productFeatureGroupsSharedCollection()).toContainEqual(productFeatureGroup);
      expect(comp.productFeaturesSharedCollection()).toContainEqual(productFeature);
      expect(comp.productFeatureGroupAppl).toEqual(productFeatureGroupAppl);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureGroupAppl>();
      const productFeatureGroupAppl = { id: 8696 };
      vi.spyOn(productFeatureGroupApplFormService, 'getProductFeatureGroupAppl').mockReturnValue(productFeatureGroupAppl);
      vi.spyOn(productFeatureGroupApplService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureGroupAppl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureGroupAppl);
      saveSubject.complete();

      // THEN
      expect(productFeatureGroupApplFormService.getProductFeatureGroupAppl).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productFeatureGroupApplService.update).toHaveBeenCalledWith(expect.objectContaining(productFeatureGroupAppl));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureGroupAppl>();
      const productFeatureGroupAppl = { id: 8696 };
      vi.spyOn(productFeatureGroupApplFormService, 'getProductFeatureGroupAppl').mockReturnValue({ id: null });
      vi.spyOn(productFeatureGroupApplService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureGroupAppl: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(productFeatureGroupAppl);
      saveSubject.complete();

      // THEN
      expect(productFeatureGroupApplFormService.getProductFeatureGroupAppl).toHaveBeenCalled();
      expect(productFeatureGroupApplService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IProductFeatureGroupAppl>();
      const productFeatureGroupAppl = { id: 8696 };
      vi.spyOn(productFeatureGroupApplService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productFeatureGroupAppl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productFeatureGroupApplService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductFeatureGroup', () => {
      it('should forward to productFeatureGroupService', () => {
        const entity = { id: 11544 };
        const entity2 = { id: 27826 };
        vi.spyOn(productFeatureGroupService, 'compareProductFeatureGroup');
        comp.compareProductFeatureGroup(entity, entity2);
        expect(productFeatureGroupService.compareProductFeatureGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductFeature', () => {
      it('should forward to productFeatureService', () => {
        const entity = { id: 30957 };
        const entity2 = { id: 7728 };
        vi.spyOn(productFeatureService, 'compareProductFeature');
        comp.compareProductFeature(entity, entity2);
        expect(productFeatureService.compareProductFeature).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
