import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderContactMech } from '../order-contact-mech.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-contact-mech.test-samples';

import { OrderContactMechService, RestOrderContactMech } from './order-contact-mech.service';

const requireRestSample: RestOrderContactMech = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('OrderContactMech Service', () => {
  let service: OrderContactMechService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderContactMech | IOrderContactMech[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderContactMechService);
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

    it('should create a OrderContactMech', () => {
      const orderContactMech = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderContactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderContactMech', () => {
      const orderContactMech = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderContactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderContactMech', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderContactMech', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderContactMech', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderContactMechToCollectionIfMissing', () => {
      it('should add a OrderContactMech to an empty array', () => {
        const orderContactMech: IOrderContactMech = sampleWithRequiredData;
        expectedResult = service.addOrderContactMechToCollectionIfMissing([], orderContactMech);
        expect(expectedResult).toEqual([orderContactMech]);
      });

      it('should not add a OrderContactMech to an array that contains it', () => {
        const orderContactMech: IOrderContactMech = sampleWithRequiredData;
        const orderContactMechCollection: IOrderContactMech[] = [
          {
            ...orderContactMech,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderContactMechToCollectionIfMissing(orderContactMechCollection, orderContactMech);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderContactMech to an array that doesn't contain it", () => {
        const orderContactMech: IOrderContactMech = sampleWithRequiredData;
        const orderContactMechCollection: IOrderContactMech[] = [sampleWithPartialData];
        expectedResult = service.addOrderContactMechToCollectionIfMissing(orderContactMechCollection, orderContactMech);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderContactMech);
      });

      it('should add only unique OrderContactMech to an array', () => {
        const orderContactMechArray: IOrderContactMech[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderContactMechCollection: IOrderContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addOrderContactMechToCollectionIfMissing(orderContactMechCollection, ...orderContactMechArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderContactMech: IOrderContactMech = sampleWithRequiredData;
        const orderContactMech2: IOrderContactMech = sampleWithPartialData;
        expectedResult = service.addOrderContactMechToCollectionIfMissing([], orderContactMech, orderContactMech2);
        expect(expectedResult).toEqual([orderContactMech, orderContactMech2]);
      });

      it('should accept null and undefined values', () => {
        const orderContactMech: IOrderContactMech = sampleWithRequiredData;
        expectedResult = service.addOrderContactMechToCollectionIfMissing([], null, orderContactMech, undefined);
        expect(expectedResult).toEqual([orderContactMech]);
      });

      it('should return initial array if no OrderContactMech is added', () => {
        const orderContactMechCollection: IOrderContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addOrderContactMechToCollectionIfMissing(orderContactMechCollection, undefined, null);
        expect(expectedResult).toEqual(orderContactMechCollection);
      });
    });

    describe('compareOrderContactMech', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderContactMech(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 112 };
        const entity2 = null;

        const compareResult1 = service.compareOrderContactMech(entity1, entity2);
        const compareResult2 = service.compareOrderContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 112 };
        const entity2 = { id: 17412 };

        const compareResult1 = service.compareOrderContactMech(entity1, entity2);
        const compareResult2 = service.compareOrderContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 112 };
        const entity2 = { id: 112 };

        const compareResult1 = service.compareOrderContactMech(entity1, entity2);
        const compareResult2 = service.compareOrderContactMech(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
