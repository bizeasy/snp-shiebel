import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyType } from '../party-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../party-type.test-samples';

import { PartyTypeService } from './party-type.service';

const requireRestSample: IPartyType = {
  ...sampleWithRequiredData,
};

describe('PartyType Service', () => {
  let service: PartyTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyType | IPartyType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyTypeService);
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

    it('should create a PartyType', () => {
      const partyType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyType', () => {
      const partyType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyTypeToCollectionIfMissing', () => {
      it('should add a PartyType to an empty array', () => {
        const partyType: IPartyType = sampleWithRequiredData;
        expectedResult = service.addPartyTypeToCollectionIfMissing([], partyType);
        expect(expectedResult).toEqual([partyType]);
      });

      it('should not add a PartyType to an array that contains it', () => {
        const partyType: IPartyType = sampleWithRequiredData;
        const partyTypeCollection: IPartyType[] = [
          {
            ...partyType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyTypeToCollectionIfMissing(partyTypeCollection, partyType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyType to an array that doesn't contain it", () => {
        const partyType: IPartyType = sampleWithRequiredData;
        const partyTypeCollection: IPartyType[] = [sampleWithPartialData];
        expectedResult = service.addPartyTypeToCollectionIfMissing(partyTypeCollection, partyType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyType);
      });

      it('should add only unique PartyType to an array', () => {
        const partyTypeArray: IPartyType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partyTypeCollection: IPartyType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyTypeToCollectionIfMissing(partyTypeCollection, ...partyTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyType: IPartyType = sampleWithRequiredData;
        const partyType2: IPartyType = sampleWithPartialData;
        expectedResult = service.addPartyTypeToCollectionIfMissing([], partyType, partyType2);
        expect(expectedResult).toEqual([partyType, partyType2]);
      });

      it('should accept null and undefined values', () => {
        const partyType: IPartyType = sampleWithRequiredData;
        expectedResult = service.addPartyTypeToCollectionIfMissing([], null, partyType, undefined);
        expect(expectedResult).toEqual([partyType]);
      });

      it('should return initial array if no PartyType is added', () => {
        const partyTypeCollection: IPartyType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyTypeToCollectionIfMissing(partyTypeCollection, undefined, null);
        expect(expectedResult).toEqual(partyTypeCollection);
      });
    });

    describe('comparePartyType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3927 };
        const entity2 = null;

        const compareResult1 = service.comparePartyType(entity1, entity2);
        const compareResult2 = service.comparePartyType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3927 };
        const entity2 = { id: 27525 };

        const compareResult1 = service.comparePartyType(entity1, entity2);
        const compareResult2 = service.comparePartyType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 3927 };
        const entity2 = { id: 3927 };

        const compareResult1 = service.comparePartyType(entity1, entity2);
        const compareResult2 = service.comparePartyType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
