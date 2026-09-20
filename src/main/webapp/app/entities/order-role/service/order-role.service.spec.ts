import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderRole } from '../order-role.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-role.test-samples';

import { OrderRoleService } from './order-role.service';

const requireRestSample: IOrderRole = {
  ...sampleWithRequiredData,
};

describe('OrderRole Service', () => {
  let service: OrderRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderRole | IOrderRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderRoleService);
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

    it('should create a OrderRole', () => {
      const orderRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderRole', () => {
      const orderRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderRole', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderRoleToCollectionIfMissing', () => {
      it('should add a OrderRole to an empty array', () => {
        const orderRole: IOrderRole = sampleWithRequiredData;
        expectedResult = service.addOrderRoleToCollectionIfMissing([], orderRole);
        expect(expectedResult).toEqual([orderRole]);
      });

      it('should not add a OrderRole to an array that contains it', () => {
        const orderRole: IOrderRole = sampleWithRequiredData;
        const orderRoleCollection: IOrderRole[] = [
          {
            ...orderRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderRoleToCollectionIfMissing(orderRoleCollection, orderRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderRole to an array that doesn't contain it", () => {
        const orderRole: IOrderRole = sampleWithRequiredData;
        const orderRoleCollection: IOrderRole[] = [sampleWithPartialData];
        expectedResult = service.addOrderRoleToCollectionIfMissing(orderRoleCollection, orderRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderRole);
      });

      it('should add only unique OrderRole to an array', () => {
        const orderRoleArray: IOrderRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderRoleCollection: IOrderRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrderRoleToCollectionIfMissing(orderRoleCollection, ...orderRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderRole: IOrderRole = sampleWithRequiredData;
        const orderRole2: IOrderRole = sampleWithPartialData;
        expectedResult = service.addOrderRoleToCollectionIfMissing([], orderRole, orderRole2);
        expect(expectedResult).toEqual([orderRole, orderRole2]);
      });

      it('should accept null and undefined values', () => {
        const orderRole: IOrderRole = sampleWithRequiredData;
        expectedResult = service.addOrderRoleToCollectionIfMissing([], null, orderRole, undefined);
        expect(expectedResult).toEqual([orderRole]);
      });

      it('should return initial array if no OrderRole is added', () => {
        const orderRoleCollection: IOrderRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrderRoleToCollectionIfMissing(orderRoleCollection, undefined, null);
        expect(expectedResult).toEqual(orderRoleCollection);
      });
    });

    describe('compareOrderRole', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7537 };
        const entity2 = null;

        const compareResult1 = service.compareOrderRole(entity1, entity2);
        const compareResult2 = service.compareOrderRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7537 };
        const entity2 = { id: 29291 };

        const compareResult1 = service.compareOrderRole(entity1, entity2);
        const compareResult2 = service.compareOrderRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7537 };
        const entity2 = { id: 7537 };

        const compareResult1 = service.compareOrderRole(entity1, entity2);
        const compareResult2 = service.compareOrderRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
