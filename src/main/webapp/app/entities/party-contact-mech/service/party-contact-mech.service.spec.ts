import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyContactMech } from '../party-contact-mech.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../party-contact-mech.test-samples';

import { PartyContactMechService, RestPartyContactMech } from './party-contact-mech.service';

const requireRestSample: RestPartyContactMech = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('PartyContactMech Service', () => {
  let service: PartyContactMechService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyContactMech | IPartyContactMech[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyContactMechService);
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

    it('should create a PartyContactMech', () => {
      const partyContactMech = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyContactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyContactMech', () => {
      const partyContactMech = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyContactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyContactMech', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyContactMech', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyContactMech', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyContactMechToCollectionIfMissing', () => {
      it('should add a PartyContactMech to an empty array', () => {
        const partyContactMech: IPartyContactMech = sampleWithRequiredData;
        expectedResult = service.addPartyContactMechToCollectionIfMissing([], partyContactMech);
        expect(expectedResult).toEqual([partyContactMech]);
      });

      it('should not add a PartyContactMech to an array that contains it', () => {
        const partyContactMech: IPartyContactMech = sampleWithRequiredData;
        const partyContactMechCollection: IPartyContactMech[] = [
          {
            ...partyContactMech,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyContactMechToCollectionIfMissing(partyContactMechCollection, partyContactMech);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyContactMech to an array that doesn't contain it", () => {
        const partyContactMech: IPartyContactMech = sampleWithRequiredData;
        const partyContactMechCollection: IPartyContactMech[] = [sampleWithPartialData];
        expectedResult = service.addPartyContactMechToCollectionIfMissing(partyContactMechCollection, partyContactMech);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyContactMech);
      });

      it('should add only unique PartyContactMech to an array', () => {
        const partyContactMechArray: IPartyContactMech[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partyContactMechCollection: IPartyContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addPartyContactMechToCollectionIfMissing(partyContactMechCollection, ...partyContactMechArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyContactMech: IPartyContactMech = sampleWithRequiredData;
        const partyContactMech2: IPartyContactMech = sampleWithPartialData;
        expectedResult = service.addPartyContactMechToCollectionIfMissing([], partyContactMech, partyContactMech2);
        expect(expectedResult).toEqual([partyContactMech, partyContactMech2]);
      });

      it('should accept null and undefined values', () => {
        const partyContactMech: IPartyContactMech = sampleWithRequiredData;
        expectedResult = service.addPartyContactMechToCollectionIfMissing([], null, partyContactMech, undefined);
        expect(expectedResult).toEqual([partyContactMech]);
      });

      it('should return initial array if no PartyContactMech is added', () => {
        const partyContactMechCollection: IPartyContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addPartyContactMechToCollectionIfMissing(partyContactMechCollection, undefined, null);
        expect(expectedResult).toEqual(partyContactMechCollection);
      });
    });

    describe('comparePartyContactMech', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyContactMech(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18082 };
        const entity2 = null;

        const compareResult1 = service.comparePartyContactMech(entity1, entity2);
        const compareResult2 = service.comparePartyContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18082 };
        const entity2 = { id: 29906 };

        const compareResult1 = service.comparePartyContactMech(entity1, entity2);
        const compareResult2 = service.comparePartyContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18082 };
        const entity2 = { id: 18082 };

        const compareResult1 = service.comparePartyContactMech(entity1, entity2);
        const compareResult2 = service.comparePartyContactMech(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
