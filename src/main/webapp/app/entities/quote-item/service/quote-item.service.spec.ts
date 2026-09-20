import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IQuoteItem } from '../quote-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../quote-item.test-samples';

import { QuoteItemService, RestQuoteItem } from './quote-item.service';

const requireRestSample: RestQuoteItem = {
  ...sampleWithRequiredData,
  estimatedDeliveryDate: sampleWithRequiredData.estimatedDeliveryDate?.toJSON(),
};

describe('QuoteItem Service', () => {
  let service: QuoteItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuoteItem | IQuoteItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuoteItemService);
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

    it('should create a QuoteItem', () => {
      const quoteItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quoteItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuoteItem', () => {
      const quoteItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quoteItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuoteItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuoteItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuoteItem', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addQuoteItemToCollectionIfMissing', () => {
      it('should add a QuoteItem to an empty array', () => {
        const quoteItem: IQuoteItem = sampleWithRequiredData;
        expectedResult = service.addQuoteItemToCollectionIfMissing([], quoteItem);
        expect(expectedResult).toEqual([quoteItem]);
      });

      it('should not add a QuoteItem to an array that contains it', () => {
        const quoteItem: IQuoteItem = sampleWithRequiredData;
        const quoteItemCollection: IQuoteItem[] = [
          {
            ...quoteItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuoteItemToCollectionIfMissing(quoteItemCollection, quoteItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuoteItem to an array that doesn't contain it", () => {
        const quoteItem: IQuoteItem = sampleWithRequiredData;
        const quoteItemCollection: IQuoteItem[] = [sampleWithPartialData];
        expectedResult = service.addQuoteItemToCollectionIfMissing(quoteItemCollection, quoteItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quoteItem);
      });

      it('should add only unique QuoteItem to an array', () => {
        const quoteItemArray: IQuoteItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quoteItemCollection: IQuoteItem[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteItemToCollectionIfMissing(quoteItemCollection, ...quoteItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quoteItem: IQuoteItem = sampleWithRequiredData;
        const quoteItem2: IQuoteItem = sampleWithPartialData;
        expectedResult = service.addQuoteItemToCollectionIfMissing([], quoteItem, quoteItem2);
        expect(expectedResult).toEqual([quoteItem, quoteItem2]);
      });

      it('should accept null and undefined values', () => {
        const quoteItem: IQuoteItem = sampleWithRequiredData;
        expectedResult = service.addQuoteItemToCollectionIfMissing([], null, quoteItem, undefined);
        expect(expectedResult).toEqual([quoteItem]);
      });

      it('should return initial array if no QuoteItem is added', () => {
        const quoteItemCollection: IQuoteItem[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteItemToCollectionIfMissing(quoteItemCollection, undefined, null);
        expect(expectedResult).toEqual(quoteItemCollection);
      });
    });

    describe('compareQuoteItem', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuoteItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28072 };
        const entity2 = null;

        const compareResult1 = service.compareQuoteItem(entity1, entity2);
        const compareResult2 = service.compareQuoteItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28072 };
        const entity2 = { id: 30045 };

        const compareResult1 = service.compareQuoteItem(entity1, entity2);
        const compareResult2 = service.compareQuoteItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28072 };
        const entity2 = { id: 28072 };

        const compareResult1 = service.compareQuoteItem(entity1, entity2);
        const compareResult2 = service.compareQuoteItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
