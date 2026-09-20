import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyRelationshipType } from '../party-relationship-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../party-relationship-type.test-samples';

import { PartyRelationshipTypeService } from './party-relationship-type.service';

const requireRestSample: IPartyRelationshipType = {
  ...sampleWithRequiredData,
};

describe('PartyRelationshipType Service', () => {
  let service: PartyRelationshipTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyRelationshipType | IPartyRelationshipType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyRelationshipTypeService);
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

    it('should create a PartyRelationshipType', () => {
      const partyRelationshipType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyRelationshipType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyRelationshipType', () => {
      const partyRelationshipType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyRelationshipType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyRelationshipType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyRelationshipType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyRelationshipType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyRelationshipTypeToCollectionIfMissing', () => {
      it('should add a PartyRelationshipType to an empty array', () => {
        const partyRelationshipType: IPartyRelationshipType = sampleWithRequiredData;
        expectedResult = service.addPartyRelationshipTypeToCollectionIfMissing([], partyRelationshipType);
        expect(expectedResult).toEqual([partyRelationshipType]);
      });

      it('should not add a PartyRelationshipType to an array that contains it', () => {
        const partyRelationshipType: IPartyRelationshipType = sampleWithRequiredData;
        const partyRelationshipTypeCollection: IPartyRelationshipType[] = [
          {
            ...partyRelationshipType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyRelationshipTypeToCollectionIfMissing(partyRelationshipTypeCollection, partyRelationshipType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyRelationshipType to an array that doesn't contain it", () => {
        const partyRelationshipType: IPartyRelationshipType = sampleWithRequiredData;
        const partyRelationshipTypeCollection: IPartyRelationshipType[] = [sampleWithPartialData];
        expectedResult = service.addPartyRelationshipTypeToCollectionIfMissing(partyRelationshipTypeCollection, partyRelationshipType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyRelationshipType);
      });

      it('should add only unique PartyRelationshipType to an array', () => {
        const partyRelationshipTypeArray: IPartyRelationshipType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partyRelationshipTypeCollection: IPartyRelationshipType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyRelationshipTypeToCollectionIfMissing(
          partyRelationshipTypeCollection,
          ...partyRelationshipTypeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyRelationshipType: IPartyRelationshipType = sampleWithRequiredData;
        const partyRelationshipType2: IPartyRelationshipType = sampleWithPartialData;
        expectedResult = service.addPartyRelationshipTypeToCollectionIfMissing([], partyRelationshipType, partyRelationshipType2);
        expect(expectedResult).toEqual([partyRelationshipType, partyRelationshipType2]);
      });

      it('should accept null and undefined values', () => {
        const partyRelationshipType: IPartyRelationshipType = sampleWithRequiredData;
        expectedResult = service.addPartyRelationshipTypeToCollectionIfMissing([], null, partyRelationshipType, undefined);
        expect(expectedResult).toEqual([partyRelationshipType]);
      });

      it('should return initial array if no PartyRelationshipType is added', () => {
        const partyRelationshipTypeCollection: IPartyRelationshipType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyRelationshipTypeToCollectionIfMissing(partyRelationshipTypeCollection, undefined, null);
        expect(expectedResult).toEqual(partyRelationshipTypeCollection);
      });
    });

    describe('comparePartyRelationshipType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyRelationshipType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13343 };
        const entity2 = null;

        const compareResult1 = service.comparePartyRelationshipType(entity1, entity2);
        const compareResult2 = service.comparePartyRelationshipType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13343 };
        const entity2 = { id: 5094 };

        const compareResult1 = service.comparePartyRelationshipType(entity1, entity2);
        const compareResult2 = service.comparePartyRelationshipType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 13343 };
        const entity2 = { id: 13343 };

        const compareResult1 = service.comparePartyRelationshipType(entity1, entity2);
        const compareResult2 = service.comparePartyRelationshipType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
