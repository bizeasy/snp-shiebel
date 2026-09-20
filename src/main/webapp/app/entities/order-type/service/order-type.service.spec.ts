import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderType } from '../order-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-type.test-samples';

import { OrderTypeService } from './order-type.service';

const requireRestSample: IOrderType = {
  ...sampleWithRequiredData,
};

describe('OrderType Service', () => {
  let service: OrderTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderType | IOrderType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderTypeService);
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

    it('should create a OrderType', () => {
      const orderType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderType', () => {
      const orderType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderTypeToCollectionIfMissing', () => {
      it('should add a OrderType to an empty array', () => {
        const orderType: IOrderType = sampleWithRequiredData;
        expectedResult = service.addOrderTypeToCollectionIfMissing([], orderType);
        expect(expectedResult).toEqual([orderType]);
      });

      it('should not add a OrderType to an array that contains it', () => {
        const orderType: IOrderType = sampleWithRequiredData;
        const orderTypeCollection: IOrderType[] = [
          {
            ...orderType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderTypeToCollectionIfMissing(orderTypeCollection, orderType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderType to an array that doesn't contain it", () => {
        const orderType: IOrderType = sampleWithRequiredData;
        const orderTypeCollection: IOrderType[] = [sampleWithPartialData];
        expectedResult = service.addOrderTypeToCollectionIfMissing(orderTypeCollection, orderType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderType);
      });

      it('should add only unique OrderType to an array', () => {
        const orderTypeArray: IOrderType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderTypeCollection: IOrderType[] = [sampleWithRequiredData];
        expectedResult = service.addOrderTypeToCollectionIfMissing(orderTypeCollection, ...orderTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderType: IOrderType = sampleWithRequiredData;
        const orderType2: IOrderType = sampleWithPartialData;
        expectedResult = service.addOrderTypeToCollectionIfMissing([], orderType, orderType2);
        expect(expectedResult).toEqual([orderType, orderType2]);
      });

      it('should accept null and undefined values', () => {
        const orderType: IOrderType = sampleWithRequiredData;
        expectedResult = service.addOrderTypeToCollectionIfMissing([], null, orderType, undefined);
        expect(expectedResult).toEqual([orderType]);
      });

      it('should return initial array if no OrderType is added', () => {
        const orderTypeCollection: IOrderType[] = [sampleWithRequiredData];
        expectedResult = service.addOrderTypeToCollectionIfMissing(orderTypeCollection, undefined, null);
        expect(expectedResult).toEqual(orderTypeCollection);
      });
    });

    describe('compareOrderType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10129 };
        const entity2 = null;

        const compareResult1 = service.compareOrderType(entity1, entity2);
        const compareResult2 = service.compareOrderType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10129 };
        const entity2 = { id: 28377 };

        const compareResult1 = service.compareOrderType(entity1, entity2);
        const compareResult2 = service.compareOrderType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 10129 };
        const entity2 = { id: 10129 };

        const compareResult1 = service.compareOrderType(entity1, entity2);
        const compareResult2 = service.compareOrderType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
