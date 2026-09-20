import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IInvoiceItemTypeMap } from '../invoice-item-type-map.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../invoice-item-type-map.test-samples';

import { InvoiceItemTypeMapService } from './invoice-item-type-map.service';

const requireRestSample: IInvoiceItemTypeMap = {
  ...sampleWithRequiredData,
};

describe('InvoiceItemTypeMap Service', () => {
  let service: InvoiceItemTypeMapService;
  let httpMock: HttpTestingController;
  let expectedResult: IInvoiceItemTypeMap | IInvoiceItemTypeMap[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceItemTypeMapService);
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

    it('should create a InvoiceItemTypeMap', () => {
      const invoiceItemTypeMap = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(invoiceItemTypeMap).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceItemTypeMap', () => {
      const invoiceItemTypeMap = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(invoiceItemTypeMap).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceItemTypeMap', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceItemTypeMap', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InvoiceItemTypeMap', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInvoiceItemTypeMapToCollectionIfMissing', () => {
      it('should add a InvoiceItemTypeMap to an empty array', () => {
        const invoiceItemTypeMap: IInvoiceItemTypeMap = sampleWithRequiredData;
        expectedResult = service.addInvoiceItemTypeMapToCollectionIfMissing([], invoiceItemTypeMap);
        expect(expectedResult).toEqual([invoiceItemTypeMap]);
      });

      it('should not add a InvoiceItemTypeMap to an array that contains it', () => {
        const invoiceItemTypeMap: IInvoiceItemTypeMap = sampleWithRequiredData;
        const invoiceItemTypeMapCollection: IInvoiceItemTypeMap[] = [
          {
            ...invoiceItemTypeMap,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInvoiceItemTypeMapToCollectionIfMissing(invoiceItemTypeMapCollection, invoiceItemTypeMap);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceItemTypeMap to an array that doesn't contain it", () => {
        const invoiceItemTypeMap: IInvoiceItemTypeMap = sampleWithRequiredData;
        const invoiceItemTypeMapCollection: IInvoiceItemTypeMap[] = [sampleWithPartialData];
        expectedResult = service.addInvoiceItemTypeMapToCollectionIfMissing(invoiceItemTypeMapCollection, invoiceItemTypeMap);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceItemTypeMap);
      });

      it('should add only unique InvoiceItemTypeMap to an array', () => {
        const invoiceItemTypeMapArray: IInvoiceItemTypeMap[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const invoiceItemTypeMapCollection: IInvoiceItemTypeMap[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceItemTypeMapToCollectionIfMissing(invoiceItemTypeMapCollection, ...invoiceItemTypeMapArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceItemTypeMap: IInvoiceItemTypeMap = sampleWithRequiredData;
        const invoiceItemTypeMap2: IInvoiceItemTypeMap = sampleWithPartialData;
        expectedResult = service.addInvoiceItemTypeMapToCollectionIfMissing([], invoiceItemTypeMap, invoiceItemTypeMap2);
        expect(expectedResult).toEqual([invoiceItemTypeMap, invoiceItemTypeMap2]);
      });

      it('should accept null and undefined values', () => {
        const invoiceItemTypeMap: IInvoiceItemTypeMap = sampleWithRequiredData;
        expectedResult = service.addInvoiceItemTypeMapToCollectionIfMissing([], null, invoiceItemTypeMap, undefined);
        expect(expectedResult).toEqual([invoiceItemTypeMap]);
      });

      it('should return initial array if no InvoiceItemTypeMap is added', () => {
        const invoiceItemTypeMapCollection: IInvoiceItemTypeMap[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceItemTypeMapToCollectionIfMissing(invoiceItemTypeMapCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceItemTypeMapCollection);
      });
    });

    describe('compareInvoiceItemTypeMap', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInvoiceItemTypeMap(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18575 };
        const entity2 = null;

        const compareResult1 = service.compareInvoiceItemTypeMap(entity1, entity2);
        const compareResult2 = service.compareInvoiceItemTypeMap(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18575 };
        const entity2 = { id: 10124 };

        const compareResult1 = service.compareInvoiceItemTypeMap(entity1, entity2);
        const compareResult2 = service.compareInvoiceItemTypeMap(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18575 };
        const entity2 = { id: 18575 };

        const compareResult1 = service.compareInvoiceItemTypeMap(entity1, entity2);
        const compareResult2 = service.compareInvoiceItemTypeMap(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
