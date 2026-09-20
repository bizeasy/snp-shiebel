import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFeatureGroupAppl } from '../product-feature-group-appl.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-feature-group-appl.test-samples';

import { ProductFeatureGroupApplService, RestProductFeatureGroupAppl } from './product-feature-group-appl.service';

const requireRestSample: RestProductFeatureGroupAppl = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('ProductFeatureGroupAppl Service', () => {
  let service: ProductFeatureGroupApplService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFeatureGroupAppl | IProductFeatureGroupAppl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFeatureGroupApplService);
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

    it('should create a ProductFeatureGroupAppl', () => {
      const productFeatureGroupAppl = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFeatureGroupAppl).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFeatureGroupAppl', () => {
      const productFeatureGroupAppl = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFeatureGroupAppl).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFeatureGroupAppl', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFeatureGroupAppl', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFeatureGroupAppl', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFeatureGroupApplToCollectionIfMissing', () => {
      it('should add a ProductFeatureGroupAppl to an empty array', () => {
        const productFeatureGroupAppl: IProductFeatureGroupAppl = sampleWithRequiredData;
        expectedResult = service.addProductFeatureGroupApplToCollectionIfMissing([], productFeatureGroupAppl);
        expect(expectedResult).toEqual([productFeatureGroupAppl]);
      });

      it('should not add a ProductFeatureGroupAppl to an array that contains it', () => {
        const productFeatureGroupAppl: IProductFeatureGroupAppl = sampleWithRequiredData;
        const productFeatureGroupApplCollection: IProductFeatureGroupAppl[] = [
          {
            ...productFeatureGroupAppl,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFeatureGroupApplToCollectionIfMissing(
          productFeatureGroupApplCollection,
          productFeatureGroupAppl,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFeatureGroupAppl to an array that doesn't contain it", () => {
        const productFeatureGroupAppl: IProductFeatureGroupAppl = sampleWithRequiredData;
        const productFeatureGroupApplCollection: IProductFeatureGroupAppl[] = [sampleWithPartialData];
        expectedResult = service.addProductFeatureGroupApplToCollectionIfMissing(
          productFeatureGroupApplCollection,
          productFeatureGroupAppl,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFeatureGroupAppl);
      });

      it('should add only unique ProductFeatureGroupAppl to an array', () => {
        const productFeatureGroupApplArray: IProductFeatureGroupAppl[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const productFeatureGroupApplCollection: IProductFeatureGroupAppl[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureGroupApplToCollectionIfMissing(
          productFeatureGroupApplCollection,
          ...productFeatureGroupApplArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFeatureGroupAppl: IProductFeatureGroupAppl = sampleWithRequiredData;
        const productFeatureGroupAppl2: IProductFeatureGroupAppl = sampleWithPartialData;
        expectedResult = service.addProductFeatureGroupApplToCollectionIfMissing([], productFeatureGroupAppl, productFeatureGroupAppl2);
        expect(expectedResult).toEqual([productFeatureGroupAppl, productFeatureGroupAppl2]);
      });

      it('should accept null and undefined values', () => {
        const productFeatureGroupAppl: IProductFeatureGroupAppl = sampleWithRequiredData;
        expectedResult = service.addProductFeatureGroupApplToCollectionIfMissing([], null, productFeatureGroupAppl, undefined);
        expect(expectedResult).toEqual([productFeatureGroupAppl]);
      });

      it('should return initial array if no ProductFeatureGroupAppl is added', () => {
        const productFeatureGroupApplCollection: IProductFeatureGroupAppl[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureGroupApplToCollectionIfMissing(productFeatureGroupApplCollection, undefined, null);
        expect(expectedResult).toEqual(productFeatureGroupApplCollection);
      });
    });

    describe('compareProductFeatureGroupAppl', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFeatureGroupAppl(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8696 };
        const entity2 = null;

        const compareResult1 = service.compareProductFeatureGroupAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureGroupAppl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8696 };
        const entity2 = { id: 7518 };

        const compareResult1 = service.compareProductFeatureGroupAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureGroupAppl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8696 };
        const entity2 = { id: 8696 };

        const compareResult1 = service.compareProductFeatureGroupAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureGroupAppl(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
