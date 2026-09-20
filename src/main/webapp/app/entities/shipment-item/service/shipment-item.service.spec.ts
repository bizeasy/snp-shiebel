import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShipmentItem } from '../shipment-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shipment-item.test-samples';

import { ShipmentItemService } from './shipment-item.service';

const requireRestSample: IShipmentItem = {
  ...sampleWithRequiredData,
};

describe('ShipmentItem Service', () => {
  let service: ShipmentItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IShipmentItem | IShipmentItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentItemService);
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

    it('should create a ShipmentItem', () => {
      const shipmentItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shipmentItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShipmentItem', () => {
      const shipmentItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shipmentItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShipmentItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShipmentItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShipmentItem', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShipmentItemToCollectionIfMissing', () => {
      it('should add a ShipmentItem to an empty array', () => {
        const shipmentItem: IShipmentItem = sampleWithRequiredData;
        expectedResult = service.addShipmentItemToCollectionIfMissing([], shipmentItem);
        expect(expectedResult).toEqual([shipmentItem]);
      });

      it('should not add a ShipmentItem to an array that contains it', () => {
        const shipmentItem: IShipmentItem = sampleWithRequiredData;
        const shipmentItemCollection: IShipmentItem[] = [
          {
            ...shipmentItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipmentItemToCollectionIfMissing(shipmentItemCollection, shipmentItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShipmentItem to an array that doesn't contain it", () => {
        const shipmentItem: IShipmentItem = sampleWithRequiredData;
        const shipmentItemCollection: IShipmentItem[] = [sampleWithPartialData];
        expectedResult = service.addShipmentItemToCollectionIfMissing(shipmentItemCollection, shipmentItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentItem);
      });

      it('should add only unique ShipmentItem to an array', () => {
        const shipmentItemArray: IShipmentItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipmentItemCollection: IShipmentItem[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentItemToCollectionIfMissing(shipmentItemCollection, ...shipmentItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipmentItem: IShipmentItem = sampleWithRequiredData;
        const shipmentItem2: IShipmentItem = sampleWithPartialData;
        expectedResult = service.addShipmentItemToCollectionIfMissing([], shipmentItem, shipmentItem2);
        expect(expectedResult).toEqual([shipmentItem, shipmentItem2]);
      });

      it('should accept null and undefined values', () => {
        const shipmentItem: IShipmentItem = sampleWithRequiredData;
        expectedResult = service.addShipmentItemToCollectionIfMissing([], null, shipmentItem, undefined);
        expect(expectedResult).toEqual([shipmentItem]);
      });

      it('should return initial array if no ShipmentItem is added', () => {
        const shipmentItemCollection: IShipmentItem[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentItemToCollectionIfMissing(shipmentItemCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentItemCollection);
      });
    });

    describe('compareShipmentItem', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShipmentItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14901 };
        const entity2 = null;

        const compareResult1 = service.compareShipmentItem(entity1, entity2);
        const compareResult2 = service.compareShipmentItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14901 };
        const entity2 = { id: 27511 };

        const compareResult1 = service.compareShipmentItem(entity1, entity2);
        const compareResult2 = service.compareShipmentItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 14901 };
        const entity2 = { id: 14901 };

        const compareResult1 = service.compareShipmentItem(entity1, entity2);
        const compareResult2 = service.compareShipmentItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
