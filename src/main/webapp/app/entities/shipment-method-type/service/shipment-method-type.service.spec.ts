import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShipmentMethodType } from '../shipment-method-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shipment-method-type.test-samples';

import { ShipmentMethodTypeService } from './shipment-method-type.service';

const requireRestSample: IShipmentMethodType = {
  ...sampleWithRequiredData,
};

describe('ShipmentMethodType Service', () => {
  let service: ShipmentMethodTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IShipmentMethodType | IShipmentMethodType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentMethodTypeService);
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

    it('should create a ShipmentMethodType', () => {
      const shipmentMethodType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shipmentMethodType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShipmentMethodType', () => {
      const shipmentMethodType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shipmentMethodType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShipmentMethodType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShipmentMethodType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShipmentMethodType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShipmentMethodTypeToCollectionIfMissing', () => {
      it('should add a ShipmentMethodType to an empty array', () => {
        const shipmentMethodType: IShipmentMethodType = sampleWithRequiredData;
        expectedResult = service.addShipmentMethodTypeToCollectionIfMissing([], shipmentMethodType);
        expect(expectedResult).toEqual([shipmentMethodType]);
      });

      it('should not add a ShipmentMethodType to an array that contains it', () => {
        const shipmentMethodType: IShipmentMethodType = sampleWithRequiredData;
        const shipmentMethodTypeCollection: IShipmentMethodType[] = [
          {
            ...shipmentMethodType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipmentMethodTypeToCollectionIfMissing(shipmentMethodTypeCollection, shipmentMethodType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShipmentMethodType to an array that doesn't contain it", () => {
        const shipmentMethodType: IShipmentMethodType = sampleWithRequiredData;
        const shipmentMethodTypeCollection: IShipmentMethodType[] = [sampleWithPartialData];
        expectedResult = service.addShipmentMethodTypeToCollectionIfMissing(shipmentMethodTypeCollection, shipmentMethodType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentMethodType);
      });

      it('should add only unique ShipmentMethodType to an array', () => {
        const shipmentMethodTypeArray: IShipmentMethodType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipmentMethodTypeCollection: IShipmentMethodType[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentMethodTypeToCollectionIfMissing(shipmentMethodTypeCollection, ...shipmentMethodTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipmentMethodType: IShipmentMethodType = sampleWithRequiredData;
        const shipmentMethodType2: IShipmentMethodType = sampleWithPartialData;
        expectedResult = service.addShipmentMethodTypeToCollectionIfMissing([], shipmentMethodType, shipmentMethodType2);
        expect(expectedResult).toEqual([shipmentMethodType, shipmentMethodType2]);
      });

      it('should accept null and undefined values', () => {
        const shipmentMethodType: IShipmentMethodType = sampleWithRequiredData;
        expectedResult = service.addShipmentMethodTypeToCollectionIfMissing([], null, shipmentMethodType, undefined);
        expect(expectedResult).toEqual([shipmentMethodType]);
      });

      it('should return initial array if no ShipmentMethodType is added', () => {
        const shipmentMethodTypeCollection: IShipmentMethodType[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentMethodTypeToCollectionIfMissing(shipmentMethodTypeCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentMethodTypeCollection);
      });
    });

    describe('compareShipmentMethodType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShipmentMethodType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6802 };
        const entity2 = null;

        const compareResult1 = service.compareShipmentMethodType(entity1, entity2);
        const compareResult2 = service.compareShipmentMethodType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6802 };
        const entity2 = { id: 25543 };

        const compareResult1 = service.compareShipmentMethodType(entity1, entity2);
        const compareResult2 = service.compareShipmentMethodType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6802 };
        const entity2 = { id: 6802 };

        const compareResult1 = service.compareShipmentMethodType(entity1, entity2);
        const compareResult2 = service.compareShipmentMethodType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
