import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyClassificationGroup } from '../party-classification-group.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../party-classification-group.test-samples';

import { PartyClassificationGroupService } from './party-classification-group.service';

const requireRestSample: IPartyClassificationGroup = {
  ...sampleWithRequiredData,
};

describe('PartyClassificationGroup Service', () => {
  let service: PartyClassificationGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyClassificationGroup | IPartyClassificationGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyClassificationGroupService);
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

    it('should create a PartyClassificationGroup', () => {
      const partyClassificationGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyClassificationGroup).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyClassificationGroup', () => {
      const partyClassificationGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyClassificationGroup).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyClassificationGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyClassificationGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyClassificationGroup', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyClassificationGroupToCollectionIfMissing', () => {
      it('should add a PartyClassificationGroup to an empty array', () => {
        const partyClassificationGroup: IPartyClassificationGroup = sampleWithRequiredData;
        expectedResult = service.addPartyClassificationGroupToCollectionIfMissing([], partyClassificationGroup);
        expect(expectedResult).toEqual([partyClassificationGroup]);
      });

      it('should not add a PartyClassificationGroup to an array that contains it', () => {
        const partyClassificationGroup: IPartyClassificationGroup = sampleWithRequiredData;
        const partyClassificationGroupCollection: IPartyClassificationGroup[] = [
          {
            ...partyClassificationGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyClassificationGroupToCollectionIfMissing(
          partyClassificationGroupCollection,
          partyClassificationGroup,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyClassificationGroup to an array that doesn't contain it", () => {
        const partyClassificationGroup: IPartyClassificationGroup = sampleWithRequiredData;
        const partyClassificationGroupCollection: IPartyClassificationGroup[] = [sampleWithPartialData];
        expectedResult = service.addPartyClassificationGroupToCollectionIfMissing(
          partyClassificationGroupCollection,
          partyClassificationGroup,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyClassificationGroup);
      });

      it('should add only unique PartyClassificationGroup to an array', () => {
        const partyClassificationGroupArray: IPartyClassificationGroup[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const partyClassificationGroupCollection: IPartyClassificationGroup[] = [sampleWithRequiredData];
        expectedResult = service.addPartyClassificationGroupToCollectionIfMissing(
          partyClassificationGroupCollection,
          ...partyClassificationGroupArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyClassificationGroup: IPartyClassificationGroup = sampleWithRequiredData;
        const partyClassificationGroup2: IPartyClassificationGroup = sampleWithPartialData;
        expectedResult = service.addPartyClassificationGroupToCollectionIfMissing([], partyClassificationGroup, partyClassificationGroup2);
        expect(expectedResult).toEqual([partyClassificationGroup, partyClassificationGroup2]);
      });

      it('should accept null and undefined values', () => {
        const partyClassificationGroup: IPartyClassificationGroup = sampleWithRequiredData;
        expectedResult = service.addPartyClassificationGroupToCollectionIfMissing([], null, partyClassificationGroup, undefined);
        expect(expectedResult).toEqual([partyClassificationGroup]);
      });

      it('should return initial array if no PartyClassificationGroup is added', () => {
        const partyClassificationGroupCollection: IPartyClassificationGroup[] = [sampleWithRequiredData];
        expectedResult = service.addPartyClassificationGroupToCollectionIfMissing(partyClassificationGroupCollection, undefined, null);
        expect(expectedResult).toEqual(partyClassificationGroupCollection);
      });
    });

    describe('comparePartyClassificationGroup', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyClassificationGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8459 };
        const entity2 = null;

        const compareResult1 = service.comparePartyClassificationGroup(entity1, entity2);
        const compareResult2 = service.comparePartyClassificationGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8459 };
        const entity2 = { id: 28249 };

        const compareResult1 = service.comparePartyClassificationGroup(entity1, entity2);
        const compareResult2 = service.comparePartyClassificationGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8459 };
        const entity2 = { id: 8459 };

        const compareResult1 = service.comparePartyClassificationGroup(entity1, entity2);
        const compareResult2 = service.comparePartyClassificationGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
