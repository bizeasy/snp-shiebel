import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInventoryTransfer } from '../inventory-transfer.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../inventory-transfer.test-samples';

import { InventoryTransferService, RestInventoryTransfer } from './inventory-transfer.service';

const requireRestSample: RestInventoryTransfer = {
  ...sampleWithRequiredData,
  sentDate: sampleWithRequiredData.sentDate?.toJSON(),
  receivedDate: sampleWithRequiredData.receivedDate?.toJSON(),
};

describe('InventoryTransfer Service', () => {
  let service: InventoryTransferService;
  let httpMock: HttpTestingController;
  let expectedResult: IInventoryTransfer | IInventoryTransfer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InventoryTransferService);
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

    it('should create a InventoryTransfer', () => {
      const inventoryTransfer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inventoryTransfer).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InventoryTransfer', () => {
      const inventoryTransfer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inventoryTransfer).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InventoryTransfer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InventoryTransfer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InventoryTransfer', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInventoryTransferToCollectionIfMissing', () => {
      it('should add a InventoryTransfer to an empty array', () => {
        const inventoryTransfer: IInventoryTransfer = sampleWithRequiredData;
        expectedResult = service.addInventoryTransferToCollectionIfMissing([], inventoryTransfer);
        expect(expectedResult).toEqual([inventoryTransfer]);
      });

      it('should not add a InventoryTransfer to an array that contains it', () => {
        const inventoryTransfer: IInventoryTransfer = sampleWithRequiredData;
        const inventoryTransferCollection: IInventoryTransfer[] = [
          {
            ...inventoryTransfer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInventoryTransferToCollectionIfMissing(inventoryTransferCollection, inventoryTransfer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InventoryTransfer to an array that doesn't contain it", () => {
        const inventoryTransfer: IInventoryTransfer = sampleWithRequiredData;
        const inventoryTransferCollection: IInventoryTransfer[] = [sampleWithPartialData];
        expectedResult = service.addInventoryTransferToCollectionIfMissing(inventoryTransferCollection, inventoryTransfer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryTransfer);
      });

      it('should add only unique InventoryTransfer to an array', () => {
        const inventoryTransferArray: IInventoryTransfer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inventoryTransferCollection: IInventoryTransfer[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryTransferToCollectionIfMissing(inventoryTransferCollection, ...inventoryTransferArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventoryTransfer: IInventoryTransfer = sampleWithRequiredData;
        const inventoryTransfer2: IInventoryTransfer = sampleWithPartialData;
        expectedResult = service.addInventoryTransferToCollectionIfMissing([], inventoryTransfer, inventoryTransfer2);
        expect(expectedResult).toEqual([inventoryTransfer, inventoryTransfer2]);
      });

      it('should accept null and undefined values', () => {
        const inventoryTransfer: IInventoryTransfer = sampleWithRequiredData;
        expectedResult = service.addInventoryTransferToCollectionIfMissing([], null, inventoryTransfer, undefined);
        expect(expectedResult).toEqual([inventoryTransfer]);
      });

      it('should return initial array if no InventoryTransfer is added', () => {
        const inventoryTransferCollection: IInventoryTransfer[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryTransferToCollectionIfMissing(inventoryTransferCollection, undefined, null);
        expect(expectedResult).toEqual(inventoryTransferCollection);
      });
    });

    describe('compareInventoryTransfer', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInventoryTransfer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25009 };
        const entity2 = null;

        const compareResult1 = service.compareInventoryTransfer(entity1, entity2);
        const compareResult2 = service.compareInventoryTransfer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25009 };
        const entity2 = { id: 27132 };

        const compareResult1 = service.compareInventoryTransfer(entity1, entity2);
        const compareResult2 = service.compareInventoryTransfer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25009 };
        const entity2 = { id: 25009 };

        const compareResult1 = service.compareInventoryTransfer(entity1, entity2);
        const compareResult2 = service.compareInventoryTransfer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
