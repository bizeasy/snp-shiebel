import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustRequestCategory } from '../cust-request-category.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../cust-request-category.test-samples';

import { CustRequestCategoryService } from './cust-request-category.service';

const requireRestSample: ICustRequestCategory = {
  ...sampleWithRequiredData,
};

describe('CustRequestCategory Service', () => {
  let service: CustRequestCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustRequestCategory | ICustRequestCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustRequestCategoryService);
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

    it('should create a CustRequestCategory', () => {
      const custRequestCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(custRequestCategory).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustRequestCategory', () => {
      const custRequestCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(custRequestCategory).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustRequestCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustRequestCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustRequestCategory', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustRequestCategoryToCollectionIfMissing', () => {
      it('should add a CustRequestCategory to an empty array', () => {
        const custRequestCategory: ICustRequestCategory = sampleWithRequiredData;
        expectedResult = service.addCustRequestCategoryToCollectionIfMissing([], custRequestCategory);
        expect(expectedResult).toEqual([custRequestCategory]);
      });

      it('should not add a CustRequestCategory to an array that contains it', () => {
        const custRequestCategory: ICustRequestCategory = sampleWithRequiredData;
        const custRequestCategoryCollection: ICustRequestCategory[] = [
          {
            ...custRequestCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustRequestCategoryToCollectionIfMissing(custRequestCategoryCollection, custRequestCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustRequestCategory to an array that doesn't contain it", () => {
        const custRequestCategory: ICustRequestCategory = sampleWithRequiredData;
        const custRequestCategoryCollection: ICustRequestCategory[] = [sampleWithPartialData];
        expectedResult = service.addCustRequestCategoryToCollectionIfMissing(custRequestCategoryCollection, custRequestCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(custRequestCategory);
      });

      it('should add only unique CustRequestCategory to an array', () => {
        const custRequestCategoryArray: ICustRequestCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const custRequestCategoryCollection: ICustRequestCategory[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestCategoryToCollectionIfMissing(custRequestCategoryCollection, ...custRequestCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const custRequestCategory: ICustRequestCategory = sampleWithRequiredData;
        const custRequestCategory2: ICustRequestCategory = sampleWithPartialData;
        expectedResult = service.addCustRequestCategoryToCollectionIfMissing([], custRequestCategory, custRequestCategory2);
        expect(expectedResult).toEqual([custRequestCategory, custRequestCategory2]);
      });

      it('should accept null and undefined values', () => {
        const custRequestCategory: ICustRequestCategory = sampleWithRequiredData;
        expectedResult = service.addCustRequestCategoryToCollectionIfMissing([], null, custRequestCategory, undefined);
        expect(expectedResult).toEqual([custRequestCategory]);
      });

      it('should return initial array if no CustRequestCategory is added', () => {
        const custRequestCategoryCollection: ICustRequestCategory[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestCategoryToCollectionIfMissing(custRequestCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(custRequestCategoryCollection);
      });
    });

    describe('compareCustRequestCategory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustRequestCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30459 };
        const entity2 = null;

        const compareResult1 = service.compareCustRequestCategory(entity1, entity2);
        const compareResult2 = service.compareCustRequestCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30459 };
        const entity2 = { id: 7246 };

        const compareResult1 = service.compareCustRequestCategory(entity1, entity2);
        const compareResult2 = service.compareCustRequestCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 30459 };
        const entity2 = { id: 30459 };

        const compareResult1 = service.compareCustRequestCategory(entity1, entity2);
        const compareResult2 = service.compareCustRequestCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
