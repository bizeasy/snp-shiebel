import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacilityCarrierShipment } from '../facility-carrier-shipment.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../facility-carrier-shipment.test-samples';

import { FacilityCarrierShipmentService } from './facility-carrier-shipment.service';

const requireRestSample: IFacilityCarrierShipment = {
  ...sampleWithRequiredData,
};

describe('FacilityCarrierShipment Service', () => {
  let service: FacilityCarrierShipmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacilityCarrierShipment | IFacilityCarrierShipment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityCarrierShipmentService);
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

    it('should create a FacilityCarrierShipment', () => {
      const facilityCarrierShipment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facilityCarrierShipment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacilityCarrierShipment', () => {
      const facilityCarrierShipment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facilityCarrierShipment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacilityCarrierShipment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacilityCarrierShipment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacilityCarrierShipment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityCarrierShipmentToCollectionIfMissing', () => {
      it('should add a FacilityCarrierShipment to an empty array', () => {
        const facilityCarrierShipment: IFacilityCarrierShipment = sampleWithRequiredData;
        expectedResult = service.addFacilityCarrierShipmentToCollectionIfMissing([], facilityCarrierShipment);
        expect(expectedResult).toEqual([facilityCarrierShipment]);
      });

      it('should not add a FacilityCarrierShipment to an array that contains it', () => {
        const facilityCarrierShipment: IFacilityCarrierShipment = sampleWithRequiredData;
        const facilityCarrierShipmentCollection: IFacilityCarrierShipment[] = [
          {
            ...facilityCarrierShipment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityCarrierShipmentToCollectionIfMissing(
          facilityCarrierShipmentCollection,
          facilityCarrierShipment,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacilityCarrierShipment to an array that doesn't contain it", () => {
        const facilityCarrierShipment: IFacilityCarrierShipment = sampleWithRequiredData;
        const facilityCarrierShipmentCollection: IFacilityCarrierShipment[] = [sampleWithPartialData];
        expectedResult = service.addFacilityCarrierShipmentToCollectionIfMissing(
          facilityCarrierShipmentCollection,
          facilityCarrierShipment,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facilityCarrierShipment);
      });

      it('should add only unique FacilityCarrierShipment to an array', () => {
        const facilityCarrierShipmentArray: IFacilityCarrierShipment[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const facilityCarrierShipmentCollection: IFacilityCarrierShipment[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityCarrierShipmentToCollectionIfMissing(
          facilityCarrierShipmentCollection,
          ...facilityCarrierShipmentArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facilityCarrierShipment: IFacilityCarrierShipment = sampleWithRequiredData;
        const facilityCarrierShipment2: IFacilityCarrierShipment = sampleWithPartialData;
        expectedResult = service.addFacilityCarrierShipmentToCollectionIfMissing([], facilityCarrierShipment, facilityCarrierShipment2);
        expect(expectedResult).toEqual([facilityCarrierShipment, facilityCarrierShipment2]);
      });

      it('should accept null and undefined values', () => {
        const facilityCarrierShipment: IFacilityCarrierShipment = sampleWithRequiredData;
        expectedResult = service.addFacilityCarrierShipmentToCollectionIfMissing([], null, facilityCarrierShipment, undefined);
        expect(expectedResult).toEqual([facilityCarrierShipment]);
      });

      it('should return initial array if no FacilityCarrierShipment is added', () => {
        const facilityCarrierShipmentCollection: IFacilityCarrierShipment[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityCarrierShipmentToCollectionIfMissing(facilityCarrierShipmentCollection, undefined, null);
        expect(expectedResult).toEqual(facilityCarrierShipmentCollection);
      });
    });

    describe('compareFacilityCarrierShipment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacilityCarrierShipment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 12764 };
        const entity2 = null;

        const compareResult1 = service.compareFacilityCarrierShipment(entity1, entity2);
        const compareResult2 = service.compareFacilityCarrierShipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 12764 };
        const entity2 = { id: 31799 };

        const compareResult1 = service.compareFacilityCarrierShipment(entity1, entity2);
        const compareResult2 = service.compareFacilityCarrierShipment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 12764 };
        const entity2 = { id: 12764 };

        const compareResult1 = service.compareFacilityCarrierShipment(entity1, entity2);
        const compareResult2 = service.compareFacilityCarrierShipment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
