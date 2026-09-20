import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFeatureAppl } from '../product-feature-appl.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-feature-appl.test-samples';

import { ProductFeatureApplService, RestProductFeatureAppl } from './product-feature-appl.service';

const requireRestSample: RestProductFeatureAppl = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('ProductFeatureAppl Service', () => {
  let service: ProductFeatureApplService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFeatureAppl | IProductFeatureAppl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFeatureApplService);
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

    it('should create a ProductFeatureAppl', () => {
      const productFeatureAppl = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFeatureAppl).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFeatureAppl', () => {
      const productFeatureAppl = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFeatureAppl).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFeatureAppl', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFeatureAppl', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFeatureAppl', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFeatureApplToCollectionIfMissing', () => {
      it('should add a ProductFeatureAppl to an empty array', () => {
        const productFeatureAppl: IProductFeatureAppl = sampleWithRequiredData;
        expectedResult = service.addProductFeatureApplToCollectionIfMissing([], productFeatureAppl);
        expect(expectedResult).toEqual([productFeatureAppl]);
      });

      it('should not add a ProductFeatureAppl to an array that contains it', () => {
        const productFeatureAppl: IProductFeatureAppl = sampleWithRequiredData;
        const productFeatureApplCollection: IProductFeatureAppl[] = [
          {
            ...productFeatureAppl,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFeatureApplToCollectionIfMissing(productFeatureApplCollection, productFeatureAppl);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFeatureAppl to an array that doesn't contain it", () => {
        const productFeatureAppl: IProductFeatureAppl = sampleWithRequiredData;
        const productFeatureApplCollection: IProductFeatureAppl[] = [sampleWithPartialData];
        expectedResult = service.addProductFeatureApplToCollectionIfMissing(productFeatureApplCollection, productFeatureAppl);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFeatureAppl);
      });

      it('should add only unique ProductFeatureAppl to an array', () => {
        const productFeatureApplArray: IProductFeatureAppl[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productFeatureApplCollection: IProductFeatureAppl[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureApplToCollectionIfMissing(productFeatureApplCollection, ...productFeatureApplArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFeatureAppl: IProductFeatureAppl = sampleWithRequiredData;
        const productFeatureAppl2: IProductFeatureAppl = sampleWithPartialData;
        expectedResult = service.addProductFeatureApplToCollectionIfMissing([], productFeatureAppl, productFeatureAppl2);
        expect(expectedResult).toEqual([productFeatureAppl, productFeatureAppl2]);
      });

      it('should accept null and undefined values', () => {
        const productFeatureAppl: IProductFeatureAppl = sampleWithRequiredData;
        expectedResult = service.addProductFeatureApplToCollectionIfMissing([], null, productFeatureAppl, undefined);
        expect(expectedResult).toEqual([productFeatureAppl]);
      });

      it('should return initial array if no ProductFeatureAppl is added', () => {
        const productFeatureApplCollection: IProductFeatureAppl[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureApplToCollectionIfMissing(productFeatureApplCollection, undefined, null);
        expect(expectedResult).toEqual(productFeatureApplCollection);
      });
    });

    describe('compareProductFeatureAppl', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFeatureAppl(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32420 };
        const entity2 = null;

        const compareResult1 = service.compareProductFeatureAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureAppl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32420 };
        const entity2 = { id: 1945 };

        const compareResult1 = service.compareProductFeatureAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureAppl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 32420 };
        const entity2 = { id: 32420 };

        const compareResult1 = service.compareProductFeatureAppl(entity1, entity2);
        const compareResult2 = service.compareProductFeatureAppl(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
