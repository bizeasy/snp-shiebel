import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductCategoryType } from '../product-category-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-category-type.test-samples';

import { ProductCategoryTypeService } from './product-category-type.service';

const requireRestSample: IProductCategoryType = {
  ...sampleWithRequiredData,
};

describe('ProductCategoryType Service', () => {
  let service: ProductCategoryTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductCategoryType | IProductCategoryType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductCategoryTypeService);
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

    it('should create a ProductCategoryType', () => {
      const productCategoryType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productCategoryType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductCategoryType', () => {
      const productCategoryType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productCategoryType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductCategoryType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductCategoryType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductCategoryType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductCategoryTypeToCollectionIfMissing', () => {
      it('should add a ProductCategoryType to an empty array', () => {
        const productCategoryType: IProductCategoryType = sampleWithRequiredData;
        expectedResult = service.addProductCategoryTypeToCollectionIfMissing([], productCategoryType);
        expect(expectedResult).toEqual([productCategoryType]);
      });

      it('should not add a ProductCategoryType to an array that contains it', () => {
        const productCategoryType: IProductCategoryType = sampleWithRequiredData;
        const productCategoryTypeCollection: IProductCategoryType[] = [
          {
            ...productCategoryType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductCategoryTypeToCollectionIfMissing(productCategoryTypeCollection, productCategoryType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductCategoryType to an array that doesn't contain it", () => {
        const productCategoryType: IProductCategoryType = sampleWithRequiredData;
        const productCategoryTypeCollection: IProductCategoryType[] = [sampleWithPartialData];
        expectedResult = service.addProductCategoryTypeToCollectionIfMissing(productCategoryTypeCollection, productCategoryType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productCategoryType);
      });

      it('should add only unique ProductCategoryType to an array', () => {
        const productCategoryTypeArray: IProductCategoryType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productCategoryTypeCollection: IProductCategoryType[] = [sampleWithRequiredData];
        expectedResult = service.addProductCategoryTypeToCollectionIfMissing(productCategoryTypeCollection, ...productCategoryTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productCategoryType: IProductCategoryType = sampleWithRequiredData;
        const productCategoryType2: IProductCategoryType = sampleWithPartialData;
        expectedResult = service.addProductCategoryTypeToCollectionIfMissing([], productCategoryType, productCategoryType2);
        expect(expectedResult).toEqual([productCategoryType, productCategoryType2]);
      });

      it('should accept null and undefined values', () => {
        const productCategoryType: IProductCategoryType = sampleWithRequiredData;
        expectedResult = service.addProductCategoryTypeToCollectionIfMissing([], null, productCategoryType, undefined);
        expect(expectedResult).toEqual([productCategoryType]);
      });

      it('should return initial array if no ProductCategoryType is added', () => {
        const productCategoryTypeCollection: IProductCategoryType[] = [sampleWithRequiredData];
        expectedResult = service.addProductCategoryTypeToCollectionIfMissing(productCategoryTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productCategoryTypeCollection);
      });
    });

    describe('compareProductCategoryType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductCategoryType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10914 };
        const entity2 = null;

        const compareResult1 = service.compareProductCategoryType(entity1, entity2);
        const compareResult2 = service.compareProductCategoryType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10914 };
        const entity2 = { id: 32619 };

        const compareResult1 = service.compareProductCategoryType(entity1, entity2);
        const compareResult2 = service.compareProductCategoryType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 10914 };
        const entity2 = { id: 10914 };

        const compareResult1 = service.compareProductCategoryType(entity1, entity2);
        const compareResult2 = service.compareProductCategoryType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
