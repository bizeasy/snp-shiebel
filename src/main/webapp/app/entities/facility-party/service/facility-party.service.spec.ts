import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacilityParty } from '../facility-party.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../facility-party.test-samples';

import { FacilityPartyService, RestFacilityParty } from './facility-party.service';

const requireRestSample: RestFacilityParty = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('FacilityParty Service', () => {
  let service: FacilityPartyService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacilityParty | IFacilityParty[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityPartyService);
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

    it('should create a FacilityParty', () => {
      const facilityParty = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facilityParty).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacilityParty', () => {
      const facilityParty = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facilityParty).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacilityParty', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacilityParty', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacilityParty', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityPartyToCollectionIfMissing', () => {
      it('should add a FacilityParty to an empty array', () => {
        const facilityParty: IFacilityParty = sampleWithRequiredData;
        expectedResult = service.addFacilityPartyToCollectionIfMissing([], facilityParty);
        expect(expectedResult).toEqual([facilityParty]);
      });

      it('should not add a FacilityParty to an array that contains it', () => {
        const facilityParty: IFacilityParty = sampleWithRequiredData;
        const facilityPartyCollection: IFacilityParty[] = [
          {
            ...facilityParty,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityPartyToCollectionIfMissing(facilityPartyCollection, facilityParty);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacilityParty to an array that doesn't contain it", () => {
        const facilityParty: IFacilityParty = sampleWithRequiredData;
        const facilityPartyCollection: IFacilityParty[] = [sampleWithPartialData];
        expectedResult = service.addFacilityPartyToCollectionIfMissing(facilityPartyCollection, facilityParty);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facilityParty);
      });

      it('should add only unique FacilityParty to an array', () => {
        const facilityPartyArray: IFacilityParty[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facilityPartyCollection: IFacilityParty[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityPartyToCollectionIfMissing(facilityPartyCollection, ...facilityPartyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facilityParty: IFacilityParty = sampleWithRequiredData;
        const facilityParty2: IFacilityParty = sampleWithPartialData;
        expectedResult = service.addFacilityPartyToCollectionIfMissing([], facilityParty, facilityParty2);
        expect(expectedResult).toEqual([facilityParty, facilityParty2]);
      });

      it('should accept null and undefined values', () => {
        const facilityParty: IFacilityParty = sampleWithRequiredData;
        expectedResult = service.addFacilityPartyToCollectionIfMissing([], null, facilityParty, undefined);
        expect(expectedResult).toEqual([facilityParty]);
      });

      it('should return initial array if no FacilityParty is added', () => {
        const facilityPartyCollection: IFacilityParty[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityPartyToCollectionIfMissing(facilityPartyCollection, undefined, null);
        expect(expectedResult).toEqual(facilityPartyCollection);
      });
    });

    describe('compareFacilityParty', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacilityParty(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9556 };
        const entity2 = null;

        const compareResult1 = service.compareFacilityParty(entity1, entity2);
        const compareResult2 = service.compareFacilityParty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9556 };
        const entity2 = { id: 15320 };

        const compareResult1 = service.compareFacilityParty(entity1, entity2);
        const compareResult2 = service.compareFacilityParty(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 9556 };
        const entity2 = { id: 9556 };

        const compareResult1 = service.compareFacilityParty(entity1, entity2);
        const compareResult2 = service.compareFacilityParty(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
