import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyClassificationType } from '../party-classification-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../party-classification-type.test-samples';

import { PartyClassificationTypeService } from './party-classification-type.service';

const requireRestSample: IPartyClassificationType = {
  ...sampleWithRequiredData,
};

describe('PartyClassificationType Service', () => {
  let service: PartyClassificationTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyClassificationType | IPartyClassificationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyClassificationTypeService);
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

    it('should create a PartyClassificationType', () => {
      const partyClassificationType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyClassificationType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyClassificationType', () => {
      const partyClassificationType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyClassificationType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyClassificationType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyClassificationType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyClassificationType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyClassificationTypeToCollectionIfMissing', () => {
      it('should add a PartyClassificationType to an empty array', () => {
        const partyClassificationType: IPartyClassificationType = sampleWithRequiredData;
        expectedResult = service.addPartyClassificationTypeToCollectionIfMissing([], partyClassificationType);
        expect(expectedResult).toEqual([partyClassificationType]);
      });

      it('should not add a PartyClassificationType to an array that contains it', () => {
        const partyClassificationType: IPartyClassificationType = sampleWithRequiredData;
        const partyClassificationTypeCollection: IPartyClassificationType[] = [
          {
            ...partyClassificationType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyClassificationTypeToCollectionIfMissing(
          partyClassificationTypeCollection,
          partyClassificationType,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyClassificationType to an array that doesn't contain it", () => {
        const partyClassificationType: IPartyClassificationType = sampleWithRequiredData;
        const partyClassificationTypeCollection: IPartyClassificationType[] = [sampleWithPartialData];
        expectedResult = service.addPartyClassificationTypeToCollectionIfMissing(
          partyClassificationTypeCollection,
          partyClassificationType,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyClassificationType);
      });

      it('should add only unique PartyClassificationType to an array', () => {
        const partyClassificationTypeArray: IPartyClassificationType[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const partyClassificationTypeCollection: IPartyClassificationType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyClassificationTypeToCollectionIfMissing(
          partyClassificationTypeCollection,
          ...partyClassificationTypeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyClassificationType: IPartyClassificationType = sampleWithRequiredData;
        const partyClassificationType2: IPartyClassificationType = sampleWithPartialData;
        expectedResult = service.addPartyClassificationTypeToCollectionIfMissing([], partyClassificationType, partyClassificationType2);
        expect(expectedResult).toEqual([partyClassificationType, partyClassificationType2]);
      });

      it('should accept null and undefined values', () => {
        const partyClassificationType: IPartyClassificationType = sampleWithRequiredData;
        expectedResult = service.addPartyClassificationTypeToCollectionIfMissing([], null, partyClassificationType, undefined);
        expect(expectedResult).toEqual([partyClassificationType]);
      });

      it('should return initial array if no PartyClassificationType is added', () => {
        const partyClassificationTypeCollection: IPartyClassificationType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyClassificationTypeToCollectionIfMissing(partyClassificationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(partyClassificationTypeCollection);
      });
    });

    describe('comparePartyClassificationType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyClassificationType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21929 };
        const entity2 = null;

        const compareResult1 = service.comparePartyClassificationType(entity1, entity2);
        const compareResult2 = service.comparePartyClassificationType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21929 };
        const entity2 = { id: 12767 };

        const compareResult1 = service.comparePartyClassificationType(entity1, entity2);
        const compareResult2 = service.comparePartyClassificationType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 21929 };
        const entity2 = { id: 21929 };

        const compareResult1 = service.comparePartyClassificationType(entity1, entity2);
        const compareResult2 = service.comparePartyClassificationType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
