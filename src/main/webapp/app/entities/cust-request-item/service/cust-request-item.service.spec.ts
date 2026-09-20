import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustRequestItem } from '../cust-request-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cust-request-item.test-samples';

import { CustRequestItemService, RestCustRequestItem } from './cust-request-item.service';

const requireRestSample: RestCustRequestItem = {
  ...sampleWithRequiredData,
  requiredByDate: sampleWithRequiredData.requiredByDate?.toJSON(),
};

describe('CustRequestItem Service', () => {
  let service: CustRequestItemService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustRequestItem | ICustRequestItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustRequestItemService);
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

    it('should create a CustRequestItem', () => {
      const custRequestItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(custRequestItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustRequestItem', () => {
      const custRequestItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(custRequestItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustRequestItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustRequestItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustRequestItem', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustRequestItemToCollectionIfMissing', () => {
      it('should add a CustRequestItem to an empty array', () => {
        const custRequestItem: ICustRequestItem = sampleWithRequiredData;
        expectedResult = service.addCustRequestItemToCollectionIfMissing([], custRequestItem);
        expect(expectedResult).toEqual([custRequestItem]);
      });

      it('should not add a CustRequestItem to an array that contains it', () => {
        const custRequestItem: ICustRequestItem = sampleWithRequiredData;
        const custRequestItemCollection: ICustRequestItem[] = [
          {
            ...custRequestItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustRequestItemToCollectionIfMissing(custRequestItemCollection, custRequestItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustRequestItem to an array that doesn't contain it", () => {
        const custRequestItem: ICustRequestItem = sampleWithRequiredData;
        const custRequestItemCollection: ICustRequestItem[] = [sampleWithPartialData];
        expectedResult = service.addCustRequestItemToCollectionIfMissing(custRequestItemCollection, custRequestItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(custRequestItem);
      });

      it('should add only unique CustRequestItem to an array', () => {
        const custRequestItemArray: ICustRequestItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const custRequestItemCollection: ICustRequestItem[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestItemToCollectionIfMissing(custRequestItemCollection, ...custRequestItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const custRequestItem: ICustRequestItem = sampleWithRequiredData;
        const custRequestItem2: ICustRequestItem = sampleWithPartialData;
        expectedResult = service.addCustRequestItemToCollectionIfMissing([], custRequestItem, custRequestItem2);
        expect(expectedResult).toEqual([custRequestItem, custRequestItem2]);
      });

      it('should accept null and undefined values', () => {
        const custRequestItem: ICustRequestItem = sampleWithRequiredData;
        expectedResult = service.addCustRequestItemToCollectionIfMissing([], null, custRequestItem, undefined);
        expect(expectedResult).toEqual([custRequestItem]);
      });

      it('should return initial array if no CustRequestItem is added', () => {
        const custRequestItemCollection: ICustRequestItem[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestItemToCollectionIfMissing(custRequestItemCollection, undefined, null);
        expect(expectedResult).toEqual(custRequestItemCollection);
      });
    });

    describe('compareCustRequestItem', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustRequestItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17328 };
        const entity2 = null;

        const compareResult1 = service.compareCustRequestItem(entity1, entity2);
        const compareResult2 = service.compareCustRequestItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17328 };
        const entity2 = { id: 23718 };

        const compareResult1 = service.compareCustRequestItem(entity1, entity2);
        const compareResult2 = service.compareCustRequestItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 17328 };
        const entity2 = { id: 17328 };

        const compareResult1 = service.compareCustRequestItem(entity1, entity2);
        const compareResult2 = service.compareCustRequestItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
