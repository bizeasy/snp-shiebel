import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITaxSlab } from '../tax-slab.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tax-slab.test-samples';

import { TaxSlabService } from './tax-slab.service';

const requireRestSample: ITaxSlab = {
  ...sampleWithRequiredData,
};

describe('TaxSlab Service', () => {
  let service: TaxSlabService;
  let httpMock: HttpTestingController;
  let expectedResult: ITaxSlab | ITaxSlab[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TaxSlabService);
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

    it('should create a TaxSlab', () => {
      const taxSlab = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(taxSlab).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TaxSlab', () => {
      const taxSlab = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(taxSlab).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TaxSlab', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TaxSlab', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TaxSlab', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTaxSlabToCollectionIfMissing', () => {
      it('should add a TaxSlab to an empty array', () => {
        const taxSlab: ITaxSlab = sampleWithRequiredData;
        expectedResult = service.addTaxSlabToCollectionIfMissing([], taxSlab);
        expect(expectedResult).toEqual([taxSlab]);
      });

      it('should not add a TaxSlab to an array that contains it', () => {
        const taxSlab: ITaxSlab = sampleWithRequiredData;
        const taxSlabCollection: ITaxSlab[] = [
          {
            ...taxSlab,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTaxSlabToCollectionIfMissing(taxSlabCollection, taxSlab);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TaxSlab to an array that doesn't contain it", () => {
        const taxSlab: ITaxSlab = sampleWithRequiredData;
        const taxSlabCollection: ITaxSlab[] = [sampleWithPartialData];
        expectedResult = service.addTaxSlabToCollectionIfMissing(taxSlabCollection, taxSlab);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxSlab);
      });

      it('should add only unique TaxSlab to an array', () => {
        const taxSlabArray: ITaxSlab[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const taxSlabCollection: ITaxSlab[] = [sampleWithRequiredData];
        expectedResult = service.addTaxSlabToCollectionIfMissing(taxSlabCollection, ...taxSlabArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taxSlab: ITaxSlab = sampleWithRequiredData;
        const taxSlab2: ITaxSlab = sampleWithPartialData;
        expectedResult = service.addTaxSlabToCollectionIfMissing([], taxSlab, taxSlab2);
        expect(expectedResult).toEqual([taxSlab, taxSlab2]);
      });

      it('should accept null and undefined values', () => {
        const taxSlab: ITaxSlab = sampleWithRequiredData;
        expectedResult = service.addTaxSlabToCollectionIfMissing([], null, taxSlab, undefined);
        expect(expectedResult).toEqual([taxSlab]);
      });

      it('should return initial array if no TaxSlab is added', () => {
        const taxSlabCollection: ITaxSlab[] = [sampleWithRequiredData];
        expectedResult = service.addTaxSlabToCollectionIfMissing(taxSlabCollection, undefined, null);
        expect(expectedResult).toEqual(taxSlabCollection);
      });
    });

    describe('compareTaxSlab', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTaxSlab(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22491 };
        const entity2 = null;

        const compareResult1 = service.compareTaxSlab(entity1, entity2);
        const compareResult2 = service.compareTaxSlab(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22491 };
        const entity2 = { id: 24819 };

        const compareResult1 = service.compareTaxSlab(entity1, entity2);
        const compareResult2 = service.compareTaxSlab(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 22491 };
        const entity2 = { id: 22491 };

        const compareResult1 = service.compareTaxSlab(entity1, entity2);
        const compareResult2 = service.compareTaxSlab(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
