import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFeatureApplType } from '../product-feature-appl-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-feature-appl-type.test-samples';

import { ProductFeatureApplTypeService } from './product-feature-appl-type.service';

const requireRestSample: IProductFeatureApplType = {
  ...sampleWithRequiredData,
};

describe('ProductFeatureApplType Service', () => {
  let service: ProductFeatureApplTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFeatureApplType | IProductFeatureApplType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFeatureApplTypeService);
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

    it('should create a ProductFeatureApplType', () => {
      const productFeatureApplType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFeatureApplType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFeatureApplType', () => {
      const productFeatureApplType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFeatureApplType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFeatureApplType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFeatureApplType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFeatureApplType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFeatureApplTypeToCollectionIfMissing', () => {
      it('should add a ProductFeatureApplType to an empty array', () => {
        const productFeatureApplType: IProductFeatureApplType = sampleWithRequiredData;
        expectedResult = service.addProductFeatureApplTypeToCollectionIfMissing([], productFeatureApplType);
        expect(expectedResult).toEqual([productFeatureApplType]);
      });

      it('should not add a ProductFeatureApplType to an array that contains it', () => {
        const productFeatureApplType: IProductFeatureApplType = sampleWithRequiredData;
        const productFeatureApplTypeCollection: IProductFeatureApplType[] = [
          {
            ...productFeatureApplType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFeatureApplTypeToCollectionIfMissing(productFeatureApplTypeCollection, productFeatureApplType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFeatureApplType to an array that doesn't contain it", () => {
        const productFeatureApplType: IProductFeatureApplType = sampleWithRequiredData;
        const productFeatureApplTypeCollection: IProductFeatureApplType[] = [sampleWithPartialData];
        expectedResult = service.addProductFeatureApplTypeToCollectionIfMissing(productFeatureApplTypeCollection, productFeatureApplType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFeatureApplType);
      });

      it('should add only unique ProductFeatureApplType to an array', () => {
        const productFeatureApplTypeArray: IProductFeatureApplType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productFeatureApplTypeCollection: IProductFeatureApplType[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureApplTypeToCollectionIfMissing(
          productFeatureApplTypeCollection,
          ...productFeatureApplTypeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFeatureApplType: IProductFeatureApplType = sampleWithRequiredData;
        const productFeatureApplType2: IProductFeatureApplType = sampleWithPartialData;
        expectedResult = service.addProductFeatureApplTypeToCollectionIfMissing([], productFeatureApplType, productFeatureApplType2);
        expect(expectedResult).toEqual([productFeatureApplType, productFeatureApplType2]);
      });

      it('should accept null and undefined values', () => {
        const productFeatureApplType: IProductFeatureApplType = sampleWithRequiredData;
        expectedResult = service.addProductFeatureApplTypeToCollectionIfMissing([], null, productFeatureApplType, undefined);
        expect(expectedResult).toEqual([productFeatureApplType]);
      });

      it('should return initial array if no ProductFeatureApplType is added', () => {
        const productFeatureApplTypeCollection: IProductFeatureApplType[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureApplTypeToCollectionIfMissing(productFeatureApplTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productFeatureApplTypeCollection);
      });
    });

    describe('compareProductFeatureApplType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFeatureApplType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6261 };
        const entity2 = null;

        const compareResult1 = service.compareProductFeatureApplType(entity1, entity2);
        const compareResult2 = service.compareProductFeatureApplType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6261 };
        const entity2 = { id: 19218 };

        const compareResult1 = service.compareProductFeatureApplType(entity1, entity2);
        const compareResult2 = service.compareProductFeatureApplType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6261 };
        const entity2 = { id: 6261 };

        const compareResult1 = service.compareProductFeatureApplType(entity1, entity2);
        const compareResult2 = service.compareProductFeatureApplType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
