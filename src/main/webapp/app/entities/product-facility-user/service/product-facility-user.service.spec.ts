import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFacilityUser } from '../product-facility-user.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-facility-user.test-samples';

import { ProductFacilityUserService, RestProductFacilityUser } from './product-facility-user.service';

const requireRestSample: RestProductFacilityUser = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('ProductFacilityUser Service', () => {
  let service: ProductFacilityUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFacilityUser | IProductFacilityUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFacilityUserService);
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

    it('should create a ProductFacilityUser', () => {
      const productFacilityUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFacilityUser).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFacilityUser', () => {
      const productFacilityUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFacilityUser).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFacilityUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFacilityUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFacilityUser', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFacilityUserToCollectionIfMissing', () => {
      it('should add a ProductFacilityUser to an empty array', () => {
        const productFacilityUser: IProductFacilityUser = sampleWithRequiredData;
        expectedResult = service.addProductFacilityUserToCollectionIfMissing([], productFacilityUser);
        expect(expectedResult).toEqual([productFacilityUser]);
      });

      it('should not add a ProductFacilityUser to an array that contains it', () => {
        const productFacilityUser: IProductFacilityUser = sampleWithRequiredData;
        const productFacilityUserCollection: IProductFacilityUser[] = [
          {
            ...productFacilityUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFacilityUserToCollectionIfMissing(productFacilityUserCollection, productFacilityUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFacilityUser to an array that doesn't contain it", () => {
        const productFacilityUser: IProductFacilityUser = sampleWithRequiredData;
        const productFacilityUserCollection: IProductFacilityUser[] = [sampleWithPartialData];
        expectedResult = service.addProductFacilityUserToCollectionIfMissing(productFacilityUserCollection, productFacilityUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFacilityUser);
      });

      it('should add only unique ProductFacilityUser to an array', () => {
        const productFacilityUserArray: IProductFacilityUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productFacilityUserCollection: IProductFacilityUser[] = [sampleWithRequiredData];
        expectedResult = service.addProductFacilityUserToCollectionIfMissing(productFacilityUserCollection, ...productFacilityUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFacilityUser: IProductFacilityUser = sampleWithRequiredData;
        const productFacilityUser2: IProductFacilityUser = sampleWithPartialData;
        expectedResult = service.addProductFacilityUserToCollectionIfMissing([], productFacilityUser, productFacilityUser2);
        expect(expectedResult).toEqual([productFacilityUser, productFacilityUser2]);
      });

      it('should accept null and undefined values', () => {
        const productFacilityUser: IProductFacilityUser = sampleWithRequiredData;
        expectedResult = service.addProductFacilityUserToCollectionIfMissing([], null, productFacilityUser, undefined);
        expect(expectedResult).toEqual([productFacilityUser]);
      });

      it('should return initial array if no ProductFacilityUser is added', () => {
        const productFacilityUserCollection: IProductFacilityUser[] = [sampleWithRequiredData];
        expectedResult = service.addProductFacilityUserToCollectionIfMissing(productFacilityUserCollection, undefined, null);
        expect(expectedResult).toEqual(productFacilityUserCollection);
      });
    });

    describe('compareProductFacilityUser', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFacilityUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 234 };
        const entity2 = null;

        const compareResult1 = service.compareProductFacilityUser(entity1, entity2);
        const compareResult2 = service.compareProductFacilityUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 234 };
        const entity2 = { id: 16575 };

        const compareResult1 = service.compareProductFacilityUser(entity1, entity2);
        const compareResult2 = service.compareProductFacilityUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 234 };
        const entity2 = { id: 234 };

        const compareResult1 = service.compareProductFacilityUser(entity1, entity2);
        const compareResult2 = service.compareProductFacilityUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
