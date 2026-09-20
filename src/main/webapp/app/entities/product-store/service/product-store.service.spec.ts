import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductStore } from '../product-store.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-store.test-samples';

import { ProductStoreService } from './product-store.service';

const requireRestSample: IProductStore = {
  ...sampleWithRequiredData,
};

describe('ProductStore Service', () => {
  let service: ProductStoreService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductStore | IProductStore[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductStoreService);
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

    it('should create a ProductStore', () => {
      const productStore = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productStore).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductStore', () => {
      const productStore = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productStore).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductStore', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductStore', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductStore', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductStoreToCollectionIfMissing', () => {
      it('should add a ProductStore to an empty array', () => {
        const productStore: IProductStore = sampleWithRequiredData;
        expectedResult = service.addProductStoreToCollectionIfMissing([], productStore);
        expect(expectedResult).toEqual([productStore]);
      });

      it('should not add a ProductStore to an array that contains it', () => {
        const productStore: IProductStore = sampleWithRequiredData;
        const productStoreCollection: IProductStore[] = [
          {
            ...productStore,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductStoreToCollectionIfMissing(productStoreCollection, productStore);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductStore to an array that doesn't contain it", () => {
        const productStore: IProductStore = sampleWithRequiredData;
        const productStoreCollection: IProductStore[] = [sampleWithPartialData];
        expectedResult = service.addProductStoreToCollectionIfMissing(productStoreCollection, productStore);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productStore);
      });

      it('should add only unique ProductStore to an array', () => {
        const productStoreArray: IProductStore[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productStoreCollection: IProductStore[] = [sampleWithRequiredData];
        expectedResult = service.addProductStoreToCollectionIfMissing(productStoreCollection, ...productStoreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productStore: IProductStore = sampleWithRequiredData;
        const productStore2: IProductStore = sampleWithPartialData;
        expectedResult = service.addProductStoreToCollectionIfMissing([], productStore, productStore2);
        expect(expectedResult).toEqual([productStore, productStore2]);
      });

      it('should accept null and undefined values', () => {
        const productStore: IProductStore = sampleWithRequiredData;
        expectedResult = service.addProductStoreToCollectionIfMissing([], null, productStore, undefined);
        expect(expectedResult).toEqual([productStore]);
      });

      it('should return initial array if no ProductStore is added', () => {
        const productStoreCollection: IProductStore[] = [sampleWithRequiredData];
        expectedResult = service.addProductStoreToCollectionIfMissing(productStoreCollection, undefined, null);
        expect(expectedResult).toEqual(productStoreCollection);
      });
    });

    describe('compareProductStore', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductStore(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30593 };
        const entity2 = null;

        const compareResult1 = service.compareProductStore(entity1, entity2);
        const compareResult2 = service.compareProductStore(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30593 };
        const entity2 = { id: 24662 };

        const compareResult1 = service.compareProductStore(entity1, entity2);
        const compareResult2 = service.compareProductStore(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 30593 };
        const entity2 = { id: 30593 };

        const compareResult1 = service.compareProductStore(entity1, entity2);
        const compareResult2 = service.compareProductStore(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
