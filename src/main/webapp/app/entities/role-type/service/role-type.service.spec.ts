import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IRoleType } from '../role-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../role-type.test-samples';

import { RoleTypeService } from './role-type.service';

const requireRestSample: IRoleType = {
  ...sampleWithRequiredData,
};

describe('RoleType Service', () => {
  let service: RoleTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IRoleType | IRoleType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RoleTypeService);
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

    it('should create a RoleType', () => {
      const roleType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(roleType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RoleType', () => {
      const roleType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(roleType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RoleType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RoleType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RoleType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addRoleTypeToCollectionIfMissing', () => {
      it('should add a RoleType to an empty array', () => {
        const roleType: IRoleType = sampleWithRequiredData;
        expectedResult = service.addRoleTypeToCollectionIfMissing([], roleType);
        expect(expectedResult).toEqual([roleType]);
      });

      it('should not add a RoleType to an array that contains it', () => {
        const roleType: IRoleType = sampleWithRequiredData;
        const roleTypeCollection: IRoleType[] = [
          {
            ...roleType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRoleTypeToCollectionIfMissing(roleTypeCollection, roleType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RoleType to an array that doesn't contain it", () => {
        const roleType: IRoleType = sampleWithRequiredData;
        const roleTypeCollection: IRoleType[] = [sampleWithPartialData];
        expectedResult = service.addRoleTypeToCollectionIfMissing(roleTypeCollection, roleType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roleType);
      });

      it('should add only unique RoleType to an array', () => {
        const roleTypeArray: IRoleType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const roleTypeCollection: IRoleType[] = [sampleWithRequiredData];
        expectedResult = service.addRoleTypeToCollectionIfMissing(roleTypeCollection, ...roleTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roleType: IRoleType = sampleWithRequiredData;
        const roleType2: IRoleType = sampleWithPartialData;
        expectedResult = service.addRoleTypeToCollectionIfMissing([], roleType, roleType2);
        expect(expectedResult).toEqual([roleType, roleType2]);
      });

      it('should accept null and undefined values', () => {
        const roleType: IRoleType = sampleWithRequiredData;
        expectedResult = service.addRoleTypeToCollectionIfMissing([], null, roleType, undefined);
        expect(expectedResult).toEqual([roleType]);
      });

      it('should return initial array if no RoleType is added', () => {
        const roleTypeCollection: IRoleType[] = [sampleWithRequiredData];
        expectedResult = service.addRoleTypeToCollectionIfMissing(roleTypeCollection, undefined, null);
        expect(expectedResult).toEqual(roleTypeCollection);
      });
    });

    describe('compareRoleType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRoleType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17576 };
        const entity2 = null;

        const compareResult1 = service.compareRoleType(entity1, entity2);
        const compareResult2 = service.compareRoleType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17576 };
        const entity2 = { id: 27052 };

        const compareResult1 = service.compareRoleType(entity1, entity2);
        const compareResult2 = service.compareRoleType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 17576 };
        const entity2 = { id: 17576 };

        const compareResult1 = service.compareRoleType(entity1, entity2);
        const compareResult2 = service.compareRoleType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
