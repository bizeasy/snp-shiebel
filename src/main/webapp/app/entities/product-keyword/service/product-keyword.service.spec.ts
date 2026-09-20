import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductKeyword } from '../product-keyword.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-keyword.test-samples';

import { ProductKeywordService } from './product-keyword.service';

const requireRestSample: IProductKeyword = {
  ...sampleWithRequiredData,
};

describe('ProductKeyword Service', () => {
  let service: ProductKeywordService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductKeyword | IProductKeyword[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductKeywordService);
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

    it('should create a ProductKeyword', () => {
      const productKeyword = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productKeyword).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductKeyword', () => {
      const productKeyword = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productKeyword).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductKeyword', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductKeyword', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductKeyword', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductKeywordToCollectionIfMissing', () => {
      it('should add a ProductKeyword to an empty array', () => {
        const productKeyword: IProductKeyword = sampleWithRequiredData;
        expectedResult = service.addProductKeywordToCollectionIfMissing([], productKeyword);
        expect(expectedResult).toEqual([productKeyword]);
      });

      it('should not add a ProductKeyword to an array that contains it', () => {
        const productKeyword: IProductKeyword = sampleWithRequiredData;
        const productKeywordCollection: IProductKeyword[] = [
          {
            ...productKeyword,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductKeywordToCollectionIfMissing(productKeywordCollection, productKeyword);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductKeyword to an array that doesn't contain it", () => {
        const productKeyword: IProductKeyword = sampleWithRequiredData;
        const productKeywordCollection: IProductKeyword[] = [sampleWithPartialData];
        expectedResult = service.addProductKeywordToCollectionIfMissing(productKeywordCollection, productKeyword);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productKeyword);
      });

      it('should add only unique ProductKeyword to an array', () => {
        const productKeywordArray: IProductKeyword[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productKeywordCollection: IProductKeyword[] = [sampleWithRequiredData];
        expectedResult = service.addProductKeywordToCollectionIfMissing(productKeywordCollection, ...productKeywordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productKeyword: IProductKeyword = sampleWithRequiredData;
        const productKeyword2: IProductKeyword = sampleWithPartialData;
        expectedResult = service.addProductKeywordToCollectionIfMissing([], productKeyword, productKeyword2);
        expect(expectedResult).toEqual([productKeyword, productKeyword2]);
      });

      it('should accept null and undefined values', () => {
        const productKeyword: IProductKeyword = sampleWithRequiredData;
        expectedResult = service.addProductKeywordToCollectionIfMissing([], null, productKeyword, undefined);
        expect(expectedResult).toEqual([productKeyword]);
      });

      it('should return initial array if no ProductKeyword is added', () => {
        const productKeywordCollection: IProductKeyword[] = [sampleWithRequiredData];
        expectedResult = service.addProductKeywordToCollectionIfMissing(productKeywordCollection, undefined, null);
        expect(expectedResult).toEqual(productKeywordCollection);
      });
    });

    describe('compareProductKeyword', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductKeyword(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14945 };
        const entity2 = null;

        const compareResult1 = service.compareProductKeyword(entity1, entity2);
        const compareResult2 = service.compareProductKeyword(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14945 };
        const entity2 = { id: 12434 };

        const compareResult1 = service.compareProductKeyword(entity1, entity2);
        const compareResult2 = service.compareProductKeyword(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 14945 };
        const entity2 = { id: 14945 };

        const compareResult1 = service.compareProductKeyword(entity1, entity2);
        const compareResult2 = service.compareProductKeyword(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
