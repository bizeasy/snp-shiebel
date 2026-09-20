import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShipment } from '../shipment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shipment.test-samples';

import { RestShipment, ShipmentService } from './shipment.service';

const requireRestSample: RestShipment = {
  ...sampleWithRequiredData,
  estimatedShipDate: sampleWithRequiredData.estimatedShipDate?.toJSON(),
  estimatedReadyDate: sampleWithRequiredData.estimatedReadyDate?.toJSON(),
  estimatedArrivalDate: sampleWithRequiredData.estimatedArrivalDate?.toJSON(),
};

describe('Shipment Service', () => {
  let service: ShipmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IShipment | IShipment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentService);
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

    it('should create a Shipment', () => {
      const shipment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shipment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Shipment', () => {
      const shipment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shipment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Shipment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Shipment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Shipment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShipmentToCollectionIfMissing', () => {
      it('should add a Shipment to an empty array', () => {
        const shipment: IShipment = sampleWithRequiredData;
        expectedResult = service.addShipmentToCollectionIfMissing([], shipment);
        expect(expectedResult).toEqual([shipment]);
      });

      it('should not add a Shipment to an array that contains it', () => {
        const shipment: IShipment = sampleWithRequiredData;
        const shipmentCollection: IShipment[] = [
          {
            ...shipment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipmentToCollectionIfMissing(shipmentCollection, shipment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Shipment to an array that doesn't contain it", () => {
        const shipment: IShipment = sampleWithRequiredData;
        const shipmentCollection: IShipment[] = [sampleWithPartialData];
        expectedResult = service.addShipmentToCollectionIfMissing(shipmentCollection, shipment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipment);
      });

      it('should add only unique Shipment to an array', () => {
        const shipmentArray: IShipment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipmentCollection: IShipment[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentToCollectionIfMissing(shipmentCollection, ...shipmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipment: IShipment = sampleWithRequiredData;
        const shipment2: IShipment = sampleWithPartialData;
        expectedResult = service.addShipmentToCollectionIfMissing([], shipment, shipment2);
        expect(expectedResult).toEqual([shipment, shipment2]);
      });

      it('should accept null and undefined values', () => {
        const shipment: IShipment = sampleWithRequiredData;
        expectedResult = service.addShipmentToCollectionIfMissing([], null, shipment, undefined);
        expect(expectedResult).toEqual([shipment]);
      });

      it('should return initial array if no Shipment is added', () => {
        const shipmentCollection: IShipment[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentToCollectionIfMissing(shipmentCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentCollection);
      });
    });

    describe('compareShipment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShipment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9594 };
        const entity2 = null;

        const compareResult1 = service.compareShipment(entity1, entity2);
        const compareResult2 = service.compareShipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9594 };
        const entity2 = { id: 9190 };

        const compareResult1 = service.compareShipment(entity1, entity2);
        const compareResult2 = service.compareShipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 9594 };
        const entity2 = { id: 9594 };

        const compareResult1 = service.compareShipment(entity1, entity2);
        const compareResult2 = service.compareShipment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
