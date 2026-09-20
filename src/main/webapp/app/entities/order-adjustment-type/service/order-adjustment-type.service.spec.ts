import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderAdjustmentType } from '../order-adjustment-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../order-adjustment-type.test-samples';

import { OrderAdjustmentTypeService } from './order-adjustment-type.service';

const requireRestSample: IOrderAdjustmentType = {
  ...sampleWithRequiredData,
};

describe('OrderAdjustmentType Service', () => {
  let service: OrderAdjustmentTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderAdjustmentType | IOrderAdjustmentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderAdjustmentTypeService);
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

    it('should create a OrderAdjustmentType', () => {
      const orderAdjustmentType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderAdjustmentType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderAdjustmentType', () => {
      const orderAdjustmentType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderAdjustmentType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderAdjustmentType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderAdjustmentType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderAdjustmentType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderAdjustmentTypeToCollectionIfMissing', () => {
      it('should add a OrderAdjustmentType to an empty array', () => {
        const orderAdjustmentType: IOrderAdjustmentType = sampleWithRequiredData;
        expectedResult = service.addOrderAdjustmentTypeToCollectionIfMissing([], orderAdjustmentType);
        expect(expectedResult).toEqual([orderAdjustmentType]);
      });

      it('should not add a OrderAdjustmentType to an array that contains it', () => {
        const orderAdjustmentType: IOrderAdjustmentType = sampleWithRequiredData;
        const orderAdjustmentTypeCollection: IOrderAdjustmentType[] = [
          {
            ...orderAdjustmentType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderAdjustmentTypeToCollectionIfMissing(orderAdjustmentTypeCollection, orderAdjustmentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderAdjustmentType to an array that doesn't contain it", () => {
        const orderAdjustmentType: IOrderAdjustmentType = sampleWithRequiredData;
        const orderAdjustmentTypeCollection: IOrderAdjustmentType[] = [sampleWithPartialData];
        expectedResult = service.addOrderAdjustmentTypeToCollectionIfMissing(orderAdjustmentTypeCollection, orderAdjustmentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderAdjustmentType);
      });

      it('should add only unique OrderAdjustmentType to an array', () => {
        const orderAdjustmentTypeArray: IOrderAdjustmentType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderAdjustmentTypeCollection: IOrderAdjustmentType[] = [sampleWithRequiredData];
        expectedResult = service.addOrderAdjustmentTypeToCollectionIfMissing(orderAdjustmentTypeCollection, ...orderAdjustmentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderAdjustmentType: IOrderAdjustmentType = sampleWithRequiredData;
        const orderAdjustmentType2: IOrderAdjustmentType = sampleWithPartialData;
        expectedResult = service.addOrderAdjustmentTypeToCollectionIfMissing([], orderAdjustmentType, orderAdjustmentType2);
        expect(expectedResult).toEqual([orderAdjustmentType, orderAdjustmentType2]);
      });

      it('should accept null and undefined values', () => {
        const orderAdjustmentType: IOrderAdjustmentType = sampleWithRequiredData;
        expectedResult = service.addOrderAdjustmentTypeToCollectionIfMissing([], null, orderAdjustmentType, undefined);
        expect(expectedResult).toEqual([orderAdjustmentType]);
      });

      it('should return initial array if no OrderAdjustmentType is added', () => {
        const orderAdjustmentTypeCollection: IOrderAdjustmentType[] = [sampleWithRequiredData];
        expectedResult = service.addOrderAdjustmentTypeToCollectionIfMissing(orderAdjustmentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(orderAdjustmentTypeCollection);
      });
    });

    describe('compareOrderAdjustmentType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderAdjustmentType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1453 };
        const entity2 = null;

        const compareResult1 = service.compareOrderAdjustmentType(entity1, entity2);
        const compareResult2 = service.compareOrderAdjustmentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1453 };
        const entity2 = { id: 11072 };

        const compareResult1 = service.compareOrderAdjustmentType(entity1, entity2);
        const compareResult2 = service.compareOrderAdjustmentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 1453 };
        const entity2 = { id: 1453 };

        const compareResult1 = service.compareOrderAdjustmentType(entity1, entity2);
        const compareResult2 = service.compareOrderAdjustmentType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
