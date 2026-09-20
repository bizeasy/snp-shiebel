import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IQuoteType } from '../quote-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../quote-type.test-samples';

import { QuoteTypeService } from './quote-type.service';

const requireRestSample: IQuoteType = {
  ...sampleWithRequiredData,
};

describe('QuoteType Service', () => {
  let service: QuoteTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuoteType | IQuoteType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuoteTypeService);
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

    it('should create a QuoteType', () => {
      const quoteType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quoteType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuoteType', () => {
      const quoteType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quoteType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuoteType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuoteType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuoteType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addQuoteTypeToCollectionIfMissing', () => {
      it('should add a QuoteType to an empty array', () => {
        const quoteType: IQuoteType = sampleWithRequiredData;
        expectedResult = service.addQuoteTypeToCollectionIfMissing([], quoteType);
        expect(expectedResult).toEqual([quoteType]);
      });

      it('should not add a QuoteType to an array that contains it', () => {
        const quoteType: IQuoteType = sampleWithRequiredData;
        const quoteTypeCollection: IQuoteType[] = [
          {
            ...quoteType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuoteTypeToCollectionIfMissing(quoteTypeCollection, quoteType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuoteType to an array that doesn't contain it", () => {
        const quoteType: IQuoteType = sampleWithRequiredData;
        const quoteTypeCollection: IQuoteType[] = [sampleWithPartialData];
        expectedResult = service.addQuoteTypeToCollectionIfMissing(quoteTypeCollection, quoteType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quoteType);
      });

      it('should add only unique QuoteType to an array', () => {
        const quoteTypeArray: IQuoteType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quoteTypeCollection: IQuoteType[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteTypeToCollectionIfMissing(quoteTypeCollection, ...quoteTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quoteType: IQuoteType = sampleWithRequiredData;
        const quoteType2: IQuoteType = sampleWithPartialData;
        expectedResult = service.addQuoteTypeToCollectionIfMissing([], quoteType, quoteType2);
        expect(expectedResult).toEqual([quoteType, quoteType2]);
      });

      it('should accept null and undefined values', () => {
        const quoteType: IQuoteType = sampleWithRequiredData;
        expectedResult = service.addQuoteTypeToCollectionIfMissing([], null, quoteType, undefined);
        expect(expectedResult).toEqual([quoteType]);
      });

      it('should return initial array if no QuoteType is added', () => {
        const quoteTypeCollection: IQuoteType[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteTypeToCollectionIfMissing(quoteTypeCollection, undefined, null);
        expect(expectedResult).toEqual(quoteTypeCollection);
      });
    });

    describe('compareQuoteType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuoteType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19838 };
        const entity2 = null;

        const compareResult1 = service.compareQuoteType(entity1, entity2);
        const compareResult2 = service.compareQuoteType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19838 };
        const entity2 = { id: 5628 };

        const compareResult1 = service.compareQuoteType(entity1, entity2);
        const compareResult2 = service.compareQuoteType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 19838 };
        const entity2 = { id: 19838 };

        const compareResult1 = service.compareQuoteType(entity1, entity2);
        const compareResult2 = service.compareQuoteType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
