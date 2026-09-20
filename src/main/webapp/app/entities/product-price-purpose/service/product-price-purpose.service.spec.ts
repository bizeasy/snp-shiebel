import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductPricePurpose } from '../product-price-purpose.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-price-purpose.test-samples';

import { ProductPricePurposeService } from './product-price-purpose.service';

const requireRestSample: IProductPricePurpose = {
  ...sampleWithRequiredData,
};

describe('ProductPricePurpose Service', () => {
  let service: ProductPricePurposeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductPricePurpose | IProductPricePurpose[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductPricePurposeService);
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

    it('should create a ProductPricePurpose', () => {
      const productPricePurpose = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productPricePurpose).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductPricePurpose', () => {
      const productPricePurpose = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productPricePurpose).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductPricePurpose', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductPricePurpose', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductPricePurpose', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductPricePurposeToCollectionIfMissing', () => {
      it('should add a ProductPricePurpose to an empty array', () => {
        const productPricePurpose: IProductPricePurpose = sampleWithRequiredData;
        expectedResult = service.addProductPricePurposeToCollectionIfMissing([], productPricePurpose);
        expect(expectedResult).toEqual([productPricePurpose]);
      });

      it('should not add a ProductPricePurpose to an array that contains it', () => {
        const productPricePurpose: IProductPricePurpose = sampleWithRequiredData;
        const productPricePurposeCollection: IProductPricePurpose[] = [
          {
            ...productPricePurpose,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductPricePurposeToCollectionIfMissing(productPricePurposeCollection, productPricePurpose);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductPricePurpose to an array that doesn't contain it", () => {
        const productPricePurpose: IProductPricePurpose = sampleWithRequiredData;
        const productPricePurposeCollection: IProductPricePurpose[] = [sampleWithPartialData];
        expectedResult = service.addProductPricePurposeToCollectionIfMissing(productPricePurposeCollection, productPricePurpose);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productPricePurpose);
      });

      it('should add only unique ProductPricePurpose to an array', () => {
        const productPricePurposeArray: IProductPricePurpose[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productPricePurposeCollection: IProductPricePurpose[] = [sampleWithRequiredData];
        expectedResult = service.addProductPricePurposeToCollectionIfMissing(productPricePurposeCollection, ...productPricePurposeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productPricePurpose: IProductPricePurpose = sampleWithRequiredData;
        const productPricePurpose2: IProductPricePurpose = sampleWithPartialData;
        expectedResult = service.addProductPricePurposeToCollectionIfMissing([], productPricePurpose, productPricePurpose2);
        expect(expectedResult).toEqual([productPricePurpose, productPricePurpose2]);
      });

      it('should accept null and undefined values', () => {
        const productPricePurpose: IProductPricePurpose = sampleWithRequiredData;
        expectedResult = service.addProductPricePurposeToCollectionIfMissing([], null, productPricePurpose, undefined);
        expect(expectedResult).toEqual([productPricePurpose]);
      });

      it('should return initial array if no ProductPricePurpose is added', () => {
        const productPricePurposeCollection: IProductPricePurpose[] = [sampleWithRequiredData];
        expectedResult = service.addProductPricePurposeToCollectionIfMissing(productPricePurposeCollection, undefined, null);
        expect(expectedResult).toEqual(productPricePurposeCollection);
      });
    });

    describe('compareProductPricePurpose', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductPricePurpose(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8537 };
        const entity2 = null;

        const compareResult1 = service.compareProductPricePurpose(entity1, entity2);
        const compareResult2 = service.compareProductPricePurpose(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8537 };
        const entity2 = { id: 15993 };

        const compareResult1 = service.compareProductPricePurpose(entity1, entity2);
        const compareResult2 = service.compareProductPricePurpose(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8537 };
        const entity2 = { id: 8537 };

        const compareResult1 = service.compareProductPricePurpose(entity1, entity2);
        const compareResult2 = service.compareProductPricePurpose(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
