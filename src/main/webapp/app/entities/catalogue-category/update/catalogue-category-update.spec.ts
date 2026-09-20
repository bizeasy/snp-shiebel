import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICatalogue } from 'app/entities/catalogue/catalogue.model';
import { CatalogueService } from 'app/entities/catalogue/service/catalogue.service';
import { ICatalogueCategoryType } from 'app/entities/catalogue-category-type/catalogue-category-type.model';
import { CatalogueCategoryTypeService } from 'app/entities/catalogue-category-type/service/catalogue-category-type.service';
import { IProductCategory } from 'app/entities/product-category/product-category.model';
import { ProductCategoryService } from 'app/entities/product-category/service/product-category.service';
import { ICatalogueCategory } from '../catalogue-category.model';
import { CatalogueCategoryService } from '../service/catalogue-category.service';

import { CatalogueCategoryFormService } from './catalogue-category-form.service';
import { CatalogueCategoryUpdate } from './catalogue-category-update';

describe('CatalogueCategory Management Update Component', () => {
  let comp: CatalogueCategoryUpdate;
  let fixture: ComponentFixture<CatalogueCategoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let catalogueCategoryFormService: CatalogueCategoryFormService;
  let catalogueCategoryService: CatalogueCategoryService;
  let catalogueService: CatalogueService;
  let productCategoryService: ProductCategoryService;
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

    fixture = TestBed.createComponent(CatalogueCategoryUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogueCategoryFormService = TestBed.inject(CatalogueCategoryFormService);
    catalogueCategoryService = TestBed.inject(CatalogueCategoryService);
    catalogueService = TestBed.inject(CatalogueService);
    productCategoryService = TestBed.inject(ProductCategoryService);
    catalogueCategoryTypeService = TestBed.inject(CatalogueCategoryTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Catalogue query and add missing value', () => {
      const catalogueCategory: ICatalogueCategory = { id: 10134 };
      const catalogue: ICatalogue = { id: 7498 };
      catalogueCategory.catalogue = catalogue;

      const catalogueCollection: ICatalogue[] = [{ id: 7498 }];
      vi.spyOn(catalogueService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogueCollection })));
      const additionalCatalogues = [catalogue];
      const expectedCollection: ICatalogue[] = [...additionalCatalogues, ...catalogueCollection];
      vi.spyOn(catalogueService, 'addCatalogueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalogueCategory });
      comp.ngOnInit();

      expect(catalogueService.query).toHaveBeenCalled();
      expect(catalogueService.addCatalogueToCollectionIfMissing).toHaveBeenCalledWith(
        catalogueCollection,
        ...additionalCatalogues.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.cataloguesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ProductCategory query and add missing value', () => {
      const catalogueCategory: ICatalogueCategory = { id: 10134 };
      const productCategory: IProductCategory = { id: 29286 };
      catalogueCategory.productCategory = productCategory;

      const productCategoryCollection: IProductCategory[] = [{ id: 29286 }];
      vi.spyOn(productCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: productCategoryCollection })));
      const additionalProductCategories = [productCategory];
      const expectedCollection: IProductCategory[] = [...additionalProductCategories, ...productCategoryCollection];
      vi.spyOn(productCategoryService, 'addProductCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalogueCategory });
      comp.ngOnInit();

      expect(productCategoryService.query).toHaveBeenCalled();
      expect(productCategoryService.addProductCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        productCategoryCollection,
        ...additionalProductCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.productCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call CatalogueCategoryType query and add missing value', () => {
      const catalogueCategory: ICatalogueCategory = { id: 10134 };
      const catalogueCategoryType: ICatalogueCategoryType = { id: 27421 };
      catalogueCategory.catalogueCategoryType = catalogueCategoryType;

      const catalogueCategoryTypeCollection: ICatalogueCategoryType[] = [{ id: 27421 }];
      vi.spyOn(catalogueCategoryTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: catalogueCategoryTypeCollection })));
      const additionalCatalogueCategoryTypes = [catalogueCategoryType];
      const expectedCollection: ICatalogueCategoryType[] = [...additionalCatalogueCategoryTypes, ...catalogueCategoryTypeCollection];
      vi.spyOn(catalogueCategoryTypeService, 'addCatalogueCategoryTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalogueCategory });
      comp.ngOnInit();

      expect(catalogueCategoryTypeService.query).toHaveBeenCalled();
      expect(catalogueCategoryTypeService.addCatalogueCategoryTypeToCollectionIfMissing).toHaveBeenCalledWith(
        catalogueCategoryTypeCollection,
        ...additionalCatalogueCategoryTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.catalogueCategoryTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const catalogueCategory: ICatalogueCategory = { id: 10134 };
      const catalogue: ICatalogue = { id: 7498 };
      catalogueCategory.catalogue = catalogue;
      const productCategory: IProductCategory = { id: 29286 };
      catalogueCategory.productCategory = productCategory;
      const catalogueCategoryType: ICatalogueCategoryType = { id: 27421 };
      catalogueCategory.catalogueCategoryType = catalogueCategoryType;

      activatedRoute.data = of({ catalogueCategory });
      comp.ngOnInit();

      expect(comp.cataloguesSharedCollection()).toContainEqual(catalogue);
      expect(comp.productCategoriesSharedCollection()).toContainEqual(productCategory);
      expect(comp.catalogueCategoryTypesSharedCollection()).toContainEqual(catalogueCategoryType);
      expect(comp.catalogueCategory).toEqual(catalogueCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogueCategory>();
      const catalogueCategory = { id: 25418 };
      vi.spyOn(catalogueCategoryFormService, 'getCatalogueCategory').mockReturnValue(catalogueCategory);
      vi.spyOn(catalogueCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogueCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(catalogueCategory);
      saveSubject.complete();

      // THEN
      expect(catalogueCategoryFormService.getCatalogueCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogueCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(catalogueCategory));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogueCategory>();
      const catalogueCategory = { id: 25418 };
      vi.spyOn(catalogueCategoryFormService, 'getCatalogueCategory').mockReturnValue({ id: null });
      vi.spyOn(catalogueCategoryService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogueCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(catalogueCategory);
      saveSubject.complete();

      // THEN
      expect(catalogueCategoryFormService.getCatalogueCategory).toHaveBeenCalled();
      expect(catalogueCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICatalogueCategory>();
      const catalogueCategory = { id: 25418 };
      vi.spyOn(catalogueCategoryService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogueCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogueCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCatalogue', () => {
      it('should forward to catalogueService', () => {
        const entity = { id: 7498 };
        const entity2 = { id: 28253 };
        vi.spyOn(catalogueService, 'compareCatalogue');
        comp.compareCatalogue(entity, entity2);
        expect(catalogueService.compareCatalogue).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProductCategory', () => {
      it('should forward to productCategoryService', () => {
        const entity = { id: 29286 };
        const entity2 = { id: 19244 };
        vi.spyOn(productCategoryService, 'compareProductCategory');
        comp.compareProductCategory(entity, entity2);
        expect(productCategoryService.compareProductCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCatalogueCategoryType', () => {
      it('should forward to catalogueCategoryTypeService', () => {
        const entity = { id: 27421 };
        const entity2 = { id: 16331 };
        vi.spyOn(catalogueCategoryTypeService, 'compareCatalogueCategoryType');
        comp.compareCatalogueCategoryType(entity, entity2);
        expect(catalogueCategoryTypeService.compareCatalogueCategoryType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
