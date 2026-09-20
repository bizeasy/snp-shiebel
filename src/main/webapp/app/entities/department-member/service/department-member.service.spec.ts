import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDepartmentMember } from '../department-member.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../department-member.test-samples';

import { DepartmentMemberService, RestDepartmentMember } from './department-member.service';

const requireRestSample: RestDepartmentMember = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('DepartmentMember Service', () => {
  let service: DepartmentMemberService;
  let httpMock: HttpTestingController;
  let expectedResult: IDepartmentMember | IDepartmentMember[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DepartmentMemberService);
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

    it('should create a DepartmentMember', () => {
      const departmentMember = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(departmentMember).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DepartmentMember', () => {
      const departmentMember = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(departmentMember).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DepartmentMember', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DepartmentMember', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DepartmentMember', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addDepartmentMemberToCollectionIfMissing', () => {
      it('should add a DepartmentMember to an empty array', () => {
        const departmentMember: IDepartmentMember = sampleWithRequiredData;
        expectedResult = service.addDepartmentMemberToCollectionIfMissing([], departmentMember);
        expect(expectedResult).toEqual([departmentMember]);
      });

      it('should not add a DepartmentMember to an array that contains it', () => {
        const departmentMember: IDepartmentMember = sampleWithRequiredData;
        const departmentMemberCollection: IDepartmentMember[] = [
          {
            ...departmentMember,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDepartmentMemberToCollectionIfMissing(departmentMemberCollection, departmentMember);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DepartmentMember to an array that doesn't contain it", () => {
        const departmentMember: IDepartmentMember = sampleWithRequiredData;
        const departmentMemberCollection: IDepartmentMember[] = [sampleWithPartialData];
        expectedResult = service.addDepartmentMemberToCollectionIfMissing(departmentMemberCollection, departmentMember);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departmentMember);
      });

      it('should add only unique DepartmentMember to an array', () => {
        const departmentMemberArray: IDepartmentMember[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const departmentMemberCollection: IDepartmentMember[] = [sampleWithRequiredData];
        expectedResult = service.addDepartmentMemberToCollectionIfMissing(departmentMemberCollection, ...departmentMemberArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const departmentMember: IDepartmentMember = sampleWithRequiredData;
        const departmentMember2: IDepartmentMember = sampleWithPartialData;
        expectedResult = service.addDepartmentMemberToCollectionIfMissing([], departmentMember, departmentMember2);
        expect(expectedResult).toEqual([departmentMember, departmentMember2]);
      });

      it('should accept null and undefined values', () => {
        const departmentMember: IDepartmentMember = sampleWithRequiredData;
        expectedResult = service.addDepartmentMemberToCollectionIfMissing([], null, departmentMember, undefined);
        expect(expectedResult).toEqual([departmentMember]);
      });

      it('should return initial array if no DepartmentMember is added', () => {
        const departmentMemberCollection: IDepartmentMember[] = [sampleWithRequiredData];
        expectedResult = service.addDepartmentMemberToCollectionIfMissing(departmentMemberCollection, undefined, null);
        expect(expectedResult).toEqual(departmentMemberCollection);
      });
    });

    describe('compareDepartmentMember', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDepartmentMember(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4074 };
        const entity2 = null;

        const compareResult1 = service.compareDepartmentMember(entity1, entity2);
        const compareResult2 = service.compareDepartmentMember(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4074 };
        const entity2 = { id: 6807 };

        const compareResult1 = service.compareDepartmentMember(entity1, entity2);
        const compareResult2 = service.compareDepartmentMember(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4074 };
        const entity2 = { id: 4074 };

        const compareResult1 = service.compareDepartmentMember(entity1, entity2);
        const compareResult2 = service.compareDepartmentMember(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
