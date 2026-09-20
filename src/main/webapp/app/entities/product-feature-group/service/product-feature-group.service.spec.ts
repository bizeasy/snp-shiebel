import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductFeatureGroup } from '../product-feature-group.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../product-feature-group.test-samples';

import { ProductFeatureGroupService } from './product-feature-group.service';

const requireRestSample: IProductFeatureGroup = {
  ...sampleWithRequiredData,
};

describe('ProductFeatureGroup Service', () => {
  let service: ProductFeatureGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductFeatureGroup | IProductFeatureGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductFeatureGroupService);
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

    it('should create a ProductFeatureGroup', () => {
      const productFeatureGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productFeatureGroup).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductFeatureGroup', () => {
      const productFeatureGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productFeatureGroup).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductFeatureGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductFeatureGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductFeatureGroup', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductFeatureGroupToCollectionIfMissing', () => {
      it('should add a ProductFeatureGroup to an empty array', () => {
        const productFeatureGroup: IProductFeatureGroup = sampleWithRequiredData;
        expectedResult = service.addProductFeatureGroupToCollectionIfMissing([], productFeatureGroup);
        expect(expectedResult).toEqual([productFeatureGroup]);
      });

      it('should not add a ProductFeatureGroup to an array that contains it', () => {
        const productFeatureGroup: IProductFeatureGroup = sampleWithRequiredData;
        const productFeatureGroupCollection: IProductFeatureGroup[] = [
          {
            ...productFeatureGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductFeatureGroupToCollectionIfMissing(productFeatureGroupCollection, productFeatureGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductFeatureGroup to an array that doesn't contain it", () => {
        const productFeatureGroup: IProductFeatureGroup = sampleWithRequiredData;
        const productFeatureGroupCollection: IProductFeatureGroup[] = [sampleWithPartialData];
        expectedResult = service.addProductFeatureGroupToCollectionIfMissing(productFeatureGroupCollection, productFeatureGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productFeatureGroup);
      });

      it('should add only unique ProductFeatureGroup to an array', () => {
        const productFeatureGroupArray: IProductFeatureGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productFeatureGroupCollection: IProductFeatureGroup[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureGroupToCollectionIfMissing(productFeatureGroupCollection, ...productFeatureGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productFeatureGroup: IProductFeatureGroup = sampleWithRequiredData;
        const productFeatureGroup2: IProductFeatureGroup = sampleWithPartialData;
        expectedResult = service.addProductFeatureGroupToCollectionIfMissing([], productFeatureGroup, productFeatureGroup2);
        expect(expectedResult).toEqual([productFeatureGroup, productFeatureGroup2]);
      });

      it('should accept null and undefined values', () => {
        const productFeatureGroup: IProductFeatureGroup = sampleWithRequiredData;
        expectedResult = service.addProductFeatureGroupToCollectionIfMissing([], null, productFeatureGroup, undefined);
        expect(expectedResult).toEqual([productFeatureGroup]);
      });

      it('should return initial array if no ProductFeatureGroup is added', () => {
        const productFeatureGroupCollection: IProductFeatureGroup[] = [sampleWithRequiredData];
        expectedResult = service.addProductFeatureGroupToCollectionIfMissing(productFeatureGroupCollection, undefined, null);
        expect(expectedResult).toEqual(productFeatureGroupCollection);
      });
    });

    describe('compareProductFeatureGroup', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductFeatureGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11544 };
        const entity2 = null;

        const compareResult1 = service.compareProductFeatureGroup(entity1, entity2);
        const compareResult2 = service.compareProductFeatureGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11544 };
        const entity2 = { id: 27826 };

        const compareResult1 = service.compareProductFeatureGroup(entity1, entity2);
        const compareResult2 = service.compareProductFeatureGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 11544 };
        const entity2 = { id: 11544 };

        const compareResult1 = service.compareProductFeatureGroup(entity1, entity2);
        const compareResult2 = service.compareProductFeatureGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
