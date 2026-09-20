import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyIdentificationType } from '../party-identification-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../party-identification-type.test-samples';

import { PartyIdentificationTypeService } from './party-identification-type.service';

const requireRestSample: IPartyIdentificationType = {
  ...sampleWithRequiredData,
};

describe('PartyIdentificationType Service', () => {
  let service: PartyIdentificationTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyIdentificationType | IPartyIdentificationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyIdentificationTypeService);
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

    it('should create a PartyIdentificationType', () => {
      const partyIdentificationType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyIdentificationType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyIdentificationType', () => {
      const partyIdentificationType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyIdentificationType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyIdentificationType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyIdentificationType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyIdentificationType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyIdentificationTypeToCollectionIfMissing', () => {
      it('should add a PartyIdentificationType to an empty array', () => {
        const partyIdentificationType: IPartyIdentificationType = sampleWithRequiredData;
        expectedResult = service.addPartyIdentificationTypeToCollectionIfMissing([], partyIdentificationType);
        expect(expectedResult).toEqual([partyIdentificationType]);
      });

      it('should not add a PartyIdentificationType to an array that contains it', () => {
        const partyIdentificationType: IPartyIdentificationType = sampleWithRequiredData;
        const partyIdentificationTypeCollection: IPartyIdentificationType[] = [
          {
            ...partyIdentificationType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyIdentificationTypeToCollectionIfMissing(
          partyIdentificationTypeCollection,
          partyIdentificationType,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyIdentificationType to an array that doesn't contain it", () => {
        const partyIdentificationType: IPartyIdentificationType = sampleWithRequiredData;
        const partyIdentificationTypeCollection: IPartyIdentificationType[] = [sampleWithPartialData];
        expectedResult = service.addPartyIdentificationTypeToCollectionIfMissing(
          partyIdentificationTypeCollection,
          partyIdentificationType,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyIdentificationType);
      });

      it('should add only unique PartyIdentificationType to an array', () => {
        const partyIdentificationTypeArray: IPartyIdentificationType[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const partyIdentificationTypeCollection: IPartyIdentificationType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyIdentificationTypeToCollectionIfMissing(
          partyIdentificationTypeCollection,
          ...partyIdentificationTypeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyIdentificationType: IPartyIdentificationType = sampleWithRequiredData;
        const partyIdentificationType2: IPartyIdentificationType = sampleWithPartialData;
        expectedResult = service.addPartyIdentificationTypeToCollectionIfMissing([], partyIdentificationType, partyIdentificationType2);
        expect(expectedResult).toEqual([partyIdentificationType, partyIdentificationType2]);
      });

      it('should accept null and undefined values', () => {
        const partyIdentificationType: IPartyIdentificationType = sampleWithRequiredData;
        expectedResult = service.addPartyIdentificationTypeToCollectionIfMissing([], null, partyIdentificationType, undefined);
        expect(expectedResult).toEqual([partyIdentificationType]);
      });

      it('should return initial array if no PartyIdentificationType is added', () => {
        const partyIdentificationTypeCollection: IPartyIdentificationType[] = [sampleWithRequiredData];
        expectedResult = service.addPartyIdentificationTypeToCollectionIfMissing(partyIdentificationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(partyIdentificationTypeCollection);
      });
    });

    describe('comparePartyIdentificationType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyIdentificationType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7941 };
        const entity2 = null;

        const compareResult1 = service.comparePartyIdentificationType(entity1, entity2);
        const compareResult2 = service.comparePartyIdentificationType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7941 };
        const entity2 = { id: 4837 };

        const compareResult1 = service.comparePartyIdentificationType(entity1, entity2);
        const compareResult2 = service.comparePartyIdentificationType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7941 };
        const entity2 = { id: 7941 };

        const compareResult1 = service.comparePartyIdentificationType(entity1, entity2);
        const compareResult2 = service.comparePartyIdentificationType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
