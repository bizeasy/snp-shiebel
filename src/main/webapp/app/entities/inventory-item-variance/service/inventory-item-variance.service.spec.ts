import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInventoryItemVariance } from '../inventory-item-variance.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../inventory-item-variance.test-samples';

import { InventoryItemVarianceService } from './inventory-item-variance.service';

const requireRestSample: IInventoryItemVariance = {
  ...sampleWithRequiredData,
};

describe('InventoryItemVariance Service', () => {
  let service: InventoryItemVarianceService;
  let httpMock: HttpTestingController;
  let expectedResult: IInventoryItemVariance | IInventoryItemVariance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InventoryItemVarianceService);
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

    it('should create a InventoryItemVariance', () => {
      const inventoryItemVariance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inventoryItemVariance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InventoryItemVariance', () => {
      const inventoryItemVariance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inventoryItemVariance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InventoryItemVariance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InventoryItemVariance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InventoryItemVariance', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInventoryItemVarianceToCollectionIfMissing', () => {
      it('should add a InventoryItemVariance to an empty array', () => {
        const inventoryItemVariance: IInventoryItemVariance = sampleWithRequiredData;
        expectedResult = service.addInventoryItemVarianceToCollectionIfMissing([], inventoryItemVariance);
        expect(expectedResult).toEqual([inventoryItemVariance]);
      });

      it('should not add a InventoryItemVariance to an array that contains it', () => {
        const inventoryItemVariance: IInventoryItemVariance = sampleWithRequiredData;
        const inventoryItemVarianceCollection: IInventoryItemVariance[] = [
          {
            ...inventoryItemVariance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInventoryItemVarianceToCollectionIfMissing(inventoryItemVarianceCollection, inventoryItemVariance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InventoryItemVariance to an array that doesn't contain it", () => {
        const inventoryItemVariance: IInventoryItemVariance = sampleWithRequiredData;
        const inventoryItemVarianceCollection: IInventoryItemVariance[] = [sampleWithPartialData];
        expectedResult = service.addInventoryItemVarianceToCollectionIfMissing(inventoryItemVarianceCollection, inventoryItemVariance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryItemVariance);
      });

      it('should add only unique InventoryItemVariance to an array', () => {
        const inventoryItemVarianceArray: IInventoryItemVariance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inventoryItemVarianceCollection: IInventoryItemVariance[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemVarianceToCollectionIfMissing(
          inventoryItemVarianceCollection,
          ...inventoryItemVarianceArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventoryItemVariance: IInventoryItemVariance = sampleWithRequiredData;
        const inventoryItemVariance2: IInventoryItemVariance = sampleWithPartialData;
        expectedResult = service.addInventoryItemVarianceToCollectionIfMissing([], inventoryItemVariance, inventoryItemVariance2);
        expect(expectedResult).toEqual([inventoryItemVariance, inventoryItemVariance2]);
      });

      it('should accept null and undefined values', () => {
        const inventoryItemVariance: IInventoryItemVariance = sampleWithRequiredData;
        expectedResult = service.addInventoryItemVarianceToCollectionIfMissing([], null, inventoryItemVariance, undefined);
        expect(expectedResult).toEqual([inventoryItemVariance]);
      });

      it('should return initial array if no InventoryItemVariance is added', () => {
        const inventoryItemVarianceCollection: IInventoryItemVariance[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemVarianceToCollectionIfMissing(inventoryItemVarianceCollection, undefined, null);
        expect(expectedResult).toEqual(inventoryItemVarianceCollection);
      });
    });

    describe('compareInventoryItemVariance', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInventoryItemVariance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7138 };
        const entity2 = null;

        const compareResult1 = service.compareInventoryItemVariance(entity1, entity2);
        const compareResult2 = service.compareInventoryItemVariance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7138 };
        const entity2 = { id: 6641 };

        const compareResult1 = service.compareInventoryItemVariance(entity1, entity2);
        const compareResult2 = service.compareInventoryItemVariance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7138 };
        const entity2 = { id: 7138 };

        const compareResult1 = service.compareInventoryItemVariance(entity1, entity2);
        const compareResult2 = service.compareInventoryItemVariance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
