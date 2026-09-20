import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPartyRole } from '../party-role.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../party-role.test-samples';

import { PartyRoleService } from './party-role.service';

const requireRestSample: IPartyRole = {
  ...sampleWithRequiredData,
};

describe('PartyRole Service', () => {
  let service: PartyRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IPartyRole | IPartyRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PartyRoleService);
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

    it('should create a PartyRole', () => {
      const partyRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(partyRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartyRole', () => {
      const partyRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(partyRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartyRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartyRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PartyRole', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPartyRoleToCollectionIfMissing', () => {
      it('should add a PartyRole to an empty array', () => {
        const partyRole: IPartyRole = sampleWithRequiredData;
        expectedResult = service.addPartyRoleToCollectionIfMissing([], partyRole);
        expect(expectedResult).toEqual([partyRole]);
      });

      it('should not add a PartyRole to an array that contains it', () => {
        const partyRole: IPartyRole = sampleWithRequiredData;
        const partyRoleCollection: IPartyRole[] = [
          {
            ...partyRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartyRoleToCollectionIfMissing(partyRoleCollection, partyRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartyRole to an array that doesn't contain it", () => {
        const partyRole: IPartyRole = sampleWithRequiredData;
        const partyRoleCollection: IPartyRole[] = [sampleWithPartialData];
        expectedResult = service.addPartyRoleToCollectionIfMissing(partyRoleCollection, partyRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partyRole);
      });

      it('should add only unique PartyRole to an array', () => {
        const partyRoleArray: IPartyRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partyRoleCollection: IPartyRole[] = [sampleWithRequiredData];
        expectedResult = service.addPartyRoleToCollectionIfMissing(partyRoleCollection, ...partyRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partyRole: IPartyRole = sampleWithRequiredData;
        const partyRole2: IPartyRole = sampleWithPartialData;
        expectedResult = service.addPartyRoleToCollectionIfMissing([], partyRole, partyRole2);
        expect(expectedResult).toEqual([partyRole, partyRole2]);
      });

      it('should accept null and undefined values', () => {
        const partyRole: IPartyRole = sampleWithRequiredData;
        expectedResult = service.addPartyRoleToCollectionIfMissing([], null, partyRole, undefined);
        expect(expectedResult).toEqual([partyRole]);
      });

      it('should return initial array if no PartyRole is added', () => {
        const partyRoleCollection: IPartyRole[] = [sampleWithRequiredData];
        expectedResult = service.addPartyRoleToCollectionIfMissing(partyRoleCollection, undefined, null);
        expect(expectedResult).toEqual(partyRoleCollection);
      });
    });

    describe('comparePartyRole', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePartyRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18921 };
        const entity2 = null;

        const compareResult1 = service.comparePartyRole(entity1, entity2);
        const compareResult2 = service.comparePartyRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18921 };
        const entity2 = { id: 29421 };

        const compareResult1 = service.comparePartyRole(entity1, entity2);
        const compareResult2 = service.comparePartyRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18921 };
        const entity2 = { id: 18921 };

        const compareResult1 = service.comparePartyRole(entity1, entity2);
        const compareResult2 = service.comparePartyRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
