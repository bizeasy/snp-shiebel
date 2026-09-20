import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IQuoteAdjustment } from '../quote-adjustment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../quote-adjustment.test-samples';

import { QuoteAdjustmentService, RestQuoteAdjustment } from './quote-adjustment.service';

const requireRestSample: RestQuoteAdjustment = {
  ...sampleWithRequiredData,
  entryDate: sampleWithRequiredData.entryDate?.toJSON(),
};

describe('QuoteAdjustment Service', () => {
  let service: QuoteAdjustmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuoteAdjustment | IQuoteAdjustment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuoteAdjustmentService);
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

    it('should create a QuoteAdjustment', () => {
      const quoteAdjustment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quoteAdjustment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuoteAdjustment', () => {
      const quoteAdjustment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quoteAdjustment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuoteAdjustment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuoteAdjustment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuoteAdjustment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addQuoteAdjustmentToCollectionIfMissing', () => {
      it('should add a QuoteAdjustment to an empty array', () => {
        const quoteAdjustment: IQuoteAdjustment = sampleWithRequiredData;
        expectedResult = service.addQuoteAdjustmentToCollectionIfMissing([], quoteAdjustment);
        expect(expectedResult).toEqual([quoteAdjustment]);
      });

      it('should not add a QuoteAdjustment to an array that contains it', () => {
        const quoteAdjustment: IQuoteAdjustment = sampleWithRequiredData;
        const quoteAdjustmentCollection: IQuoteAdjustment[] = [
          {
            ...quoteAdjustment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuoteAdjustmentToCollectionIfMissing(quoteAdjustmentCollection, quoteAdjustment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuoteAdjustment to an array that doesn't contain it", () => {
        const quoteAdjustment: IQuoteAdjustment = sampleWithRequiredData;
        const quoteAdjustmentCollection: IQuoteAdjustment[] = [sampleWithPartialData];
        expectedResult = service.addQuoteAdjustmentToCollectionIfMissing(quoteAdjustmentCollection, quoteAdjustment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quoteAdjustment);
      });

      it('should add only unique QuoteAdjustment to an array', () => {
        const quoteAdjustmentArray: IQuoteAdjustment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quoteAdjustmentCollection: IQuoteAdjustment[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteAdjustmentToCollectionIfMissing(quoteAdjustmentCollection, ...quoteAdjustmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quoteAdjustment: IQuoteAdjustment = sampleWithRequiredData;
        const quoteAdjustment2: IQuoteAdjustment = sampleWithPartialData;
        expectedResult = service.addQuoteAdjustmentToCollectionIfMissing([], quoteAdjustment, quoteAdjustment2);
        expect(expectedResult).toEqual([quoteAdjustment, quoteAdjustment2]);
      });

      it('should accept null and undefined values', () => {
        const quoteAdjustment: IQuoteAdjustment = sampleWithRequiredData;
        expectedResult = service.addQuoteAdjustmentToCollectionIfMissing([], null, quoteAdjustment, undefined);
        expect(expectedResult).toEqual([quoteAdjustment]);
      });

      it('should return initial array if no QuoteAdjustment is added', () => {
        const quoteAdjustmentCollection: IQuoteAdjustment[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteAdjustmentToCollectionIfMissing(quoteAdjustmentCollection, undefined, null);
        expect(expectedResult).toEqual(quoteAdjustmentCollection);
      });
    });

    describe('compareQuoteAdjustment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuoteAdjustment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2656 };
        const entity2 = null;

        const compareResult1 = service.compareQuoteAdjustment(entity1, entity2);
        const compareResult2 = service.compareQuoteAdjustment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2656 };
        const entity2 = { id: 6610 };

        const compareResult1 = service.compareQuoteAdjustment(entity1, entity2);
        const compareResult2 = service.compareQuoteAdjustment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 2656 };
        const entity2 = { id: 2656 };

        const compareResult1 = service.compareQuoteAdjustment(entity1, entity2);
        const compareResult2 = service.compareQuoteAdjustment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
