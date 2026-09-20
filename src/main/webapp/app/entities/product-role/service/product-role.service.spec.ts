import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductRole } from '../product-role.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-role.test-samples';

import { ProductRoleService } from './product-role.service';

const requireRestSample: IProductRole = {
  ...sampleWithRequiredData,
};

describe('ProductRole Service', () => {
  let service: ProductRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductRole | IProductRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductRoleService);
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

    it('should create a ProductRole', () => {
      const productRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductRole', () => {
      const productRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductRole', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductRoleToCollectionIfMissing', () => {
      it('should add a ProductRole to an empty array', () => {
        const productRole: IProductRole = sampleWithRequiredData;
        expectedResult = service.addProductRoleToCollectionIfMissing([], productRole);
        expect(expectedResult).toEqual([productRole]);
      });

      it('should not add a ProductRole to an array that contains it', () => {
        const productRole: IProductRole = sampleWithRequiredData;
        const productRoleCollection: IProductRole[] = [
          {
            ...productRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductRoleToCollectionIfMissing(productRoleCollection, productRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductRole to an array that doesn't contain it", () => {
        const productRole: IProductRole = sampleWithRequiredData;
        const productRoleCollection: IProductRole[] = [sampleWithPartialData];
        expectedResult = service.addProductRoleToCollectionIfMissing(productRoleCollection, productRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productRole);
      });

      it('should add only unique ProductRole to an array', () => {
        const productRoleArray: IProductRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productRoleCollection: IProductRole[] = [sampleWithRequiredData];
        expectedResult = service.addProductRoleToCollectionIfMissing(productRoleCollection, ...productRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productRole: IProductRole = sampleWithRequiredData;
        const productRole2: IProductRole = sampleWithPartialData;
        expectedResult = service.addProductRoleToCollectionIfMissing([], productRole, productRole2);
        expect(expectedResult).toEqual([productRole, productRole2]);
      });

      it('should accept null and undefined values', () => {
        const productRole: IProductRole = sampleWithRequiredData;
        expectedResult = service.addProductRoleToCollectionIfMissing([], null, productRole, undefined);
        expect(expectedResult).toEqual([productRole]);
      });

      it('should return initial array if no ProductRole is added', () => {
        const productRoleCollection: IProductRole[] = [sampleWithRequiredData];
        expectedResult = service.addProductRoleToCollectionIfMissing(productRoleCollection, undefined, null);
        expect(expectedResult).toEqual(productRoleCollection);
      });
    });

    describe('compareProductRole', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7042 };
        const entity2 = null;

        const compareResult1 = service.compareProductRole(entity1, entity2);
        const compareResult2 = service.compareProductRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7042 };
        const entity2 = { id: 10277 };

        const compareResult1 = service.compareProductRole(entity1, entity2);
        const compareResult2 = service.compareProductRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7042 };
        const entity2 = { id: 7042 };

        const compareResult1 = service.compareProductRole(entity1, entity2);
        const compareResult2 = service.compareProductRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
