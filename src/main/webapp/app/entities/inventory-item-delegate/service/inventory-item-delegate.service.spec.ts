import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInventoryItemDelegate } from '../inventory-item-delegate.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../inventory-item-delegate.test-samples';

import { InventoryItemDelegateService, RestInventoryItemDelegate } from './inventory-item-delegate.service';

const requireRestSample: RestInventoryItemDelegate = {
  ...sampleWithRequiredData,
  effectiveDate: sampleWithRequiredData.effectiveDate?.toJSON(),
};

describe('InventoryItemDelegate Service', () => {
  let service: InventoryItemDelegateService;
  let httpMock: HttpTestingController;
  let expectedResult: IInventoryItemDelegate | IInventoryItemDelegate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InventoryItemDelegateService);
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

    it('should create a InventoryItemDelegate', () => {
      const inventoryItemDelegate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inventoryItemDelegate).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InventoryItemDelegate', () => {
      const inventoryItemDelegate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inventoryItemDelegate).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InventoryItemDelegate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InventoryItemDelegate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InventoryItemDelegate', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInventoryItemDelegateToCollectionIfMissing', () => {
      it('should add a InventoryItemDelegate to an empty array', () => {
        const inventoryItemDelegate: IInventoryItemDelegate = sampleWithRequiredData;
        expectedResult = service.addInventoryItemDelegateToCollectionIfMissing([], inventoryItemDelegate);
        expect(expectedResult).toEqual([inventoryItemDelegate]);
      });

      it('should not add a InventoryItemDelegate to an array that contains it', () => {
        const inventoryItemDelegate: IInventoryItemDelegate = sampleWithRequiredData;
        const inventoryItemDelegateCollection: IInventoryItemDelegate[] = [
          {
            ...inventoryItemDelegate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInventoryItemDelegateToCollectionIfMissing(inventoryItemDelegateCollection, inventoryItemDelegate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InventoryItemDelegate to an array that doesn't contain it", () => {
        const inventoryItemDelegate: IInventoryItemDelegate = sampleWithRequiredData;
        const inventoryItemDelegateCollection: IInventoryItemDelegate[] = [sampleWithPartialData];
        expectedResult = service.addInventoryItemDelegateToCollectionIfMissing(inventoryItemDelegateCollection, inventoryItemDelegate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryItemDelegate);
      });

      it('should add only unique InventoryItemDelegate to an array', () => {
        const inventoryItemDelegateArray: IInventoryItemDelegate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inventoryItemDelegateCollection: IInventoryItemDelegate[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemDelegateToCollectionIfMissing(
          inventoryItemDelegateCollection,
          ...inventoryItemDelegateArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventoryItemDelegate: IInventoryItemDelegate = sampleWithRequiredData;
        const inventoryItemDelegate2: IInventoryItemDelegate = sampleWithPartialData;
        expectedResult = service.addInventoryItemDelegateToCollectionIfMissing([], inventoryItemDelegate, inventoryItemDelegate2);
        expect(expectedResult).toEqual([inventoryItemDelegate, inventoryItemDelegate2]);
      });

      it('should accept null and undefined values', () => {
        const inventoryItemDelegate: IInventoryItemDelegate = sampleWithRequiredData;
        expectedResult = service.addInventoryItemDelegateToCollectionIfMissing([], null, inventoryItemDelegate, undefined);
        expect(expectedResult).toEqual([inventoryItemDelegate]);
      });

      it('should return initial array if no InventoryItemDelegate is added', () => {
        const inventoryItemDelegateCollection: IInventoryItemDelegate[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemDelegateToCollectionIfMissing(inventoryItemDelegateCollection, undefined, null);
        expect(expectedResult).toEqual(inventoryItemDelegateCollection);
      });
    });

    describe('compareInventoryItemDelegate', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInventoryItemDelegate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14485 };
        const entity2 = null;

        const compareResult1 = service.compareInventoryItemDelegate(entity1, entity2);
        const compareResult2 = service.compareInventoryItemDelegate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14485 };
        const entity2 = { id: 1018 };

        const compareResult1 = service.compareInventoryItemDelegate(entity1, entity2);
        const compareResult2 = service.compareInventoryItemDelegate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 14485 };
        const entity2 = { id: 14485 };

        const compareResult1 = service.compareInventoryItemDelegate(entity1, entity2);
        const compareResult2 = service.compareInventoryItemDelegate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
