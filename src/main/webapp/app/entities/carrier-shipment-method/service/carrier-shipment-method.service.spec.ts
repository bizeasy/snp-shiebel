import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICarrierShipmentMethod } from '../carrier-shipment-method.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../carrier-shipment-method.test-samples';

import { CarrierShipmentMethodService } from './carrier-shipment-method.service';

const requireRestSample: ICarrierShipmentMethod = {
  ...sampleWithRequiredData,
};

describe('CarrierShipmentMethod Service', () => {
  let service: CarrierShipmentMethodService;
  let httpMock: HttpTestingController;
  let expectedResult: ICarrierShipmentMethod | ICarrierShipmentMethod[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CarrierShipmentMethodService);
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

    it('should create a CarrierShipmentMethod', () => {
      const carrierShipmentMethod = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(carrierShipmentMethod).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CarrierShipmentMethod', () => {
      const carrierShipmentMethod = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(carrierShipmentMethod).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CarrierShipmentMethod', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CarrierShipmentMethod', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CarrierShipmentMethod', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCarrierShipmentMethodToCollectionIfMissing', () => {
      it('should add a CarrierShipmentMethod to an empty array', () => {
        const carrierShipmentMethod: ICarrierShipmentMethod = sampleWithRequiredData;
        expectedResult = service.addCarrierShipmentMethodToCollectionIfMissing([], carrierShipmentMethod);
        expect(expectedResult).toEqual([carrierShipmentMethod]);
      });

      it('should not add a CarrierShipmentMethod to an array that contains it', () => {
        const carrierShipmentMethod: ICarrierShipmentMethod = sampleWithRequiredData;
        const carrierShipmentMethodCollection: ICarrierShipmentMethod[] = [
          {
            ...carrierShipmentMethod,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCarrierShipmentMethodToCollectionIfMissing(carrierShipmentMethodCollection, carrierShipmentMethod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CarrierShipmentMethod to an array that doesn't contain it", () => {
        const carrierShipmentMethod: ICarrierShipmentMethod = sampleWithRequiredData;
        const carrierShipmentMethodCollection: ICarrierShipmentMethod[] = [sampleWithPartialData];
        expectedResult = service.addCarrierShipmentMethodToCollectionIfMissing(carrierShipmentMethodCollection, carrierShipmentMethod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carrierShipmentMethod);
      });

      it('should add only unique CarrierShipmentMethod to an array', () => {
        const carrierShipmentMethodArray: ICarrierShipmentMethod[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const carrierShipmentMethodCollection: ICarrierShipmentMethod[] = [sampleWithRequiredData];
        expectedResult = service.addCarrierShipmentMethodToCollectionIfMissing(
          carrierShipmentMethodCollection,
          ...carrierShipmentMethodArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const carrierShipmentMethod: ICarrierShipmentMethod = sampleWithRequiredData;
        const carrierShipmentMethod2: ICarrierShipmentMethod = sampleWithPartialData;
        expectedResult = service.addCarrierShipmentMethodToCollectionIfMissing([], carrierShipmentMethod, carrierShipmentMethod2);
        expect(expectedResult).toEqual([carrierShipmentMethod, carrierShipmentMethod2]);
      });

      it('should accept null and undefined values', () => {
        const carrierShipmentMethod: ICarrierShipmentMethod = sampleWithRequiredData;
        expectedResult = service.addCarrierShipmentMethodToCollectionIfMissing([], null, carrierShipmentMethod, undefined);
        expect(expectedResult).toEqual([carrierShipmentMethod]);
      });

      it('should return initial array if no CarrierShipmentMethod is added', () => {
        const carrierShipmentMethodCollection: ICarrierShipmentMethod[] = [sampleWithRequiredData];
        expectedResult = service.addCarrierShipmentMethodToCollectionIfMissing(carrierShipmentMethodCollection, undefined, null);
        expect(expectedResult).toEqual(carrierShipmentMethodCollection);
      });
    });

    describe('compareCarrierShipmentMethod', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCarrierShipmentMethod(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14904 };
        const entity2 = null;

        const compareResult1 = service.compareCarrierShipmentMethod(entity1, entity2);
        const compareResult2 = service.compareCarrierShipmentMethod(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14904 };
        const entity2 = { id: 11348 };

        const compareResult1 = service.compareCarrierShipmentMethod(entity1, entity2);
        const compareResult2 = service.compareCarrierShipmentMethod(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 14904 };
        const entity2 = { id: 14904 };

        const compareResult1 = service.compareCarrierShipmentMethod(entity1, entity2);
        const compareResult2 = service.compareCarrierShipmentMethod(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
