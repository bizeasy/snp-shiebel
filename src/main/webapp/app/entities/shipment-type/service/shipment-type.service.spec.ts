import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShipmentType } from '../shipment-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shipment-type.test-samples';

import { ShipmentTypeService } from './shipment-type.service';

const requireRestSample: IShipmentType = {
  ...sampleWithRequiredData,
};

describe('ShipmentType Service', () => {
  let service: ShipmentTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IShipmentType | IShipmentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentTypeService);
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

    it('should create a ShipmentType', () => {
      const shipmentType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shipmentType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShipmentType', () => {
      const shipmentType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shipmentType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShipmentType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShipmentType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShipmentType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShipmentTypeToCollectionIfMissing', () => {
      it('should add a ShipmentType to an empty array', () => {
        const shipmentType: IShipmentType = sampleWithRequiredData;
        expectedResult = service.addShipmentTypeToCollectionIfMissing([], shipmentType);
        expect(expectedResult).toEqual([shipmentType]);
      });

      it('should not add a ShipmentType to an array that contains it', () => {
        const shipmentType: IShipmentType = sampleWithRequiredData;
        const shipmentTypeCollection: IShipmentType[] = [
          {
            ...shipmentType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, shipmentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShipmentType to an array that doesn't contain it", () => {
        const shipmentType: IShipmentType = sampleWithRequiredData;
        const shipmentTypeCollection: IShipmentType[] = [sampleWithPartialData];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, shipmentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentType);
      });

      it('should add only unique ShipmentType to an array', () => {
        const shipmentTypeArray: IShipmentType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipmentTypeCollection: IShipmentType[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, ...shipmentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipmentType: IShipmentType = sampleWithRequiredData;
        const shipmentType2: IShipmentType = sampleWithPartialData;
        expectedResult = service.addShipmentTypeToCollectionIfMissing([], shipmentType, shipmentType2);
        expect(expectedResult).toEqual([shipmentType, shipmentType2]);
      });

      it('should accept null and undefined values', () => {
        const shipmentType: IShipmentType = sampleWithRequiredData;
        expectedResult = service.addShipmentTypeToCollectionIfMissing([], null, shipmentType, undefined);
        expect(expectedResult).toEqual([shipmentType]);
      });

      it('should return initial array if no ShipmentType is added', () => {
        const shipmentTypeCollection: IShipmentType[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentTypeToCollectionIfMissing(shipmentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentTypeCollection);
      });
    });

    describe('compareShipmentType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShipmentType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16845 };
        const entity2 = null;

        const compareResult1 = service.compareShipmentType(entity1, entity2);
        const compareResult2 = service.compareShipmentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16845 };
        const entity2 = { id: 32676 };

        const compareResult1 = service.compareShipmentType(entity1, entity2);
        const compareResult2 = service.compareShipmentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 16845 };
        const entity2 = { id: 16845 };

        const compareResult1 = service.compareShipmentType(entity1, entity2);
        const compareResult2 = service.compareShipmentType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
