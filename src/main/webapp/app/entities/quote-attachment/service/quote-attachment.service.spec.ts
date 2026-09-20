import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IQuoteAttachment } from '../quote-attachment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../quote-attachment.test-samples';

import { QuoteAttachmentService } from './quote-attachment.service';

const requireRestSample: IQuoteAttachment = {
  ...sampleWithRequiredData,
};

describe('QuoteAttachment Service', () => {
  let service: QuoteAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuoteAttachment | IQuoteAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(QuoteAttachmentService);
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

    it('should create a QuoteAttachment', () => {
      const quoteAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(quoteAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuoteAttachment', () => {
      const quoteAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(quoteAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuoteAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuoteAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuoteAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addQuoteAttachmentToCollectionIfMissing', () => {
      it('should add a QuoteAttachment to an empty array', () => {
        const quoteAttachment: IQuoteAttachment = sampleWithRequiredData;
        expectedResult = service.addQuoteAttachmentToCollectionIfMissing([], quoteAttachment);
        expect(expectedResult).toEqual([quoteAttachment]);
      });

      it('should not add a QuoteAttachment to an array that contains it', () => {
        const quoteAttachment: IQuoteAttachment = sampleWithRequiredData;
        const quoteAttachmentCollection: IQuoteAttachment[] = [
          {
            ...quoteAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuoteAttachmentToCollectionIfMissing(quoteAttachmentCollection, quoteAttachment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuoteAttachment to an array that doesn't contain it", () => {
        const quoteAttachment: IQuoteAttachment = sampleWithRequiredData;
        const quoteAttachmentCollection: IQuoteAttachment[] = [sampleWithPartialData];
        expectedResult = service.addQuoteAttachmentToCollectionIfMissing(quoteAttachmentCollection, quoteAttachment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quoteAttachment);
      });

      it('should add only unique QuoteAttachment to an array', () => {
        const quoteAttachmentArray: IQuoteAttachment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const quoteAttachmentCollection: IQuoteAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteAttachmentToCollectionIfMissing(quoteAttachmentCollection, ...quoteAttachmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quoteAttachment: IQuoteAttachment = sampleWithRequiredData;
        const quoteAttachment2: IQuoteAttachment = sampleWithPartialData;
        expectedResult = service.addQuoteAttachmentToCollectionIfMissing([], quoteAttachment, quoteAttachment2);
        expect(expectedResult).toEqual([quoteAttachment, quoteAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const quoteAttachment: IQuoteAttachment = sampleWithRequiredData;
        expectedResult = service.addQuoteAttachmentToCollectionIfMissing([], null, quoteAttachment, undefined);
        expect(expectedResult).toEqual([quoteAttachment]);
      });

      it('should return initial array if no QuoteAttachment is added', () => {
        const quoteAttachmentCollection: IQuoteAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addQuoteAttachmentToCollectionIfMissing(quoteAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(quoteAttachmentCollection);
      });
    });

    describe('compareQuoteAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuoteAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18038 };
        const entity2 = null;

        const compareResult1 = service.compareQuoteAttachment(entity1, entity2);
        const compareResult2 = service.compareQuoteAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18038 };
        const entity2 = { id: 29771 };

        const compareResult1 = service.compareQuoteAttachment(entity1, entity2);
        const compareResult2 = service.compareQuoteAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18038 };
        const entity2 = { id: 18038 };

        const compareResult1 = service.compareQuoteAttachment(entity1, entity2);
        const compareResult2 = service.compareQuoteAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
