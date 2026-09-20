import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacilityGroupMember } from '../facility-group-member.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../facility-group-member.test-samples';

import { FacilityGroupMemberService, RestFacilityGroupMember } from './facility-group-member.service';

const requireRestSample: RestFacilityGroupMember = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('FacilityGroupMember Service', () => {
  let service: FacilityGroupMemberService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacilityGroupMember | IFacilityGroupMember[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityGroupMemberService);
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

    it('should create a FacilityGroupMember', () => {
      const facilityGroupMember = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facilityGroupMember).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacilityGroupMember', () => {
      const facilityGroupMember = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facilityGroupMember).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacilityGroupMember', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacilityGroupMember', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacilityGroupMember', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityGroupMemberToCollectionIfMissing', () => {
      it('should add a FacilityGroupMember to an empty array', () => {
        const facilityGroupMember: IFacilityGroupMember = sampleWithRequiredData;
        expectedResult = service.addFacilityGroupMemberToCollectionIfMissing([], facilityGroupMember);
        expect(expectedResult).toEqual([facilityGroupMember]);
      });

      it('should not add a FacilityGroupMember to an array that contains it', () => {
        const facilityGroupMember: IFacilityGroupMember = sampleWithRequiredData;
        const facilityGroupMemberCollection: IFacilityGroupMember[] = [
          {
            ...facilityGroupMember,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityGroupMemberToCollectionIfMissing(facilityGroupMemberCollection, facilityGroupMember);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacilityGroupMember to an array that doesn't contain it", () => {
        const facilityGroupMember: IFacilityGroupMember = sampleWithRequiredData;
        const facilityGroupMemberCollection: IFacilityGroupMember[] = [sampleWithPartialData];
        expectedResult = service.addFacilityGroupMemberToCollectionIfMissing(facilityGroupMemberCollection, facilityGroupMember);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facilityGroupMember);
      });

      it('should add only unique FacilityGroupMember to an array', () => {
        const facilityGroupMemberArray: IFacilityGroupMember[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facilityGroupMemberCollection: IFacilityGroupMember[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityGroupMemberToCollectionIfMissing(facilityGroupMemberCollection, ...facilityGroupMemberArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facilityGroupMember: IFacilityGroupMember = sampleWithRequiredData;
        const facilityGroupMember2: IFacilityGroupMember = sampleWithPartialData;
        expectedResult = service.addFacilityGroupMemberToCollectionIfMissing([], facilityGroupMember, facilityGroupMember2);
        expect(expectedResult).toEqual([facilityGroupMember, facilityGroupMember2]);
      });

      it('should accept null and undefined values', () => {
        const facilityGroupMember: IFacilityGroupMember = sampleWithRequiredData;
        expectedResult = service.addFacilityGroupMemberToCollectionIfMissing([], null, facilityGroupMember, undefined);
        expect(expectedResult).toEqual([facilityGroupMember]);
      });

      it('should return initial array if no FacilityGroupMember is added', () => {
        const facilityGroupMemberCollection: IFacilityGroupMember[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityGroupMemberToCollectionIfMissing(facilityGroupMemberCollection, undefined, null);
        expect(expectedResult).toEqual(facilityGroupMemberCollection);
      });
    });

    describe('compareFacilityGroupMember', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacilityGroupMember(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26141 };
        const entity2 = null;

        const compareResult1 = service.compareFacilityGroupMember(entity1, entity2);
        const compareResult2 = service.compareFacilityGroupMember(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26141 };
        const entity2 = { id: 16206 };

        const compareResult1 = service.compareFacilityGroupMember(entity1, entity2);
        const compareResult2 = service.compareFacilityGroupMember(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 26141 };
        const entity2 = { id: 26141 };

        const compareResult1 = service.compareFacilityGroupMember(entity1, entity2);
        const compareResult2 = service.compareFacilityGroupMember(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
