import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICatalogueCategory } from '../catalogue-category.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../catalogue-category.test-samples';

import { CatalogueCategoryService } from './catalogue-category.service';

const requireRestSample: ICatalogueCategory = {
  ...sampleWithRequiredData,
};

describe('CatalogueCategory Service', () => {
  let service: CatalogueCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ICatalogueCategory | ICatalogueCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogueCategoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CatalogueCategory', () => {
      const catalogueCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(catalogueCategory).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CatalogueCategory', () => {
      const catalogueCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(catalogueCategory).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CatalogueCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CatalogueCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CatalogueCategory', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCatalogueCategoryToCollectionIfMissing', () => {
      it('should add a CatalogueCategory to an empty array', () => {
        const catalogueCategory: ICatalogueCategory = sampleWithRequiredData;
        expectedResult = service.addCatalogueCategoryToCollectionIfMissing([], catalogueCategory);
        expect(expectedResult).toEqual([catalogueCategory]);
      });

      it('should not add a CatalogueCategory to an array that contains it', () => {
        const catalogueCategory: ICatalogueCategory = sampleWithRequiredData;
        const catalogueCategoryCollection: ICatalogueCategory[] = [
          {
            ...catalogueCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCatalogueCategoryToCollectionIfMissing(catalogueCategoryCollection, catalogueCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CatalogueCategory to an array that doesn't contain it", () => {
        const catalogueCategory: ICatalogueCategory = sampleWithRequiredData;
        const catalogueCategoryCollection: ICatalogueCategory[] = [sampleWithPartialData];
        expectedResult = service.addCatalogueCategoryToCollectionIfMissing(catalogueCategoryCollection, catalogueCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogueCategory);
      });

      it('should add only unique CatalogueCategory to an array', () => {
        const catalogueCategoryArray: ICatalogueCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const catalogueCategoryCollection: ICatalogueCategory[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogueCategoryToCollectionIfMissing(catalogueCategoryCollection, ...catalogueCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogueCategory: ICatalogueCategory = sampleWithRequiredData;
        const catalogueCategory2: ICatalogueCategory = sampleWithPartialData;
        expectedResult = service.addCatalogueCategoryToCollectionIfMissing([], catalogueCategory, catalogueCategory2);
        expect(expectedResult).toEqual([catalogueCategory, catalogueCategory2]);
      });

      it('should accept null and undefined values', () => {
        const catalogueCategory: ICatalogueCategory = sampleWithRequiredData;
        expectedResult = service.addCatalogueCategoryToCollectionIfMissing([], null, catalogueCategory, undefined);
        expect(expectedResult).toEqual([catalogueCategory]);
      });

      it('should return initial array if no CatalogueCategory is added', () => {
        const catalogueCategoryCollection: ICatalogueCategory[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogueCategoryToCollectionIfMissing(catalogueCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(catalogueCategoryCollection);
      });
    });

    describe('compareCatalogueCategory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCatalogueCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25418 };
        const entity2 = null;

        const compareResult1 = service.compareCatalogueCategory(entity1, entity2);
        const compareResult2 = service.compareCatalogueCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25418 };
        const entity2 = { id: 10134 };

        const compareResult1 = service.compareCatalogueCategory(entity1, entity2);
        const compareResult2 = service.compareCatalogueCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25418 };
        const entity2 = { id: 25418 };

        const compareResult1 = service.compareCatalogueCategory(entity1, entity2);
        const compareResult2 = service.compareCatalogueCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
