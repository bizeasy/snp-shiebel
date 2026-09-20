import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderItemBilling } from '../order-item-billing.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-item-billing.test-samples';

import { OrderItemBillingService } from './order-item-billing.service';

const requireRestSample: IOrderItemBilling = {
  ...sampleWithRequiredData,
};

describe('OrderItemBilling Service', () => {
  let service: OrderItemBillingService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderItemBilling | IOrderItemBilling[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderItemBillingService);
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

    it('should create a OrderItemBilling', () => {
      const orderItemBilling = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderItemBilling).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderItemBilling', () => {
      const orderItemBilling = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderItemBilling).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderItemBilling', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderItemBilling', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderItemBilling', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderItemBillingToCollectionIfMissing', () => {
      it('should add a OrderItemBilling to an empty array', () => {
        const orderItemBilling: IOrderItemBilling = sampleWithRequiredData;
        expectedResult = service.addOrderItemBillingToCollectionIfMissing([], orderItemBilling);
        expect(expectedResult).toEqual([orderItemBilling]);
      });

      it('should not add a OrderItemBilling to an array that contains it', () => {
        const orderItemBilling: IOrderItemBilling = sampleWithRequiredData;
        const orderItemBillingCollection: IOrderItemBilling[] = [
          {
            ...orderItemBilling,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderItemBillingToCollectionIfMissing(orderItemBillingCollection, orderItemBilling);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderItemBilling to an array that doesn't contain it", () => {
        const orderItemBilling: IOrderItemBilling = sampleWithRequiredData;
        const orderItemBillingCollection: IOrderItemBilling[] = [sampleWithPartialData];
        expectedResult = service.addOrderItemBillingToCollectionIfMissing(orderItemBillingCollection, orderItemBilling);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderItemBilling);
      });

      it('should add only unique OrderItemBilling to an array', () => {
        const orderItemBillingArray: IOrderItemBilling[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderItemBillingCollection: IOrderItemBilling[] = [sampleWithRequiredData];
        expectedResult = service.addOrderItemBillingToCollectionIfMissing(orderItemBillingCollection, ...orderItemBillingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderItemBilling: IOrderItemBilling = sampleWithRequiredData;
        const orderItemBilling2: IOrderItemBilling = sampleWithPartialData;
        expectedResult = service.addOrderItemBillingToCollectionIfMissing([], orderItemBilling, orderItemBilling2);
        expect(expectedResult).toEqual([orderItemBilling, orderItemBilling2]);
      });

      it('should accept null and undefined values', () => {
        const orderItemBilling: IOrderItemBilling = sampleWithRequiredData;
        expectedResult = service.addOrderItemBillingToCollectionIfMissing([], null, orderItemBilling, undefined);
        expect(expectedResult).toEqual([orderItemBilling]);
      });

      it('should return initial array if no OrderItemBilling is added', () => {
        const orderItemBillingCollection: IOrderItemBilling[] = [sampleWithRequiredData];
        expectedResult = service.addOrderItemBillingToCollectionIfMissing(orderItemBillingCollection, undefined, null);
        expect(expectedResult).toEqual(orderItemBillingCollection);
      });
    });

    describe('compareOrderItemBilling', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderItemBilling(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19790 };
        const entity2 = null;

        const compareResult1 = service.compareOrderItemBilling(entity1, entity2);
        const compareResult2 = service.compareOrderItemBilling(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19790 };
        const entity2 = { id: 16501 };

        const compareResult1 = service.compareOrderItemBilling(entity1, entity2);
        const compareResult2 = service.compareOrderItemBilling(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 19790 };
        const entity2 = { id: 19790 };

        const compareResult1 = service.compareOrderItemBilling(entity1, entity2);
        const compareResult2 = service.compareOrderItemBilling(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
