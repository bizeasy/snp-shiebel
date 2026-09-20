import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFacility } from '../product-facility.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-facility.test-samples';

import { ProductFacilityService } from './product-facility.service';

const requireRestSample: IProductFacility = {
  ...sampleWithRequiredData,
};

describe('ProductFacility Service', () => {
  let service: ProductFacilityService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFacility | IProductFacility[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFacilityService);
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

    it('should create a ProductFacility', () => {
      const productFacility = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFacility).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFacility', () => {
      const productFacility = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFacility).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFacility', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFacility', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFacility', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFacilityToCollectionIfMissing', () => {
      it('should add a ProductFacility to an empty array', () => {
        const productFacility: IProductFacility = sampleWithRequiredData;
        expectedResult = service.addProductFacilityToCollectionIfMissing([], productFacility);
        expect(expectedResult).toEqual([productFacility]);
      });

      it('should not add a ProductFacility to an array that contains it', () => {
        const productFacility: IProductFacility = sampleWithRequiredData;
        const productFacilityCollection: IProductFacility[] = [
          {
            ...productFacility,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFacilityToCollectionIfMissing(productFacilityCollection, productFacility);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFacility to an array that doesn't contain it", () => {
        const productFacility: IProductFacility = sampleWithRequiredData;
        const productFacilityCollection: IProductFacility[] = [sampleWithPartialData];
        expectedResult = service.addProductFacilityToCollectionIfMissing(productFacilityCollection, productFacility);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFacility);
      });

      it('should add only unique ProductFacility to an array', () => {
        const productFacilityArray: IProductFacility[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productFacilityCollection: IProductFacility[] = [sampleWithRequiredData];
        expectedResult = service.addProductFacilityToCollectionIfMissing(productFacilityCollection, ...productFacilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFacility: IProductFacility = sampleWithRequiredData;
        const productFacility2: IProductFacility = sampleWithPartialData;
        expectedResult = service.addProductFacilityToCollectionIfMissing([], productFacility, productFacility2);
        expect(expectedResult).toEqual([productFacility, productFacility2]);
      });

      it('should accept null and undefined values', () => {
        const productFacility: IProductFacility = sampleWithRequiredData;
        expectedResult = service.addProductFacilityToCollectionIfMissing([], null, productFacility, undefined);
        expect(expectedResult).toEqual([productFacility]);
      });

      it('should return initial array if no ProductFacility is added', () => {
        const productFacilityCollection: IProductFacility[] = [sampleWithRequiredData];
        expectedResult = service.addProductFacilityToCollectionIfMissing(productFacilityCollection, undefined, null);
        expect(expectedResult).toEqual(productFacilityCollection);
      });
    });

    describe('compareProductFacility', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFacility(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30505 };
        const entity2 = null;

        const compareResult1 = service.compareProductFacility(entity1, entity2);
        const compareResult2 = service.compareProductFacility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30505 };
        const entity2 = { id: 4242 };

        const compareResult1 = service.compareProductFacility(entity1, entity2);
        const compareResult2 = service.compareProductFacility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 30505 };
        const entity2 = { id: 30505 };

        const compareResult1 = service.compareProductFacility(entity1, entity2);
        const compareResult2 = service.compareProductFacility(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
