import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInventoryItemType } from '../inventory-item-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../inventory-item-type.test-samples';

import { InventoryItemTypeService } from './inventory-item-type.service';

const requireRestSample: IInventoryItemType = {
  ...sampleWithRequiredData,
};

describe('InventoryItemType Service', () => {
  let service: InventoryItemTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IInventoryItemType | IInventoryItemType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InventoryItemTypeService);
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

    it('should create a InventoryItemType', () => {
      const inventoryItemType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inventoryItemType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InventoryItemType', () => {
      const inventoryItemType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inventoryItemType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InventoryItemType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InventoryItemType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InventoryItemType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInventoryItemTypeToCollectionIfMissing', () => {
      it('should add a InventoryItemType to an empty array', () => {
        const inventoryItemType: IInventoryItemType = sampleWithRequiredData;
        expectedResult = service.addInventoryItemTypeToCollectionIfMissing([], inventoryItemType);
        expect(expectedResult).toEqual([inventoryItemType]);
      });

      it('should not add a InventoryItemType to an array that contains it', () => {
        const inventoryItemType: IInventoryItemType = sampleWithRequiredData;
        const inventoryItemTypeCollection: IInventoryItemType[] = [
          {
            ...inventoryItemType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInventoryItemTypeToCollectionIfMissing(inventoryItemTypeCollection, inventoryItemType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InventoryItemType to an array that doesn't contain it", () => {
        const inventoryItemType: IInventoryItemType = sampleWithRequiredData;
        const inventoryItemTypeCollection: IInventoryItemType[] = [sampleWithPartialData];
        expectedResult = service.addInventoryItemTypeToCollectionIfMissing(inventoryItemTypeCollection, inventoryItemType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryItemType);
      });

      it('should add only unique InventoryItemType to an array', () => {
        const inventoryItemTypeArray: IInventoryItemType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inventoryItemTypeCollection: IInventoryItemType[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemTypeToCollectionIfMissing(inventoryItemTypeCollection, ...inventoryItemTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventoryItemType: IInventoryItemType = sampleWithRequiredData;
        const inventoryItemType2: IInventoryItemType = sampleWithPartialData;
        expectedResult = service.addInventoryItemTypeToCollectionIfMissing([], inventoryItemType, inventoryItemType2);
        expect(expectedResult).toEqual([inventoryItemType, inventoryItemType2]);
      });

      it('should accept null and undefined values', () => {
        const inventoryItemType: IInventoryItemType = sampleWithRequiredData;
        expectedResult = service.addInventoryItemTypeToCollectionIfMissing([], null, inventoryItemType, undefined);
        expect(expectedResult).toEqual([inventoryItemType]);
      });

      it('should return initial array if no InventoryItemType is added', () => {
        const inventoryItemTypeCollection: IInventoryItemType[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemTypeToCollectionIfMissing(inventoryItemTypeCollection, undefined, null);
        expect(expectedResult).toEqual(inventoryItemTypeCollection);
      });
    });

    describe('compareInventoryItemType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInventoryItemType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9002 };
        const entity2 = null;

        const compareResult1 = service.compareInventoryItemType(entity1, entity2);
        const compareResult2 = service.compareInventoryItemType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9002 };
        const entity2 = { id: 3434 };

        const compareResult1 = service.compareInventoryItemType(entity1, entity2);
        const compareResult2 = service.compareInventoryItemType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 9002 };
        const entity2 = { id: 9002 };

        const compareResult1 = service.compareInventoryItemType(entity1, entity2);
        const compareResult2 = service.compareInventoryItemType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
