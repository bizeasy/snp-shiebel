import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFeatureCategoryAppl } from '../product-feature-category-appl.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-feature-category-appl.test-samples';

import { ProductFeatureCategoryApplService, RestProductFeatureCategoryAppl } from './product-feature-category-appl.service';

const requireRestSample: RestProductFeatureCategoryAppl = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('ProductFeatureCategoryAppl Service', () => {
  let service: ProductFeatureCategoryApplService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFeatureCategoryAppl | IProductFeatureCategoryAppl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFeatureCategoryApplService);
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

    it('should create a ProductFeatureCategoryAppl', () => {
      const productFeatureCategoryAppl = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFeatureCategoryAppl).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFeatureCategoryAppl', () => {
      const productFeatureCategoryAppl = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFeatureCategoryAppl).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFeatureCategoryAppl', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFeatureCategoryAppl', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFeatureCategoryAppl', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFeatureCategoryApplToCollectionIfMissing', () => {
      it('should add a ProductFeatureCategoryAppl to an empty array', () => {
        const productFeatureCategoryAppl: IProductFeatureCategoryAppl = sampleWithRequiredData;
        expectedResult = service.addProductFeatureCategoryApplToCollectionIfMissing([], productFeatureCategoryAppl);
        expect(expectedResult).toEqual([productFeatureCategoryAppl]);
      });

      it('should not add a ProductFeatureCategoryAppl to an array that contains it', () => {
        const productFeatureCategoryAppl: IProductFeatureCategoryAppl = sampleWithRequiredData;
        const productFeatureCategoryApplCollection: IProductFeatureCategoryAppl[] = [
          {
            ...productFeatureCategoryAppl,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFeatureCategoryApplToCollectionIfMissing(
          productFeatureCategoryApplCollection,
          productFeatureCategoryAppl,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFeatureCategoryAppl to an array that doesn't contain it", () => {
        const productFeatureCategoryAppl: IProductFeatureCategoryAppl = sampleWithRequiredData;
        const productFeatureCategoryApplCollection: IProductFeatureCategoryAppl[] = [sampleWithPartialData];
        expectedResult = service.addProductFeatureCategoryApplToCollectionIfMissing(
          productFeatureCategoryApplCollection,
          productFeatureCategoryAppl,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFeatureCategoryAppl);
      });

      it('should add only unique ProductFeatureCategoryAppl to an array', () => {
        const productFeatureCategoryApplArray: IProductFeatureCategoryAppl[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const productFeatureCategoryApplCollection: IProductFeatureCategoryAppl[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureCategoryApplToCollectionIfMissing(
          productFeatureCategoryApplCollection,
          ...productFeatureCategoryApplArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFeatureCategoryAppl: IProductFeatureCategoryAppl = sampleWithRequiredData;
        const productFeatureCategoryAppl2: IProductFeatureCategoryAppl = sampleWithPartialData;
        expectedResult = service.addProductFeatureCategoryApplToCollectionIfMissing(
          [],
          productFeatureCategoryAppl,
          productFeatureCategoryAppl2,
        );
        expect(expectedResult).toEqual([productFeatureCategoryAppl, productFeatureCategoryAppl2]);
      });

      it('should accept null and undefined values', () => {
        const productFeatureCategoryAppl: IProductFeatureCategoryAppl = sampleWithRequiredData;
        expectedResult = service.addProductFeatureCategoryApplToCollectionIfMissing([], null, productFeatureCategoryAppl, undefined);
        expect(expectedResult).toEqual([productFeatureCategoryAppl]);
      });

      it('should return initial array if no ProductFeatureCategoryAppl is added', () => {
        const productFeatureCategoryApplCollection: IProductFeatureCategoryAppl[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureCategoryApplToCollectionIfMissing(productFeatureCategoryApplCollection, undefined, null);
        expect(expectedResult).toEqual(productFeatureCategoryApplCollection);
      });
    });

    describe('compareProductFeatureCategoryAppl', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFeatureCategoryAppl(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5520 };
        const entity2 = null;

        const compareResult1 = service.compareProductFeatureCategoryAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureCategoryAppl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5520 };
        const entity2 = { id: 12894 };

        const compareResult1 = service.compareProductFeatureCategoryAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureCategoryAppl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 5520 };
        const entity2 = { id: 5520 };

        const compareResult1 = service.compareProductFeatureCategoryAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureCategoryAppl(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
