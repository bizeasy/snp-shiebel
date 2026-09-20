import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductPrice } from '../product-price.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-price.test-samples';

import { ProductPriceService, RestProductPrice } from './product-price.service';

const requireRestSample: RestProductPrice = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('ProductPrice Service', () => {
  let service: ProductPriceService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductPrice | IProductPrice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductPriceService);
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

    it('should create a ProductPrice', () => {
      const productPrice = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productPrice).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductPrice', () => {
      const productPrice = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productPrice).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductPrice', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductPrice', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductPrice', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductPriceToCollectionIfMissing', () => {
      it('should add a ProductPrice to an empty array', () => {
        const productPrice: IProductPrice = sampleWithRequiredData;
        expectedResult = service.addProductPriceToCollectionIfMissing([], productPrice);
        expect(expectedResult).toEqual([productPrice]);
      });

      it('should not add a ProductPrice to an array that contains it', () => {
        const productPrice: IProductPrice = sampleWithRequiredData;
        const productPriceCollection: IProductPrice[] = [
          {
            ...productPrice,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductPriceToCollectionIfMissing(productPriceCollection, productPrice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductPrice to an array that doesn't contain it", () => {
        const productPrice: IProductPrice = sampleWithRequiredData;
        const productPriceCollection: IProductPrice[] = [sampleWithPartialData];
        expectedResult = service.addProductPriceToCollectionIfMissing(productPriceCollection, productPrice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productPrice);
      });

      it('should add only unique ProductPrice to an array', () => {
        const productPriceArray: IProductPrice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productPriceCollection: IProductPrice[] = [sampleWithRequiredData];
        expectedResult = service.addProductPriceToCollectionIfMissing(productPriceCollection, ...productPriceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productPrice: IProductPrice = sampleWithRequiredData;
        const productPrice2: IProductPrice = sampleWithPartialData;
        expectedResult = service.addProductPriceToCollectionIfMissing([], productPrice, productPrice2);
        expect(expectedResult).toEqual([productPrice, productPrice2]);
      });

      it('should accept null and undefined values', () => {
        const productPrice: IProductPrice = sampleWithRequiredData;
        expectedResult = service.addProductPriceToCollectionIfMissing([], null, productPrice, undefined);
        expect(expectedResult).toEqual([productPrice]);
      });

      it('should return initial array if no ProductPrice is added', () => {
        const productPriceCollection: IProductPrice[] = [sampleWithRequiredData];
        expectedResult = service.addProductPriceToCollectionIfMissing(productPriceCollection, undefined, null);
        expect(expectedResult).toEqual(productPriceCollection);
      });
    });

    describe('compareProductPrice', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductPrice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28388 };
        const entity2 = null;

        const compareResult1 = service.compareProductPrice(entity1, entity2);
        const compareResult2 = service.compareProductPrice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28388 };
        const entity2 = { id: 1931 };

        const compareResult1 = service.compareProductPrice(entity1, entity2);
        const compareResult2 = service.compareProductPrice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28388 };
        const entity2 = { id: 28388 };

        const compareResult1 = service.compareProductPrice(entity1, entity2);
        const compareResult2 = service.compareProductPrice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
