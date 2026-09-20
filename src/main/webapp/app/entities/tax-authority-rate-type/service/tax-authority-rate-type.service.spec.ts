import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITaxAuthorityRateType } from '../tax-authority-rate-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../tax-authority-rate-type.test-samples';

import { TaxAuthorityRateTypeService } from './tax-authority-rate-type.service';

const requireRestSample: ITaxAuthorityRateType = {
  ...sampleWithRequiredData,
};

describe('TaxAuthorityRateType Service', () => {
  let service: TaxAuthorityRateTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ITaxAuthorityRateType | ITaxAuthorityRateType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TaxAuthorityRateTypeService);
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

    it('should create a TaxAuthorityRateType', () => {
      const taxAuthorityRateType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(taxAuthorityRateType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TaxAuthorityRateType', () => {
      const taxAuthorityRateType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(taxAuthorityRateType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TaxAuthorityRateType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TaxAuthorityRateType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TaxAuthorityRateType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTaxAuthorityRateTypeToCollectionIfMissing', () => {
      it('should add a TaxAuthorityRateType to an empty array', () => {
        const taxAuthorityRateType: ITaxAuthorityRateType = sampleWithRequiredData;
        expectedResult = service.addTaxAuthorityRateTypeToCollectionIfMissing([], taxAuthorityRateType);
        expect(expectedResult).toEqual([taxAuthorityRateType]);
      });

      it('should not add a TaxAuthorityRateType to an array that contains it', () => {
        const taxAuthorityRateType: ITaxAuthorityRateType = sampleWithRequiredData;
        const taxAuthorityRateTypeCollection: ITaxAuthorityRateType[] = [
          {
            ...taxAuthorityRateType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTaxAuthorityRateTypeToCollectionIfMissing(taxAuthorityRateTypeCollection, taxAuthorityRateType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TaxAuthorityRateType to an array that doesn't contain it", () => {
        const taxAuthorityRateType: ITaxAuthorityRateType = sampleWithRequiredData;
        const taxAuthorityRateTypeCollection: ITaxAuthorityRateType[] = [sampleWithPartialData];
        expectedResult = service.addTaxAuthorityRateTypeToCollectionIfMissing(taxAuthorityRateTypeCollection, taxAuthorityRateType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxAuthorityRateType);
      });

      it('should add only unique TaxAuthorityRateType to an array', () => {
        const taxAuthorityRateTypeArray: ITaxAuthorityRateType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const taxAuthorityRateTypeCollection: ITaxAuthorityRateType[] = [sampleWithRequiredData];
        expectedResult = service.addTaxAuthorityRateTypeToCollectionIfMissing(taxAuthorityRateTypeCollection, ...taxAuthorityRateTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taxAuthorityRateType: ITaxAuthorityRateType = sampleWithRequiredData;
        const taxAuthorityRateType2: ITaxAuthorityRateType = sampleWithPartialData;
        expectedResult = service.addTaxAuthorityRateTypeToCollectionIfMissing([], taxAuthorityRateType, taxAuthorityRateType2);
        expect(expectedResult).toEqual([taxAuthorityRateType, taxAuthorityRateType2]);
      });

      it('should accept null and undefined values', () => {
        const taxAuthorityRateType: ITaxAuthorityRateType = sampleWithRequiredData;
        expectedResult = service.addTaxAuthorityRateTypeToCollectionIfMissing([], null, taxAuthorityRateType, undefined);
        expect(expectedResult).toEqual([taxAuthorityRateType]);
      });

      it('should return initial array if no TaxAuthorityRateType is added', () => {
        const taxAuthorityRateTypeCollection: ITaxAuthorityRateType[] = [sampleWithRequiredData];
        expectedResult = service.addTaxAuthorityRateTypeToCollectionIfMissing(taxAuthorityRateTypeCollection, undefined, null);
        expect(expectedResult).toEqual(taxAuthorityRateTypeCollection);
      });
    });

    describe('compareTaxAuthorityRateType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTaxAuthorityRateType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11026 };
        const entity2 = null;

        const compareResult1 = service.compareTaxAuthorityRateType(entity1, entity2);
        const compareResult2 = service.compareTaxAuthorityRateType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11026 };
        const entity2 = { id: 3035 };

        const compareResult1 = service.compareTaxAuthorityRateType(entity1, entity2);
        const compareResult2 = service.compareTaxAuthorityRateType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 11026 };
        const entity2 = { id: 11026 };

        const compareResult1 = service.compareTaxAuthorityRateType(entity1, entity2);
        const compareResult2 = service.compareTaxAuthorityRateType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
