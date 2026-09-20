import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInvoiceRole } from '../invoice-role.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../invoice-role.test-samples';

import { InvoiceRoleService } from './invoice-role.service';

const requireRestSample: IInvoiceRole = {
  ...sampleWithRequiredData,
};

describe('InvoiceRole Service', () => {
  let service: InvoiceRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IInvoiceRole | IInvoiceRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceRoleService);
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

    it('should create a InvoiceRole', () => {
      const invoiceRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(invoiceRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceRole', () => {
      const invoiceRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(invoiceRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InvoiceRole', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInvoiceRoleToCollectionIfMissing', () => {
      it('should add a InvoiceRole to an empty array', () => {
        const invoiceRole: IInvoiceRole = sampleWithRequiredData;
        expectedResult = service.addInvoiceRoleToCollectionIfMissing([], invoiceRole);
        expect(expectedResult).toEqual([invoiceRole]);
      });

      it('should not add a InvoiceRole to an array that contains it', () => {
        const invoiceRole: IInvoiceRole = sampleWithRequiredData;
        const invoiceRoleCollection: IInvoiceRole[] = [
          {
            ...invoiceRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInvoiceRoleToCollectionIfMissing(invoiceRoleCollection, invoiceRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceRole to an array that doesn't contain it", () => {
        const invoiceRole: IInvoiceRole = sampleWithRequiredData;
        const invoiceRoleCollection: IInvoiceRole[] = [sampleWithPartialData];
        expectedResult = service.addInvoiceRoleToCollectionIfMissing(invoiceRoleCollection, invoiceRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceRole);
      });

      it('should add only unique InvoiceRole to an array', () => {
        const invoiceRoleArray: IInvoiceRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const invoiceRoleCollection: IInvoiceRole[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceRoleToCollectionIfMissing(invoiceRoleCollection, ...invoiceRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceRole: IInvoiceRole = sampleWithRequiredData;
        const invoiceRole2: IInvoiceRole = sampleWithPartialData;
        expectedResult = service.addInvoiceRoleToCollectionIfMissing([], invoiceRole, invoiceRole2);
        expect(expectedResult).toEqual([invoiceRole, invoiceRole2]);
      });

      it('should accept null and undefined values', () => {
        const invoiceRole: IInvoiceRole = sampleWithRequiredData;
        expectedResult = service.addInvoiceRoleToCollectionIfMissing([], null, invoiceRole, undefined);
        expect(expectedResult).toEqual([invoiceRole]);
      });

      it('should return initial array if no InvoiceRole is added', () => {
        const invoiceRoleCollection: IInvoiceRole[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceRoleToCollectionIfMissing(invoiceRoleCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceRoleCollection);
      });
    });

    describe('compareInvoiceRole', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInvoiceRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2077 };
        const entity2 = null;

        const compareResult1 = service.compareInvoiceRole(entity1, entity2);
        const compareResult2 = service.compareInvoiceRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2077 };
        const entity2 = { id: 9582 };

        const compareResult1 = service.compareInvoiceRole(entity1, entity2);
        const compareResult2 = service.compareInvoiceRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 2077 };
        const entity2 = { id: 2077 };

        const compareResult1 = service.compareInvoiceRole(entity1, entity2);
        const compareResult2 = service.compareInvoiceRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
