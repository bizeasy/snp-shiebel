import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShipmentReceipt } from '../shipment-receipt.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shipment-receipt.test-samples';

import { RestShipmentReceipt, ShipmentReceiptService } from './shipment-receipt.service';

const requireRestSample: RestShipmentReceipt = {
  ...sampleWithRequiredData,
  receivedDate: sampleWithRequiredData.receivedDate?.toJSON(),
};

describe('ShipmentReceipt Service', () => {
  let service: ShipmentReceiptService;
  let httpMock: HttpTestingController;
  let expectedResult: IShipmentReceipt | IShipmentReceipt[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentReceiptService);
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

    it('should create a ShipmentReceipt', () => {
      const shipmentReceipt = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shipmentReceipt).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShipmentReceipt', () => {
      const shipmentReceipt = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shipmentReceipt).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShipmentReceipt', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShipmentReceipt', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShipmentReceipt', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShipmentReceiptToCollectionIfMissing', () => {
      it('should add a ShipmentReceipt to an empty array', () => {
        const shipmentReceipt: IShipmentReceipt = sampleWithRequiredData;
        expectedResult = service.addShipmentReceiptToCollectionIfMissing([], shipmentReceipt);
        expect(expectedResult).toEqual([shipmentReceipt]);
      });

      it('should not add a ShipmentReceipt to an array that contains it', () => {
        const shipmentReceipt: IShipmentReceipt = sampleWithRequiredData;
        const shipmentReceiptCollection: IShipmentReceipt[] = [
          {
            ...shipmentReceipt,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipmentReceiptToCollectionIfMissing(shipmentReceiptCollection, shipmentReceipt);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShipmentReceipt to an array that doesn't contain it", () => {
        const shipmentReceipt: IShipmentReceipt = sampleWithRequiredData;
        const shipmentReceiptCollection: IShipmentReceipt[] = [sampleWithPartialData];
        expectedResult = service.addShipmentReceiptToCollectionIfMissing(shipmentReceiptCollection, shipmentReceipt);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentReceipt);
      });

      it('should add only unique ShipmentReceipt to an array', () => {
        const shipmentReceiptArray: IShipmentReceipt[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipmentReceiptCollection: IShipmentReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentReceiptToCollectionIfMissing(shipmentReceiptCollection, ...shipmentReceiptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipmentReceipt: IShipmentReceipt = sampleWithRequiredData;
        const shipmentReceipt2: IShipmentReceipt = sampleWithPartialData;
        expectedResult = service.addShipmentReceiptToCollectionIfMissing([], shipmentReceipt, shipmentReceipt2);
        expect(expectedResult).toEqual([shipmentReceipt, shipmentReceipt2]);
      });

      it('should accept null and undefined values', () => {
        const shipmentReceipt: IShipmentReceipt = sampleWithRequiredData;
        expectedResult = service.addShipmentReceiptToCollectionIfMissing([], null, shipmentReceipt, undefined);
        expect(expectedResult).toEqual([shipmentReceipt]);
      });

      it('should return initial array if no ShipmentReceipt is added', () => {
        const shipmentReceiptCollection: IShipmentReceipt[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentReceiptToCollectionIfMissing(shipmentReceiptCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentReceiptCollection);
      });
    });

    describe('compareShipmentReceipt', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShipmentReceipt(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7355 };
        const entity2 = null;

        const compareResult1 = service.compareShipmentReceipt(entity1, entity2);
        const compareResult2 = service.compareShipmentReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7355 };
        const entity2 = { id: 18394 };

        const compareResult1 = service.compareShipmentReceipt(entity1, entity2);
        const compareResult2 = service.compareShipmentReceipt(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7355 };
        const entity2 = { id: 7355 };

        const compareResult1 = service.compareShipmentReceipt(entity1, entity2);
        const compareResult2 = service.compareShipmentReceipt(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
