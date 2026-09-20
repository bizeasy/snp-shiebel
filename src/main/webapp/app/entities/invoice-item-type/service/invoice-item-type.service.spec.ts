import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInvoiceItemType } from '../invoice-item-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../invoice-item-type.test-samples';

import { InvoiceItemTypeService } from './invoice-item-type.service';

const requireRestSample: IInvoiceItemType = {
  ...sampleWithRequiredData,
};

describe('InvoiceItemType Service', () => {
  let service: InvoiceItemTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IInvoiceItemType | IInvoiceItemType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceItemTypeService);
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

    it('should create a InvoiceItemType', () => {
      const invoiceItemType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(invoiceItemType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceItemType', () => {
      const invoiceItemType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(invoiceItemType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceItemType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceItemType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InvoiceItemType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInvoiceItemTypeToCollectionIfMissing', () => {
      it('should add a InvoiceItemType to an empty array', () => {
        const invoiceItemType: IInvoiceItemType = sampleWithRequiredData;
        expectedResult = service.addInvoiceItemTypeToCollectionIfMissing([], invoiceItemType);
        expect(expectedResult).toEqual([invoiceItemType]);
      });

      it('should not add a InvoiceItemType to an array that contains it', () => {
        const invoiceItemType: IInvoiceItemType = sampleWithRequiredData;
        const invoiceItemTypeCollection: IInvoiceItemType[] = [
          {
            ...invoiceItemType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInvoiceItemTypeToCollectionIfMissing(invoiceItemTypeCollection, invoiceItemType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceItemType to an array that doesn't contain it", () => {
        const invoiceItemType: IInvoiceItemType = sampleWithRequiredData;
        const invoiceItemTypeCollection: IInvoiceItemType[] = [sampleWithPartialData];
        expectedResult = service.addInvoiceItemTypeToCollectionIfMissing(invoiceItemTypeCollection, invoiceItemType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceItemType);
      });

      it('should add only unique InvoiceItemType to an array', () => {
        const invoiceItemTypeArray: IInvoiceItemType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const invoiceItemTypeCollection: IInvoiceItemType[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceItemTypeToCollectionIfMissing(invoiceItemTypeCollection, ...invoiceItemTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceItemType: IInvoiceItemType = sampleWithRequiredData;
        const invoiceItemType2: IInvoiceItemType = sampleWithPartialData;
        expectedResult = service.addInvoiceItemTypeToCollectionIfMissing([], invoiceItemType, invoiceItemType2);
        expect(expectedResult).toEqual([invoiceItemType, invoiceItemType2]);
      });

      it('should accept null and undefined values', () => {
        const invoiceItemType: IInvoiceItemType = sampleWithRequiredData;
        expectedResult = service.addInvoiceItemTypeToCollectionIfMissing([], null, invoiceItemType, undefined);
        expect(expectedResult).toEqual([invoiceItemType]);
      });

      it('should return initial array if no InvoiceItemType is added', () => {
        const invoiceItemTypeCollection: IInvoiceItemType[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceItemTypeToCollectionIfMissing(invoiceItemTypeCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceItemTypeCollection);
      });
    });

    describe('compareInvoiceItemType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInvoiceItemType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32383 };
        const entity2 = null;

        const compareResult1 = service.compareInvoiceItemType(entity1, entity2);
        const compareResult2 = service.compareInvoiceItemType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32383 };
        const entity2 = { id: 25434 };

        const compareResult1 = service.compareInvoiceItemType(entity1, entity2);
        const compareResult2 = service.compareInvoiceItemType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 32383 };
        const entity2 = { id: 32383 };

        const compareResult1 = service.compareInvoiceItemType(entity1, entity2);
        const compareResult2 = service.compareInvoiceItemType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
