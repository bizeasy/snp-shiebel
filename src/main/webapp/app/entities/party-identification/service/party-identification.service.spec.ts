import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyIdentification } from '../party-identification.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../party-identification.test-samples';

import { PartyIdentificationService } from './party-identification.service';

const requireRestSample: IPartyIdentification = {
  ...sampleWithRequiredData,
};

describe('PartyIdentification Service', () => {
  let service: PartyIdentificationService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyIdentification | IPartyIdentification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyIdentificationService);
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

    it('should create a PartyIdentification', () => {
      const partyIdentification = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyIdentification).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyIdentification', () => {
      const partyIdentification = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyIdentification).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyIdentification', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyIdentification', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyIdentification', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyIdentificationToCollectionIfMissing', () => {
      it('should add a PartyIdentification to an empty array', () => {
        const partyIdentification: IPartyIdentification = sampleWithRequiredData;
        expectedResult = service.addPartyIdentificationToCollectionIfMissing([], partyIdentification);
        expect(expectedResult).toEqual([partyIdentification]);
      });

      it('should not add a PartyIdentification to an array that contains it', () => {
        const partyIdentification: IPartyIdentification = sampleWithRequiredData;
        const partyIdentificationCollection: IPartyIdentification[] = [
          {
            ...partyIdentification,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyIdentificationToCollectionIfMissing(partyIdentificationCollection, partyIdentification);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyIdentification to an array that doesn't contain it", () => {
        const partyIdentification: IPartyIdentification = sampleWithRequiredData;
        const partyIdentificationCollection: IPartyIdentification[] = [sampleWithPartialData];
        expectedResult = service.addPartyIdentificationToCollectionIfMissing(partyIdentificationCollection, partyIdentification);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyIdentification);
      });

      it('should add only unique PartyIdentification to an array', () => {
        const partyIdentificationArray: IPartyIdentification[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partyIdentificationCollection: IPartyIdentification[] = [sampleWithRequiredData];
        expectedResult = service.addPartyIdentificationToCollectionIfMissing(partyIdentificationCollection, ...partyIdentificationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyIdentification: IPartyIdentification = sampleWithRequiredData;
        const partyIdentification2: IPartyIdentification = sampleWithPartialData;
        expectedResult = service.addPartyIdentificationToCollectionIfMissing([], partyIdentification, partyIdentification2);
        expect(expectedResult).toEqual([partyIdentification, partyIdentification2]);
      });

      it('should accept null and undefined values', () => {
        const partyIdentification: IPartyIdentification = sampleWithRequiredData;
        expectedResult = service.addPartyIdentificationToCollectionIfMissing([], null, partyIdentification, undefined);
        expect(expectedResult).toEqual([partyIdentification]);
      });

      it('should return initial array if no PartyIdentification is added', () => {
        const partyIdentificationCollection: IPartyIdentification[] = [sampleWithRequiredData];
        expectedResult = service.addPartyIdentificationToCollectionIfMissing(partyIdentificationCollection, undefined, null);
        expect(expectedResult).toEqual(partyIdentificationCollection);
      });
    });

    describe('comparePartyIdentification', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyIdentification(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24654 };
        const entity2 = null;

        const compareResult1 = service.comparePartyIdentification(entity1, entity2);
        const compareResult2 = service.comparePartyIdentification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24654 };
        const entity2 = { id: 10445 };

        const compareResult1 = service.comparePartyIdentification(entity1, entity2);
        const compareResult2 = service.comparePartyIdentification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 24654 };
        const entity2 = { id: 24654 };

        const compareResult1 = service.comparePartyIdentification(entity1, entity2);
        const compareResult2 = service.comparePartyIdentification(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
