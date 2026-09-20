import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInvoiceType } from '../invoice-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../invoice-type.test-samples';

import { InvoiceTypeService } from './invoice-type.service';

const requireRestSample: IInvoiceType = {
  ...sampleWithRequiredData,
};

describe('InvoiceType Service', () => {
  let service: InvoiceTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IInvoiceType | IInvoiceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceTypeService);
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

    it('should create a InvoiceType', () => {
      const invoiceType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(invoiceType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceType', () => {
      const invoiceType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(invoiceType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InvoiceType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInvoiceTypeToCollectionIfMissing', () => {
      it('should add a InvoiceType to an empty array', () => {
        const invoiceType: IInvoiceType = sampleWithRequiredData;
        expectedResult = service.addInvoiceTypeToCollectionIfMissing([], invoiceType);
        expect(expectedResult).toEqual([invoiceType]);
      });

      it('should not add a InvoiceType to an array that contains it', () => {
        const invoiceType: IInvoiceType = sampleWithRequiredData;
        const invoiceTypeCollection: IInvoiceType[] = [
          {
            ...invoiceType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInvoiceTypeToCollectionIfMissing(invoiceTypeCollection, invoiceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceType to an array that doesn't contain it", () => {
        const invoiceType: IInvoiceType = sampleWithRequiredData;
        const invoiceTypeCollection: IInvoiceType[] = [sampleWithPartialData];
        expectedResult = service.addInvoiceTypeToCollectionIfMissing(invoiceTypeCollection, invoiceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceType);
      });

      it('should add only unique InvoiceType to an array', () => {
        const invoiceTypeArray: IInvoiceType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const invoiceTypeCollection: IInvoiceType[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceTypeToCollectionIfMissing(invoiceTypeCollection, ...invoiceTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceType: IInvoiceType = sampleWithRequiredData;
        const invoiceType2: IInvoiceType = sampleWithPartialData;
        expectedResult = service.addInvoiceTypeToCollectionIfMissing([], invoiceType, invoiceType2);
        expect(expectedResult).toEqual([invoiceType, invoiceType2]);
      });

      it('should accept null and undefined values', () => {
        const invoiceType: IInvoiceType = sampleWithRequiredData;
        expectedResult = service.addInvoiceTypeToCollectionIfMissing([], null, invoiceType, undefined);
        expect(expectedResult).toEqual([invoiceType]);
      });

      it('should return initial array if no InvoiceType is added', () => {
        const invoiceTypeCollection: IInvoiceType[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceTypeToCollectionIfMissing(invoiceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceTypeCollection);
      });
    });

    describe('compareInvoiceType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInvoiceType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15913 };
        const entity2 = null;

        const compareResult1 = service.compareInvoiceType(entity1, entity2);
        const compareResult2 = service.compareInvoiceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15913 };
        const entity2 = { id: 3910 };

        const compareResult1 = service.compareInvoiceType(entity1, entity2);
        const compareResult2 = service.compareInvoiceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 15913 };
        const entity2 = { id: 15913 };

        const compareResult1 = service.compareInvoiceType(entity1, entity2);
        const compareResult2 = service.compareInvoiceType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
