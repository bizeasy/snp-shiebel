import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITaxAuthority } from '../tax-authority.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tax-authority.test-samples';

import { TaxAuthorityService } from './tax-authority.service';

const requireRestSample: ITaxAuthority = {
  ...sampleWithRequiredData,
};

describe('TaxAuthority Service', () => {
  let service: TaxAuthorityService;
  let httpMock: HttpTestingController;
  let expectedResult: ITaxAuthority | ITaxAuthority[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TaxAuthorityService);
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

    it('should create a TaxAuthority', () => {
      const taxAuthority = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(taxAuthority).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TaxAuthority', () => {
      const taxAuthority = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(taxAuthority).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TaxAuthority', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TaxAuthority', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TaxAuthority', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTaxAuthorityToCollectionIfMissing', () => {
      it('should add a TaxAuthority to an empty array', () => {
        const taxAuthority: ITaxAuthority = sampleWithRequiredData;
        expectedResult = service.addTaxAuthorityToCollectionIfMissing([], taxAuthority);
        expect(expectedResult).toEqual([taxAuthority]);
      });

      it('should not add a TaxAuthority to an array that contains it', () => {
        const taxAuthority: ITaxAuthority = sampleWithRequiredData;
        const taxAuthorityCollection: ITaxAuthority[] = [
          {
            ...taxAuthority,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTaxAuthorityToCollectionIfMissing(taxAuthorityCollection, taxAuthority);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TaxAuthority to an array that doesn't contain it", () => {
        const taxAuthority: ITaxAuthority = sampleWithRequiredData;
        const taxAuthorityCollection: ITaxAuthority[] = [sampleWithPartialData];
        expectedResult = service.addTaxAuthorityToCollectionIfMissing(taxAuthorityCollection, taxAuthority);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(taxAuthority);
      });

      it('should add only unique TaxAuthority to an array', () => {
        const taxAuthorityArray: ITaxAuthority[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const taxAuthorityCollection: ITaxAuthority[] = [sampleWithRequiredData];
        expectedResult = service.addTaxAuthorityToCollectionIfMissing(taxAuthorityCollection, ...taxAuthorityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const taxAuthority: ITaxAuthority = sampleWithRequiredData;
        const taxAuthority2: ITaxAuthority = sampleWithPartialData;
        expectedResult = service.addTaxAuthorityToCollectionIfMissing([], taxAuthority, taxAuthority2);
        expect(expectedResult).toEqual([taxAuthority, taxAuthority2]);
      });

      it('should accept null and undefined values', () => {
        const taxAuthority: ITaxAuthority = sampleWithRequiredData;
        expectedResult = service.addTaxAuthorityToCollectionIfMissing([], null, taxAuthority, undefined);
        expect(expectedResult).toEqual([taxAuthority]);
      });

      it('should return initial array if no TaxAuthority is added', () => {
        const taxAuthorityCollection: ITaxAuthority[] = [sampleWithRequiredData];
        expectedResult = service.addTaxAuthorityToCollectionIfMissing(taxAuthorityCollection, undefined, null);
        expect(expectedResult).toEqual(taxAuthorityCollection);
      });
    });

    describe('compareTaxAuthority', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTaxAuthority(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14408 };
        const entity2 = null;

        const compareResult1 = service.compareTaxAuthority(entity1, entity2);
        const compareResult2 = service.compareTaxAuthority(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14408 };
        const entity2 = { id: 27648 };

        const compareResult1 = service.compareTaxAuthority(entity1, entity2);
        const compareResult2 = service.compareTaxAuthority(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 14408 };
        const entity2 = { id: 14408 };

        const compareResult1 = service.compareTaxAuthority(entity1, entity2);
        const compareResult2 = service.compareTaxAuthority(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
