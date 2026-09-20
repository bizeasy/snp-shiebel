import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacility } from '../facility.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../facility.test-samples';

import { FacilityService } from './facility.service';

const requireRestSample: IFacility = {
  ...sampleWithRequiredData,
};

describe('Facility Service', () => {
  let service: FacilityService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacility | IFacility[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityService);
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

    it('should create a Facility', () => {
      const facility = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facility).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Facility', () => {
      const facility = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facility).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Facility', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Facility', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Facility', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityToCollectionIfMissing', () => {
      it('should add a Facility to an empty array', () => {
        const facility: IFacility = sampleWithRequiredData;
        expectedResult = service.addFacilityToCollectionIfMissing([], facility);
        expect(expectedResult).toEqual([facility]);
      });

      it('should not add a Facility to an array that contains it', () => {
        const facility: IFacility = sampleWithRequiredData;
        const facilityCollection: IFacility[] = [
          {
            ...facility,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityToCollectionIfMissing(facilityCollection, facility);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Facility to an array that doesn't contain it", () => {
        const facility: IFacility = sampleWithRequiredData;
        const facilityCollection: IFacility[] = [sampleWithPartialData];
        expectedResult = service.addFacilityToCollectionIfMissing(facilityCollection, facility);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facility);
      });

      it('should add only unique Facility to an array', () => {
        const facilityArray: IFacility[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facilityCollection: IFacility[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityToCollectionIfMissing(facilityCollection, ...facilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facility: IFacility = sampleWithRequiredData;
        const facility2: IFacility = sampleWithPartialData;
        expectedResult = service.addFacilityToCollectionIfMissing([], facility, facility2);
        expect(expectedResult).toEqual([facility, facility2]);
      });

      it('should accept null and undefined values', () => {
        const facility: IFacility = sampleWithRequiredData;
        expectedResult = service.addFacilityToCollectionIfMissing([], null, facility, undefined);
        expect(expectedResult).toEqual([facility]);
      });

      it('should return initial array if no Facility is added', () => {
        const facilityCollection: IFacility[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityToCollectionIfMissing(facilityCollection, undefined, null);
        expect(expectedResult).toEqual(facilityCollection);
      });
    });

    describe('compareFacility', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacility(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4866 };
        const entity2 = null;

        const compareResult1 = service.compareFacility(entity1, entity2);
        const compareResult2 = service.compareFacility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4866 };
        const entity2 = { id: 6958 };

        const compareResult1 = service.compareFacility(entity1, entity2);
        const compareResult2 = service.compareFacility(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4866 };
        const entity2 = { id: 4866 };

        const compareResult1 = service.compareFacility(entity1, entity2);
        const compareResult2 = service.compareFacility(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
