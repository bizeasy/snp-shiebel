import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderShipment } from '../order-shipment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-shipment.test-samples';

import { OrderShipmentService } from './order-shipment.service';

const requireRestSample: IOrderShipment = {
  ...sampleWithRequiredData,
};

describe('OrderShipment Service', () => {
  let service: OrderShipmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderShipment | IOrderShipment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderShipmentService);
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

    it('should create a OrderShipment', () => {
      const orderShipment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderShipment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderShipment', () => {
      const orderShipment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderShipment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderShipment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderShipment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderShipment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderShipmentToCollectionIfMissing', () => {
      it('should add a OrderShipment to an empty array', () => {
        const orderShipment: IOrderShipment = sampleWithRequiredData;
        expectedResult = service.addOrderShipmentToCollectionIfMissing([], orderShipment);
        expect(expectedResult).toEqual([orderShipment]);
      });

      it('should not add a OrderShipment to an array that contains it', () => {
        const orderShipment: IOrderShipment = sampleWithRequiredData;
        const orderShipmentCollection: IOrderShipment[] = [
          {
            ...orderShipment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderShipmentToCollectionIfMissing(orderShipmentCollection, orderShipment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderShipment to an array that doesn't contain it", () => {
        const orderShipment: IOrderShipment = sampleWithRequiredData;
        const orderShipmentCollection: IOrderShipment[] = [sampleWithPartialData];
        expectedResult = service.addOrderShipmentToCollectionIfMissing(orderShipmentCollection, orderShipment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderShipment);
      });

      it('should add only unique OrderShipment to an array', () => {
        const orderShipmentArray: IOrderShipment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderShipmentCollection: IOrderShipment[] = [sampleWithRequiredData];
        expectedResult = service.addOrderShipmentToCollectionIfMissing(orderShipmentCollection, ...orderShipmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderShipment: IOrderShipment = sampleWithRequiredData;
        const orderShipment2: IOrderShipment = sampleWithPartialData;
        expectedResult = service.addOrderShipmentToCollectionIfMissing([], orderShipment, orderShipment2);
        expect(expectedResult).toEqual([orderShipment, orderShipment2]);
      });

      it('should accept null and undefined values', () => {
        const orderShipment: IOrderShipment = sampleWithRequiredData;
        expectedResult = service.addOrderShipmentToCollectionIfMissing([], null, orderShipment, undefined);
        expect(expectedResult).toEqual([orderShipment]);
      });

      it('should return initial array if no OrderShipment is added', () => {
        const orderShipmentCollection: IOrderShipment[] = [sampleWithRequiredData];
        expectedResult = service.addOrderShipmentToCollectionIfMissing(orderShipmentCollection, undefined, null);
        expect(expectedResult).toEqual(orderShipmentCollection);
      });
    });

    describe('compareOrderShipment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderShipment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15441 };
        const entity2 = null;

        const compareResult1 = service.compareOrderShipment(entity1, entity2);
        const compareResult2 = service.compareOrderShipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15441 };
        const entity2 = { id: 18335 };

        const compareResult1 = service.compareOrderShipment(entity1, entity2);
        const compareResult2 = service.compareOrderShipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 15441 };
        const entity2 = { id: 15441 };

        const compareResult1 = service.compareOrderShipment(entity1, entity2);
        const compareResult2 = service.compareOrderShipment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
