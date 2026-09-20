import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IQuoteRole } from '../quote-role.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../quote-role.test-samples';

import { QuoteRoleService } from './quote-role.service';

const requireRestSample: IQuoteRole = {
  ...sampleWithRequiredData,
};

describe('QuoteRole Service', () => {
  let service: QuoteRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuoteRole | IQuoteRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuoteRoleService);
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

    it('should create a QuoteRole', () => {
      const quoteRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quoteRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuoteRole', () => {
      const quoteRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quoteRole).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuoteRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuoteRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuoteRole', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addQuoteRoleToCollectionIfMissing', () => {
      it('should add a QuoteRole to an empty array', () => {
        const quoteRole: IQuoteRole = sampleWithRequiredData;
        expectedResult = service.addQuoteRoleToCollectionIfMissing([], quoteRole);
        expect(expectedResult).toEqual([quoteRole]);
      });

      it('should not add a QuoteRole to an array that contains it', () => {
        const quoteRole: IQuoteRole = sampleWithRequiredData;
        const quoteRoleCollection: IQuoteRole[] = [
          {
            ...quoteRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuoteRoleToCollectionIfMissing(quoteRoleCollection, quoteRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuoteRole to an array that doesn't contain it", () => {
        const quoteRole: IQuoteRole = sampleWithRequiredData;
        const quoteRoleCollection: IQuoteRole[] = [sampleWithPartialData];
        expectedResult = service.addQuoteRoleToCollectionIfMissing(quoteRoleCollection, quoteRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quoteRole);
      });

      it('should add only unique QuoteRole to an array', () => {
        const quoteRoleArray: IQuoteRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quoteRoleCollection: IQuoteRole[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteRoleToCollectionIfMissing(quoteRoleCollection, ...quoteRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quoteRole: IQuoteRole = sampleWithRequiredData;
        const quoteRole2: IQuoteRole = sampleWithPartialData;
        expectedResult = service.addQuoteRoleToCollectionIfMissing([], quoteRole, quoteRole2);
        expect(expectedResult).toEqual([quoteRole, quoteRole2]);
      });

      it('should accept null and undefined values', () => {
        const quoteRole: IQuoteRole = sampleWithRequiredData;
        expectedResult = service.addQuoteRoleToCollectionIfMissing([], null, quoteRole, undefined);
        expect(expectedResult).toEqual([quoteRole]);
      });

      it('should return initial array if no QuoteRole is added', () => {
        const quoteRoleCollection: IQuoteRole[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteRoleToCollectionIfMissing(quoteRoleCollection, undefined, null);
        expect(expectedResult).toEqual(quoteRoleCollection);
      });
    });

    describe('compareQuoteRole', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuoteRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4674 };
        const entity2 = null;

        const compareResult1 = service.compareQuoteRole(entity1, entity2);
        const compareResult2 = service.compareQuoteRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4674 };
        const entity2 = { id: 133 };

        const compareResult1 = service.compareQuoteRole(entity1, entity2);
        const compareResult2 = service.compareQuoteRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4674 };
        const entity2 = { id: 4674 };

        const compareResult1 = service.compareQuoteRole(entity1, entity2);
        const compareResult2 = service.compareQuoteRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
