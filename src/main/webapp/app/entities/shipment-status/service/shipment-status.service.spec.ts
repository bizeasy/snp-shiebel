import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShipmentStatus } from '../shipment-status.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shipment-status.test-samples';

import { RestShipmentStatus, ShipmentStatusService } from './shipment-status.service';

const requireRestSample: RestShipmentStatus = {
  ...sampleWithRequiredData,
  statusDateTime: sampleWithRequiredData.statusDateTime?.toJSON(),
};

describe('ShipmentStatus Service', () => {
  let service: ShipmentStatusService;
  let httpMock: HttpTestingController;
  let expectedResult: IShipmentStatus | IShipmentStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentStatusService);
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

    it('should create a ShipmentStatus', () => {
      const shipmentStatus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shipmentStatus).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShipmentStatus', () => {
      const shipmentStatus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shipmentStatus).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShipmentStatus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShipmentStatus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShipmentStatus', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShipmentStatusToCollectionIfMissing', () => {
      it('should add a ShipmentStatus to an empty array', () => {
        const shipmentStatus: IShipmentStatus = sampleWithRequiredData;
        expectedResult = service.addShipmentStatusToCollectionIfMissing([], shipmentStatus);
        expect(expectedResult).toEqual([shipmentStatus]);
      });

      it('should not add a ShipmentStatus to an array that contains it', () => {
        const shipmentStatus: IShipmentStatus = sampleWithRequiredData;
        const shipmentStatusCollection: IShipmentStatus[] = [
          {
            ...shipmentStatus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipmentStatusToCollectionIfMissing(shipmentStatusCollection, shipmentStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShipmentStatus to an array that doesn't contain it", () => {
        const shipmentStatus: IShipmentStatus = sampleWithRequiredData;
        const shipmentStatusCollection: IShipmentStatus[] = [sampleWithPartialData];
        expectedResult = service.addShipmentStatusToCollectionIfMissing(shipmentStatusCollection, shipmentStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentStatus);
      });

      it('should add only unique ShipmentStatus to an array', () => {
        const shipmentStatusArray: IShipmentStatus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipmentStatusCollection: IShipmentStatus[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentStatusToCollectionIfMissing(shipmentStatusCollection, ...shipmentStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipmentStatus: IShipmentStatus = sampleWithRequiredData;
        const shipmentStatus2: IShipmentStatus = sampleWithPartialData;
        expectedResult = service.addShipmentStatusToCollectionIfMissing([], shipmentStatus, shipmentStatus2);
        expect(expectedResult).toEqual([shipmentStatus, shipmentStatus2]);
      });

      it('should accept null and undefined values', () => {
        const shipmentStatus: IShipmentStatus = sampleWithRequiredData;
        expectedResult = service.addShipmentStatusToCollectionIfMissing([], null, shipmentStatus, undefined);
        expect(expectedResult).toEqual([shipmentStatus]);
      });

      it('should return initial array if no ShipmentStatus is added', () => {
        const shipmentStatusCollection: IShipmentStatus[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentStatusToCollectionIfMissing(shipmentStatusCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentStatusCollection);
      });
    });

    describe('compareShipmentStatus', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShipmentStatus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26844 };
        const entity2 = null;

        const compareResult1 = service.compareShipmentStatus(entity1, entity2);
        const compareResult2 = service.compareShipmentStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26844 };
        const entity2 = { id: 18749 };

        const compareResult1 = service.compareShipmentStatus(entity1, entity2);
        const compareResult2 = service.compareShipmentStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 26844 };
        const entity2 = { id: 26844 };

        const compareResult1 = service.compareShipmentStatus(entity1, entity2);
        const compareResult2 = service.compareShipmentStatus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
