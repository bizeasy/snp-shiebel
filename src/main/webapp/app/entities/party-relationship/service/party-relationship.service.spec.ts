import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyRelationship } from '../party-relationship.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../party-relationship.test-samples';

import { PartyRelationshipService, RestPartyRelationship } from './party-relationship.service';

const requireRestSample: RestPartyRelationship = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('PartyRelationship Service', () => {
  let service: PartyRelationshipService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyRelationship | IPartyRelationship[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyRelationshipService);
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

    it('should create a PartyRelationship', () => {
      const partyRelationship = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyRelationship).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyRelationship', () => {
      const partyRelationship = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyRelationship).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyRelationship', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyRelationship', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyRelationship', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyRelationshipToCollectionIfMissing', () => {
      it('should add a PartyRelationship to an empty array', () => {
        const partyRelationship: IPartyRelationship = sampleWithRequiredData;
        expectedResult = service.addPartyRelationshipToCollectionIfMissing([], partyRelationship);
        expect(expectedResult).toEqual([partyRelationship]);
      });

      it('should not add a PartyRelationship to an array that contains it', () => {
        const partyRelationship: IPartyRelationship = sampleWithRequiredData;
        const partyRelationshipCollection: IPartyRelationship[] = [
          {
            ...partyRelationship,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyRelationshipToCollectionIfMissing(partyRelationshipCollection, partyRelationship);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyRelationship to an array that doesn't contain it", () => {
        const partyRelationship: IPartyRelationship = sampleWithRequiredData;
        const partyRelationshipCollection: IPartyRelationship[] = [sampleWithPartialData];
        expectedResult = service.addPartyRelationshipToCollectionIfMissing(partyRelationshipCollection, partyRelationship);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyRelationship);
      });

      it('should add only unique PartyRelationship to an array', () => {
        const partyRelationshipArray: IPartyRelationship[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partyRelationshipCollection: IPartyRelationship[] = [sampleWithRequiredData];
        expectedResult = service.addPartyRelationshipToCollectionIfMissing(partyRelationshipCollection, ...partyRelationshipArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyRelationship: IPartyRelationship = sampleWithRequiredData;
        const partyRelationship2: IPartyRelationship = sampleWithPartialData;
        expectedResult = service.addPartyRelationshipToCollectionIfMissing([], partyRelationship, partyRelationship2);
        expect(expectedResult).toEqual([partyRelationship, partyRelationship2]);
      });

      it('should accept null and undefined values', () => {
        const partyRelationship: IPartyRelationship = sampleWithRequiredData;
        expectedResult = service.addPartyRelationshipToCollectionIfMissing([], null, partyRelationship, undefined);
        expect(expectedResult).toEqual([partyRelationship]);
      });

      it('should return initial array if no PartyRelationship is added', () => {
        const partyRelationshipCollection: IPartyRelationship[] = [sampleWithRequiredData];
        expectedResult = service.addPartyRelationshipToCollectionIfMissing(partyRelationshipCollection, undefined, null);
        expect(expectedResult).toEqual(partyRelationshipCollection);
      });
    });

    describe('comparePartyRelationship', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyRelationship(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18196 };
        const entity2 = null;

        const compareResult1 = service.comparePartyRelationship(entity1, entity2);
        const compareResult2 = service.comparePartyRelationship(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18196 };
        const entity2 = { id: 31647 };

        const compareResult1 = service.comparePartyRelationship(entity1, entity2);
        const compareResult2 = service.comparePartyRelationship(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18196 };
        const entity2 = { id: 18196 };

        const compareResult1 = service.comparePartyRelationship(entity1, entity2);
        const compareResult2 = service.comparePartyRelationship(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
