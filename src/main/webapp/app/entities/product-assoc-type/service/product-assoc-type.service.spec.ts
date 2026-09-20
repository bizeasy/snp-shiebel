import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductAssocType } from '../product-assoc-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-assoc-type.test-samples';

import { ProductAssocTypeService } from './product-assoc-type.service';

const requireRestSample: IProductAssocType = {
  ...sampleWithRequiredData,
};

describe('ProductAssocType Service', () => {
  let service: ProductAssocTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductAssocType | IProductAssocType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductAssocTypeService);
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

    it('should create a ProductAssocType', () => {
      const productAssocType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productAssocType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductAssocType', () => {
      const productAssocType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productAssocType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductAssocType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductAssocType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductAssocType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductAssocTypeToCollectionIfMissing', () => {
      it('should add a ProductAssocType to an empty array', () => {
        const productAssocType: IProductAssocType = sampleWithRequiredData;
        expectedResult = service.addProductAssocTypeToCollectionIfMissing([], productAssocType);
        expect(expectedResult).toEqual([productAssocType]);
      });

      it('should not add a ProductAssocType to an array that contains it', () => {
        const productAssocType: IProductAssocType = sampleWithRequiredData;
        const productAssocTypeCollection: IProductAssocType[] = [
          {
            ...productAssocType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductAssocTypeToCollectionIfMissing(productAssocTypeCollection, productAssocType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductAssocType to an array that doesn't contain it", () => {
        const productAssocType: IProductAssocType = sampleWithRequiredData;
        const productAssocTypeCollection: IProductAssocType[] = [sampleWithPartialData];
        expectedResult = service.addProductAssocTypeToCollectionIfMissing(productAssocTypeCollection, productAssocType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productAssocType);
      });

      it('should add only unique ProductAssocType to an array', () => {
        const productAssocTypeArray: IProductAssocType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productAssocTypeCollection: IProductAssocType[] = [sampleWithRequiredData];
        expectedResult = service.addProductAssocTypeToCollectionIfMissing(productAssocTypeCollection, ...productAssocTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productAssocType: IProductAssocType = sampleWithRequiredData;
        const productAssocType2: IProductAssocType = sampleWithPartialData;
        expectedResult = service.addProductAssocTypeToCollectionIfMissing([], productAssocType, productAssocType2);
        expect(expectedResult).toEqual([productAssocType, productAssocType2]);
      });

      it('should accept null and undefined values', () => {
        const productAssocType: IProductAssocType = sampleWithRequiredData;
        expectedResult = service.addProductAssocTypeToCollectionIfMissing([], null, productAssocType, undefined);
        expect(expectedResult).toEqual([productAssocType]);
      });

      it('should return initial array if no ProductAssocType is added', () => {
        const productAssocTypeCollection: IProductAssocType[] = [sampleWithRequiredData];
        expectedResult = service.addProductAssocTypeToCollectionIfMissing(productAssocTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productAssocTypeCollection);
      });
    });

    describe('compareProductAssocType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductAssocType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15174 };
        const entity2 = null;

        const compareResult1 = service.compareProductAssocType(entity1, entity2);
        const compareResult2 = service.compareProductAssocType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15174 };
        const entity2 = { id: 4250 };

        const compareResult1 = service.compareProductAssocType(entity1, entity2);
        const compareResult2 = service.compareProductAssocType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 15174 };
        const entity2 = { id: 15174 };

        const compareResult1 = service.compareProductAssocType(entity1, entity2);
        const compareResult2 = service.compareProductAssocType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
