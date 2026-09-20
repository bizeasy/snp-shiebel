import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderStatus } from '../order-status.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-status.test-samples';

import { OrderStatusService, RestOrderStatus } from './order-status.service';

const requireRestSample: RestOrderStatus = {
  ...sampleWithRequiredData,
  statusDateTime: sampleWithRequiredData.statusDateTime?.toJSON(),
};

describe('OrderStatus Service', () => {
  let service: OrderStatusService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderStatus | IOrderStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderStatusService);
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

    it('should create a OrderStatus', () => {
      const orderStatus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderStatus).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderStatus', () => {
      const orderStatus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderStatus).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderStatus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderStatus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderStatus', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderStatusToCollectionIfMissing', () => {
      it('should add a OrderStatus to an empty array', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        expectedResult = service.addOrderStatusToCollectionIfMissing([], orderStatus);
        expect(expectedResult).toEqual([orderStatus]);
      });

      it('should not add a OrderStatus to an array that contains it', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        const orderStatusCollection: IOrderStatus[] = [
          {
            ...orderStatus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, orderStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderStatus to an array that doesn't contain it", () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        const orderStatusCollection: IOrderStatus[] = [sampleWithPartialData];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, orderStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderStatus);
      });

      it('should add only unique OrderStatus to an array', () => {
        const orderStatusArray: IOrderStatus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderStatusCollection: IOrderStatus[] = [sampleWithRequiredData];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, ...orderStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        const orderStatus2: IOrderStatus = sampleWithPartialData;
        expectedResult = service.addOrderStatusToCollectionIfMissing([], orderStatus, orderStatus2);
        expect(expectedResult).toEqual([orderStatus, orderStatus2]);
      });

      it('should accept null and undefined values', () => {
        const orderStatus: IOrderStatus = sampleWithRequiredData;
        expectedResult = service.addOrderStatusToCollectionIfMissing([], null, orderStatus, undefined);
        expect(expectedResult).toEqual([orderStatus]);
      });

      it('should return initial array if no OrderStatus is added', () => {
        const orderStatusCollection: IOrderStatus[] = [sampleWithRequiredData];
        expectedResult = service.addOrderStatusToCollectionIfMissing(orderStatusCollection, undefined, null);
        expect(expectedResult).toEqual(orderStatusCollection);
      });
    });

    describe('compareOrderStatus', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderStatus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5634 };
        const entity2 = null;

        const compareResult1 = service.compareOrderStatus(entity1, entity2);
        const compareResult2 = service.compareOrderStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5634 };
        const entity2 = { id: 6707 };

        const compareResult1 = service.compareOrderStatus(entity1, entity2);
        const compareResult2 = service.compareOrderStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 5634 };
        const entity2 = { id: 5634 };

        const compareResult1 = service.compareOrderStatus(entity1, entity2);
        const compareResult2 = service.compareOrderStatus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
