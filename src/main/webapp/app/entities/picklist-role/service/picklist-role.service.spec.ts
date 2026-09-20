import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPicklistRole } from '../picklist-role.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../picklist-role.test-samples';

import { PicklistRoleService } from './picklist-role.service';

const requireRestSample: IPicklistRole = {
  ...sampleWithRequiredData,
};

describe('PicklistRole Service', () => {
  let service: PicklistRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IPicklistRole | IPicklistRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PicklistRoleService);
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

    it('should create a PicklistRole', () => {
      const picklistRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(picklistRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PicklistRole', () => {
      const picklistRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(picklistRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PicklistRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PicklistRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PicklistRole', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPicklistRoleToCollectionIfMissing', () => {
      it('should add a PicklistRole to an empty array', () => {
        const picklistRole: IPicklistRole = sampleWithRequiredData;
        expectedResult = service.addPicklistRoleToCollectionIfMissing([], picklistRole);
        expect(expectedResult).toEqual([picklistRole]);
      });

      it('should not add a PicklistRole to an array that contains it', () => {
        const picklistRole: IPicklistRole = sampleWithRequiredData;
        const picklistRoleCollection: IPicklistRole[] = [
          {
            ...picklistRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPicklistRoleToCollectionIfMissing(picklistRoleCollection, picklistRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PicklistRole to an array that doesn't contain it", () => {
        const picklistRole: IPicklistRole = sampleWithRequiredData;
        const picklistRoleCollection: IPicklistRole[] = [sampleWithPartialData];
        expectedResult = service.addPicklistRoleToCollectionIfMissing(picklistRoleCollection, picklistRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(picklistRole);
      });

      it('should add only unique PicklistRole to an array', () => {
        const picklistRoleArray: IPicklistRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const picklistRoleCollection: IPicklistRole[] = [sampleWithRequiredData];
        expectedResult = service.addPicklistRoleToCollectionIfMissing(picklistRoleCollection, ...picklistRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const picklistRole: IPicklistRole = sampleWithRequiredData;
        const picklistRole2: IPicklistRole = sampleWithPartialData;
        expectedResult = service.addPicklistRoleToCollectionIfMissing([], picklistRole, picklistRole2);
        expect(expectedResult).toEqual([picklistRole, picklistRole2]);
      });

      it('should accept null and undefined values', () => {
        const picklistRole: IPicklistRole = sampleWithRequiredData;
        expectedResult = service.addPicklistRoleToCollectionIfMissing([], null, picklistRole, undefined);
        expect(expectedResult).toEqual([picklistRole]);
      });

      it('should return initial array if no PicklistRole is added', () => {
        const picklistRoleCollection: IPicklistRole[] = [sampleWithRequiredData];
        expectedResult = service.addPicklistRoleToCollectionIfMissing(picklistRoleCollection, undefined, null);
        expect(expectedResult).toEqual(picklistRoleCollection);
      });
    });

    describe('comparePicklistRole', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePicklistRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6650 };
        const entity2 = null;

        const compareResult1 = service.comparePicklistRole(entity1, entity2);
        const compareResult2 = service.comparePicklistRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6650 };
        const entity2 = { id: 28969 };

        const compareResult1 = service.comparePicklistRole(entity1, entity2);
        const compareResult2 = service.comparePicklistRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6650 };
        const entity2 = { id: 6650 };

        const compareResult1 = service.comparePicklistRole(entity1, entity2);
        const compareResult2 = service.comparePicklistRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
