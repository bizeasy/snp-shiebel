import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFeatureType } from '../product-feature-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-feature-type.test-samples';

import { ProductFeatureTypeService } from './product-feature-type.service';

const requireRestSample: IProductFeatureType = {
  ...sampleWithRequiredData,
};

describe('ProductFeatureType Service', () => {
  let service: ProductFeatureTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFeatureType | IProductFeatureType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFeatureTypeService);
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

    it('should create a ProductFeatureType', () => {
      const productFeatureType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFeatureType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFeatureType', () => {
      const productFeatureType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFeatureType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFeatureType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFeatureType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFeatureType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFeatureTypeToCollectionIfMissing', () => {
      it('should add a ProductFeatureType to an empty array', () => {
        const productFeatureType: IProductFeatureType = sampleWithRequiredData;
        expectedResult = service.addProductFeatureTypeToCollectionIfMissing([], productFeatureType);
        expect(expectedResult).toEqual([productFeatureType]);
      });

      it('should not add a ProductFeatureType to an array that contains it', () => {
        const productFeatureType: IProductFeatureType = sampleWithRequiredData;
        const productFeatureTypeCollection: IProductFeatureType[] = [
          {
            ...productFeatureType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFeatureTypeToCollectionIfMissing(productFeatureTypeCollection, productFeatureType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFeatureType to an array that doesn't contain it", () => {
        const productFeatureType: IProductFeatureType = sampleWithRequiredData;
        const productFeatureTypeCollection: IProductFeatureType[] = [sampleWithPartialData];
        expectedResult = service.addProductFeatureTypeToCollectionIfMissing(productFeatureTypeCollection, productFeatureType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFeatureType);
      });

      it('should add only unique ProductFeatureType to an array', () => {
        const productFeatureTypeArray: IProductFeatureType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productFeatureTypeCollection: IProductFeatureType[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureTypeToCollectionIfMissing(productFeatureTypeCollection, ...productFeatureTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFeatureType: IProductFeatureType = sampleWithRequiredData;
        const productFeatureType2: IProductFeatureType = sampleWithPartialData;
        expectedResult = service.addProductFeatureTypeToCollectionIfMissing([], productFeatureType, productFeatureType2);
        expect(expectedResult).toEqual([productFeatureType, productFeatureType2]);
      });

      it('should accept null and undefined values', () => {
        const productFeatureType: IProductFeatureType = sampleWithRequiredData;
        expectedResult = service.addProductFeatureTypeToCollectionIfMissing([], null, productFeatureType, undefined);
        expect(expectedResult).toEqual([productFeatureType]);
      });

      it('should return initial array if no ProductFeatureType is added', () => {
        const productFeatureTypeCollection: IProductFeatureType[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureTypeToCollectionIfMissing(productFeatureTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productFeatureTypeCollection);
      });
    });

    describe('compareProductFeatureType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFeatureType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29389 };
        const entity2 = null;

        const compareResult1 = service.compareProductFeatureType(entity1, entity2);
        const compareResult2 = service.compareProductFeatureType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29389 };
        const entity2 = { id: 20310 };

        const compareResult1 = service.compareProductFeatureType(entity1, entity2);
        const compareResult2 = service.compareProductFeatureType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 29389 };
        const entity2 = { id: 29389 };

        const compareResult1 = service.compareProductFeatureType(entity1, entity2);
        const compareResult2 = service.compareProductFeatureType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
