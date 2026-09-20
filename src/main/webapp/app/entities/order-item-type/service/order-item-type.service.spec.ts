import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderItemType } from '../order-item-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-item-type.test-samples';

import { OrderItemTypeService } from './order-item-type.service';

const requireRestSample: IOrderItemType = {
  ...sampleWithRequiredData,
};

describe('OrderItemType Service', () => {
  let service: OrderItemTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderItemType | IOrderItemType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderItemTypeService);
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

    it('should create a OrderItemType', () => {
      const orderItemType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderItemType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderItemType', () => {
      const orderItemType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderItemType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderItemType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderItemType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderItemType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderItemTypeToCollectionIfMissing', () => {
      it('should add a OrderItemType to an empty array', () => {
        const orderItemType: IOrderItemType = sampleWithRequiredData;
        expectedResult = service.addOrderItemTypeToCollectionIfMissing([], orderItemType);
        expect(expectedResult).toEqual([orderItemType]);
      });

      it('should not add a OrderItemType to an array that contains it', () => {
        const orderItemType: IOrderItemType = sampleWithRequiredData;
        const orderItemTypeCollection: IOrderItemType[] = [
          {
            ...orderItemType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderItemTypeToCollectionIfMissing(orderItemTypeCollection, orderItemType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderItemType to an array that doesn't contain it", () => {
        const orderItemType: IOrderItemType = sampleWithRequiredData;
        const orderItemTypeCollection: IOrderItemType[] = [sampleWithPartialData];
        expectedResult = service.addOrderItemTypeToCollectionIfMissing(orderItemTypeCollection, orderItemType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderItemType);
      });

      it('should add only unique OrderItemType to an array', () => {
        const orderItemTypeArray: IOrderItemType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderItemTypeCollection: IOrderItemType[] = [sampleWithRequiredData];
        expectedResult = service.addOrderItemTypeToCollectionIfMissing(orderItemTypeCollection, ...orderItemTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderItemType: IOrderItemType = sampleWithRequiredData;
        const orderItemType2: IOrderItemType = sampleWithPartialData;
        expectedResult = service.addOrderItemTypeToCollectionIfMissing([], orderItemType, orderItemType2);
        expect(expectedResult).toEqual([orderItemType, orderItemType2]);
      });

      it('should accept null and undefined values', () => {
        const orderItemType: IOrderItemType = sampleWithRequiredData;
        expectedResult = service.addOrderItemTypeToCollectionIfMissing([], null, orderItemType, undefined);
        expect(expectedResult).toEqual([orderItemType]);
      });

      it('should return initial array if no OrderItemType is added', () => {
        const orderItemTypeCollection: IOrderItemType[] = [sampleWithRequiredData];
        expectedResult = service.addOrderItemTypeToCollectionIfMissing(orderItemTypeCollection, undefined, null);
        expect(expectedResult).toEqual(orderItemTypeCollection);
      });
    });

    describe('compareOrderItemType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderItemType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31138 };
        const entity2 = null;

        const compareResult1 = service.compareOrderItemType(entity1, entity2);
        const compareResult2 = service.compareOrderItemType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31138 };
        const entity2 = { id: 18748 };

        const compareResult1 = service.compareOrderItemType(entity1, entity2);
        const compareResult2 = service.compareOrderItemType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 31138 };
        const entity2 = { id: 31138 };

        const compareResult1 = service.compareOrderItemType(entity1, entity2);
        const compareResult2 = service.compareOrderItemType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
