import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductPriceType } from '../product-price-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-price-type.test-samples';

import { ProductPriceTypeService } from './product-price-type.service';

const requireRestSample: IProductPriceType = {
  ...sampleWithRequiredData,
};

describe('ProductPriceType Service', () => {
  let service: ProductPriceTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductPriceType | IProductPriceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductPriceTypeService);
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

    it('should create a ProductPriceType', () => {
      const productPriceType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productPriceType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductPriceType', () => {
      const productPriceType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productPriceType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductPriceType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductPriceType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductPriceType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductPriceTypeToCollectionIfMissing', () => {
      it('should add a ProductPriceType to an empty array', () => {
        const productPriceType: IProductPriceType = sampleWithRequiredData;
        expectedResult = service.addProductPriceTypeToCollectionIfMissing([], productPriceType);
        expect(expectedResult).toEqual([productPriceType]);
      });

      it('should not add a ProductPriceType to an array that contains it', () => {
        const productPriceType: IProductPriceType = sampleWithRequiredData;
        const productPriceTypeCollection: IProductPriceType[] = [
          {
            ...productPriceType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductPriceTypeToCollectionIfMissing(productPriceTypeCollection, productPriceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductPriceType to an array that doesn't contain it", () => {
        const productPriceType: IProductPriceType = sampleWithRequiredData;
        const productPriceTypeCollection: IProductPriceType[] = [sampleWithPartialData];
        expectedResult = service.addProductPriceTypeToCollectionIfMissing(productPriceTypeCollection, productPriceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productPriceType);
      });

      it('should add only unique ProductPriceType to an array', () => {
        const productPriceTypeArray: IProductPriceType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productPriceTypeCollection: IProductPriceType[] = [sampleWithRequiredData];
        expectedResult = service.addProductPriceTypeToCollectionIfMissing(productPriceTypeCollection, ...productPriceTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productPriceType: IProductPriceType = sampleWithRequiredData;
        const productPriceType2: IProductPriceType = sampleWithPartialData;
        expectedResult = service.addProductPriceTypeToCollectionIfMissing([], productPriceType, productPriceType2);
        expect(expectedResult).toEqual([productPriceType, productPriceType2]);
      });

      it('should accept null and undefined values', () => {
        const productPriceType: IProductPriceType = sampleWithRequiredData;
        expectedResult = service.addProductPriceTypeToCollectionIfMissing([], null, productPriceType, undefined);
        expect(expectedResult).toEqual([productPriceType]);
      });

      it('should return initial array if no ProductPriceType is added', () => {
        const productPriceTypeCollection: IProductPriceType[] = [sampleWithRequiredData];
        expectedResult = service.addProductPriceTypeToCollectionIfMissing(productPriceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productPriceTypeCollection);
      });
    });

    describe('compareProductPriceType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductPriceType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18810 };
        const entity2 = null;

        const compareResult1 = service.compareProductPriceType(entity1, entity2);
        const compareResult2 = service.compareProductPriceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18810 };
        const entity2 = { id: 31296 };

        const compareResult1 = service.compareProductPriceType(entity1, entity2);
        const compareResult2 = service.compareProductPriceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18810 };
        const entity2 = { id: 18810 };

        const compareResult1 = service.compareProductPriceType(entity1, entity2);
        const compareResult2 = service.compareProductPriceType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
