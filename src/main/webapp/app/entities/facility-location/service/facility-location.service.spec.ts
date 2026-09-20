import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacilityLocation } from '../facility-location.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../facility-location.test-samples';

import { FacilityLocationService } from './facility-location.service';

const requireRestSample: IFacilityLocation = {
  ...sampleWithRequiredData,
};

describe('FacilityLocation Service', () => {
  let service: FacilityLocationService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacilityLocation | IFacilityLocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityLocationService);
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

    it('should create a FacilityLocation', () => {
      const facilityLocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facilityLocation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacilityLocation', () => {
      const facilityLocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facilityLocation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacilityLocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacilityLocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacilityLocation', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityLocationToCollectionIfMissing', () => {
      it('should add a FacilityLocation to an empty array', () => {
        const facilityLocation: IFacilityLocation = sampleWithRequiredData;
        expectedResult = service.addFacilityLocationToCollectionIfMissing([], facilityLocation);
        expect(expectedResult).toEqual([facilityLocation]);
      });

      it('should not add a FacilityLocation to an array that contains it', () => {
        const facilityLocation: IFacilityLocation = sampleWithRequiredData;
        const facilityLocationCollection: IFacilityLocation[] = [
          {
            ...facilityLocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityLocationToCollectionIfMissing(facilityLocationCollection, facilityLocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacilityLocation to an array that doesn't contain it", () => {
        const facilityLocation: IFacilityLocation = sampleWithRequiredData;
        const facilityLocationCollection: IFacilityLocation[] = [sampleWithPartialData];
        expectedResult = service.addFacilityLocationToCollectionIfMissing(facilityLocationCollection, facilityLocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facilityLocation);
      });

      it('should add only unique FacilityLocation to an array', () => {
        const facilityLocationArray: IFacilityLocation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facilityLocationCollection: IFacilityLocation[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityLocationToCollectionIfMissing(facilityLocationCollection, ...facilityLocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facilityLocation: IFacilityLocation = sampleWithRequiredData;
        const facilityLocation2: IFacilityLocation = sampleWithPartialData;
        expectedResult = service.addFacilityLocationToCollectionIfMissing([], facilityLocation, facilityLocation2);
        expect(expectedResult).toEqual([facilityLocation, facilityLocation2]);
      });

      it('should accept null and undefined values', () => {
        const facilityLocation: IFacilityLocation = sampleWithRequiredData;
        expectedResult = service.addFacilityLocationToCollectionIfMissing([], null, facilityLocation, undefined);
        expect(expectedResult).toEqual([facilityLocation]);
      });

      it('should return initial array if no FacilityLocation is added', () => {
        const facilityLocationCollection: IFacilityLocation[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityLocationToCollectionIfMissing(facilityLocationCollection, undefined, null);
        expect(expectedResult).toEqual(facilityLocationCollection);
      });
    });

    describe('compareFacilityLocation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacilityLocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 76 };
        const entity2 = null;

        const compareResult1 = service.compareFacilityLocation(entity1, entity2);
        const compareResult2 = service.compareFacilityLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 76 };
        const entity2 = { id: 28655 };

        const compareResult1 = service.compareFacilityLocation(entity1, entity2);
        const compareResult2 = service.compareFacilityLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 76 };
        const entity2 = { id: 76 };

        const compareResult1 = service.compareFacilityLocation(entity1, entity2);
        const compareResult2 = service.compareFacilityLocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
