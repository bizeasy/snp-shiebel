import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductType } from '../product-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-type.test-samples';

import { ProductTypeService } from './product-type.service';

const requireRestSample: IProductType = {
  ...sampleWithRequiredData,
};

describe('ProductType Service', () => {
  let service: ProductTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductType | IProductType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductTypeService);
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

    it('should create a ProductType', () => {
      const productType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductType', () => {
      const productType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductTypeToCollectionIfMissing', () => {
      it('should add a ProductType to an empty array', () => {
        const productType: IProductType = sampleWithRequiredData;
        expectedResult = service.addProductTypeToCollectionIfMissing([], productType);
        expect(expectedResult).toEqual([productType]);
      });

      it('should not add a ProductType to an array that contains it', () => {
        const productType: IProductType = sampleWithRequiredData;
        const productTypeCollection: IProductType[] = [
          {
            ...productType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, productType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductType to an array that doesn't contain it", () => {
        const productType: IProductType = sampleWithRequiredData;
        const productTypeCollection: IProductType[] = [sampleWithPartialData];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, productType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productType);
      });

      it('should add only unique ProductType to an array', () => {
        const productTypeArray: IProductType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productTypeCollection: IProductType[] = [sampleWithRequiredData];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, ...productTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productType: IProductType = sampleWithRequiredData;
        const productType2: IProductType = sampleWithPartialData;
        expectedResult = service.addProductTypeToCollectionIfMissing([], productType, productType2);
        expect(expectedResult).toEqual([productType, productType2]);
      });

      it('should accept null and undefined values', () => {
        const productType: IProductType = sampleWithRequiredData;
        expectedResult = service.addProductTypeToCollectionIfMissing([], null, productType, undefined);
        expect(expectedResult).toEqual([productType]);
      });

      it('should return initial array if no ProductType is added', () => {
        const productTypeCollection: IProductType[] = [sampleWithRequiredData];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productTypeCollection);
      });
    });

    describe('compareProductType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28647 };
        const entity2 = null;

        const compareResult1 = service.compareProductType(entity1, entity2);
        const compareResult2 = service.compareProductType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28647 };
        const entity2 = { id: 20361 };

        const compareResult1 = service.compareProductType(entity1, entity2);
        const compareResult2 = service.compareProductType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28647 };
        const entity2 = { id: 28647 };

        const compareResult1 = service.compareProductType(entity1, entity2);
        const compareResult2 = service.compareProductType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
