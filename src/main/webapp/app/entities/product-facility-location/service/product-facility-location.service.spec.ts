import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFacilityLocation } from '../product-facility-location.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-facility-location.test-samples';

import { ProductFacilityLocationService } from './product-facility-location.service';

const requireRestSample: IProductFacilityLocation = {
  ...sampleWithRequiredData,
};

describe('ProductFacilityLocation Service', () => {
  let service: ProductFacilityLocationService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFacilityLocation | IProductFacilityLocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFacilityLocationService);
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

    it('should create a ProductFacilityLocation', () => {
      const productFacilityLocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFacilityLocation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFacilityLocation', () => {
      const productFacilityLocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFacilityLocation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFacilityLocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFacilityLocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFacilityLocation', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFacilityLocationToCollectionIfMissing', () => {
      it('should add a ProductFacilityLocation to an empty array', () => {
        const productFacilityLocation: IProductFacilityLocation = sampleWithRequiredData;
        expectedResult = service.addProductFacilityLocationToCollectionIfMissing([], productFacilityLocation);
        expect(expectedResult).toEqual([productFacilityLocation]);
      });

      it('should not add a ProductFacilityLocation to an array that contains it', () => {
        const productFacilityLocation: IProductFacilityLocation = sampleWithRequiredData;
        const productFacilityLocationCollection: IProductFacilityLocation[] = [
          {
            ...productFacilityLocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFacilityLocationToCollectionIfMissing(
          productFacilityLocationCollection,
          productFacilityLocation,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFacilityLocation to an array that doesn't contain it", () => {
        const productFacilityLocation: IProductFacilityLocation = sampleWithRequiredData;
        const productFacilityLocationCollection: IProductFacilityLocation[] = [sampleWithPartialData];
        expectedResult = service.addProductFacilityLocationToCollectionIfMissing(
          productFacilityLocationCollection,
          productFacilityLocation,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFacilityLocation);
      });

      it('should add only unique ProductFacilityLocation to an array', () => {
        const productFacilityLocationArray: IProductFacilityLocation[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const productFacilityLocationCollection: IProductFacilityLocation[] = [sampleWithRequiredData];
        expectedResult = service.addProductFacilityLocationToCollectionIfMissing(
          productFacilityLocationCollection,
          ...productFacilityLocationArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFacilityLocation: IProductFacilityLocation = sampleWithRequiredData;
        const productFacilityLocation2: IProductFacilityLocation = sampleWithPartialData;
        expectedResult = service.addProductFacilityLocationToCollectionIfMissing([], productFacilityLocation, productFacilityLocation2);
        expect(expectedResult).toEqual([productFacilityLocation, productFacilityLocation2]);
      });

      it('should accept null and undefined values', () => {
        const productFacilityLocation: IProductFacilityLocation = sampleWithRequiredData;
        expectedResult = service.addProductFacilityLocationToCollectionIfMissing([], null, productFacilityLocation, undefined);
        expect(expectedResult).toEqual([productFacilityLocation]);
      });

      it('should return initial array if no ProductFacilityLocation is added', () => {
        const productFacilityLocationCollection: IProductFacilityLocation[] = [sampleWithRequiredData];
        expectedResult = service.addProductFacilityLocationToCollectionIfMissing(productFacilityLocationCollection, undefined, null);
        expect(expectedResult).toEqual(productFacilityLocationCollection);
      });
    });

    describe('compareProductFacilityLocation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFacilityLocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22461 };
        const entity2 = null;

        const compareResult1 = service.compareProductFacilityLocation(entity1, entity2);
        const compareResult2 = service.compareProductFacilityLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22461 };
        const entity2 = { id: 15870 };

        const compareResult1 = service.compareProductFacilityLocation(entity1, entity2);
        const compareResult2 = service.compareProductFacilityLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 22461 };
        const entity2 = { id: 22461 };

        const compareResult1 = service.compareProductFacilityLocation(entity1, entity2);
        const compareResult2 = service.compareProductFacilityLocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
