import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustRequestRole } from '../cust-request-role.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cust-request-role.test-samples';

import { CustRequestRoleService } from './cust-request-role.service';

const requireRestSample: ICustRequestRole = {
  ...sampleWithRequiredData,
};

describe('CustRequestRole Service', () => {
  let service: CustRequestRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustRequestRole | ICustRequestRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustRequestRoleService);
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

    it('should create a CustRequestRole', () => {
      const custRequestRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(custRequestRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustRequestRole', () => {
      const custRequestRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(custRequestRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustRequestRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustRequestRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustRequestRole', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustRequestRoleToCollectionIfMissing', () => {
      it('should add a CustRequestRole to an empty array', () => {
        const custRequestRole: ICustRequestRole = sampleWithRequiredData;
        expectedResult = service.addCustRequestRoleToCollectionIfMissing([], custRequestRole);
        expect(expectedResult).toEqual([custRequestRole]);
      });

      it('should not add a CustRequestRole to an array that contains it', () => {
        const custRequestRole: ICustRequestRole = sampleWithRequiredData;
        const custRequestRoleCollection: ICustRequestRole[] = [
          {
            ...custRequestRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustRequestRoleToCollectionIfMissing(custRequestRoleCollection, custRequestRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustRequestRole to an array that doesn't contain it", () => {
        const custRequestRole: ICustRequestRole = sampleWithRequiredData;
        const custRequestRoleCollection: ICustRequestRole[] = [sampleWithPartialData];
        expectedResult = service.addCustRequestRoleToCollectionIfMissing(custRequestRoleCollection, custRequestRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(custRequestRole);
      });

      it('should add only unique CustRequestRole to an array', () => {
        const custRequestRoleArray: ICustRequestRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const custRequestRoleCollection: ICustRequestRole[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestRoleToCollectionIfMissing(custRequestRoleCollection, ...custRequestRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const custRequestRole: ICustRequestRole = sampleWithRequiredData;
        const custRequestRole2: ICustRequestRole = sampleWithPartialData;
        expectedResult = service.addCustRequestRoleToCollectionIfMissing([], custRequestRole, custRequestRole2);
        expect(expectedResult).toEqual([custRequestRole, custRequestRole2]);
      });

      it('should accept null and undefined values', () => {
        const custRequestRole: ICustRequestRole = sampleWithRequiredData;
        expectedResult = service.addCustRequestRoleToCollectionIfMissing([], null, custRequestRole, undefined);
        expect(expectedResult).toEqual([custRequestRole]);
      });

      it('should return initial array if no CustRequestRole is added', () => {
        const custRequestRoleCollection: ICustRequestRole[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestRoleToCollectionIfMissing(custRequestRoleCollection, undefined, null);
        expect(expectedResult).toEqual(custRequestRoleCollection);
      });
    });

    describe('compareCustRequestRole', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustRequestRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26912 };
        const entity2 = null;

        const compareResult1 = service.compareCustRequestRole(entity1, entity2);
        const compareResult2 = service.compareCustRequestRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26912 };
        const entity2 = { id: 16150 };

        const compareResult1 = service.compareCustRequestRole(entity1, entity2);
        const compareResult2 = service.compareCustRequestRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 26912 };
        const entity2 = { id: 26912 };

        const compareResult1 = service.compareCustRequestRole(entity1, entity2);
        const compareResult2 = service.compareCustRequestRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
