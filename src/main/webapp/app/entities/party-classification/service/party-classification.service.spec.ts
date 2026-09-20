import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyClassification } from '../party-classification.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../party-classification.test-samples';

import { PartyClassificationService, RestPartyClassification } from './party-classification.service';

const requireRestSample: RestPartyClassification = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('PartyClassification Service', () => {
  let service: PartyClassificationService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyClassification | IPartyClassification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyClassificationService);
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

    it('should create a PartyClassification', () => {
      const partyClassification = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyClassification).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyClassification', () => {
      const partyClassification = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyClassification).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyClassification', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyClassification', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyClassification', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyClassificationToCollectionIfMissing', () => {
      it('should add a PartyClassification to an empty array', () => {
        const partyClassification: IPartyClassification = sampleWithRequiredData;
        expectedResult = service.addPartyClassificationToCollectionIfMissing([], partyClassification);
        expect(expectedResult).toEqual([partyClassification]);
      });

      it('should not add a PartyClassification to an array that contains it', () => {
        const partyClassification: IPartyClassification = sampleWithRequiredData;
        const partyClassificationCollection: IPartyClassification[] = [
          {
            ...partyClassification,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyClassificationToCollectionIfMissing(partyClassificationCollection, partyClassification);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyClassification to an array that doesn't contain it", () => {
        const partyClassification: IPartyClassification = sampleWithRequiredData;
        const partyClassificationCollection: IPartyClassification[] = [sampleWithPartialData];
        expectedResult = service.addPartyClassificationToCollectionIfMissing(partyClassificationCollection, partyClassification);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyClassification);
      });

      it('should add only unique PartyClassification to an array', () => {
        const partyClassificationArray: IPartyClassification[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partyClassificationCollection: IPartyClassification[] = [sampleWithRequiredData];
        expectedResult = service.addPartyClassificationToCollectionIfMissing(partyClassificationCollection, ...partyClassificationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyClassification: IPartyClassification = sampleWithRequiredData;
        const partyClassification2: IPartyClassification = sampleWithPartialData;
        expectedResult = service.addPartyClassificationToCollectionIfMissing([], partyClassification, partyClassification2);
        expect(expectedResult).toEqual([partyClassification, partyClassification2]);
      });

      it('should accept null and undefined values', () => {
        const partyClassification: IPartyClassification = sampleWithRequiredData;
        expectedResult = service.addPartyClassificationToCollectionIfMissing([], null, partyClassification, undefined);
        expect(expectedResult).toEqual([partyClassification]);
      });

      it('should return initial array if no PartyClassification is added', () => {
        const partyClassificationCollection: IPartyClassification[] = [sampleWithRequiredData];
        expectedResult = service.addPartyClassificationToCollectionIfMissing(partyClassificationCollection, undefined, null);
        expect(expectedResult).toEqual(partyClassificationCollection);
      });
    });

    describe('comparePartyClassification', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyClassification(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21352 };
        const entity2 = null;

        const compareResult1 = service.comparePartyClassification(entity1, entity2);
        const compareResult2 = service.comparePartyClassification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21352 };
        const entity2 = { id: 12846 };

        const compareResult1 = service.comparePartyClassification(entity1, entity2);
        const compareResult2 = service.comparePartyClassification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 21352 };
        const entity2 = { id: 21352 };

        const compareResult1 = service.comparePartyClassification(entity1, entity2);
        const compareResult2 = service.comparePartyClassification(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
