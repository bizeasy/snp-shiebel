import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IReturnItemBilling } from '../return-item-billing.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../return-item-billing.test-samples';

import { ReturnItemBillingService } from './return-item-billing.service';

const requireRestSample: IReturnItemBilling = {
  ...sampleWithRequiredData,
};

describe('ReturnItemBilling Service', () => {
  let service: ReturnItemBillingService;
  let httpMock: HttpTestingController;
  let expectedResult: IReturnItemBilling | IReturnItemBilling[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReturnItemBillingService);
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

    it('should create a ReturnItemBilling', () => {
      const returnItemBilling = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(returnItemBilling).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReturnItemBilling', () => {
      const returnItemBilling = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(returnItemBilling).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReturnItemBilling', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReturnItemBilling', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReturnItemBilling', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addReturnItemBillingToCollectionIfMissing', () => {
      it('should add a ReturnItemBilling to an empty array', () => {
        const returnItemBilling: IReturnItemBilling = sampleWithRequiredData;
        expectedResult = service.addReturnItemBillingToCollectionIfMissing([], returnItemBilling);
        expect(expectedResult).toEqual([returnItemBilling]);
      });

      it('should not add a ReturnItemBilling to an array that contains it', () => {
        const returnItemBilling: IReturnItemBilling = sampleWithRequiredData;
        const returnItemBillingCollection: IReturnItemBilling[] = [
          {
            ...returnItemBilling,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReturnItemBillingToCollectionIfMissing(returnItemBillingCollection, returnItemBilling);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReturnItemBilling to an array that doesn't contain it", () => {
        const returnItemBilling: IReturnItemBilling = sampleWithRequiredData;
        const returnItemBillingCollection: IReturnItemBilling[] = [sampleWithPartialData];
        expectedResult = service.addReturnItemBillingToCollectionIfMissing(returnItemBillingCollection, returnItemBilling);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(returnItemBilling);
      });

      it('should add only unique ReturnItemBilling to an array', () => {
        const returnItemBillingArray: IReturnItemBilling[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const returnItemBillingCollection: IReturnItemBilling[] = [sampleWithRequiredData];
        expectedResult = service.addReturnItemBillingToCollectionIfMissing(returnItemBillingCollection, ...returnItemBillingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const returnItemBilling: IReturnItemBilling = sampleWithRequiredData;
        const returnItemBilling2: IReturnItemBilling = sampleWithPartialData;
        expectedResult = service.addReturnItemBillingToCollectionIfMissing([], returnItemBilling, returnItemBilling2);
        expect(expectedResult).toEqual([returnItemBilling, returnItemBilling2]);
      });

      it('should accept null and undefined values', () => {
        const returnItemBilling: IReturnItemBilling = sampleWithRequiredData;
        expectedResult = service.addReturnItemBillingToCollectionIfMissing([], null, returnItemBilling, undefined);
        expect(expectedResult).toEqual([returnItemBilling]);
      });

      it('should return initial array if no ReturnItemBilling is added', () => {
        const returnItemBillingCollection: IReturnItemBilling[] = [sampleWithRequiredData];
        expectedResult = service.addReturnItemBillingToCollectionIfMissing(returnItemBillingCollection, undefined, null);
        expect(expectedResult).toEqual(returnItemBillingCollection);
      });
    });

    describe('compareReturnItemBilling', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReturnItemBilling(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25080 };
        const entity2 = null;

        const compareResult1 = service.compareReturnItemBilling(entity1, entity2);
        const compareResult2 = service.compareReturnItemBilling(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25080 };
        const entity2 = { id: 3543 };

        const compareResult1 = service.compareReturnItemBilling(entity1, entity2);
        const compareResult2 = service.compareReturnItemBilling(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25080 };
        const entity2 = { id: 25080 };

        const compareResult1 = service.compareReturnItemBilling(entity1, entity2);
        const compareResult2 = service.compareReturnItemBilling(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
