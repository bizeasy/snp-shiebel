import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICashBookAttachment } from '../cash-book-attachment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cash-book-attachment.test-samples';

import { CashBookAttachmentService } from './cash-book-attachment.service';

const requireRestSample: ICashBookAttachment = {
  ...sampleWithRequiredData,
};

describe('CashBookAttachment Service', () => {
  let service: CashBookAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICashBookAttachment | ICashBookAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CashBookAttachmentService);
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

    it('should create a CashBookAttachment', () => {
      const cashBookAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cashBookAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CashBookAttachment', () => {
      const cashBookAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cashBookAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CashBookAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CashBookAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CashBookAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCashBookAttachmentToCollectionIfMissing', () => {
      it('should add a CashBookAttachment to an empty array', () => {
        const cashBookAttachment: ICashBookAttachment = sampleWithRequiredData;
        expectedResult = service.addCashBookAttachmentToCollectionIfMissing([], cashBookAttachment);
        expect(expectedResult).toEqual([cashBookAttachment]);
      });

      it('should not add a CashBookAttachment to an array that contains it', () => {
        const cashBookAttachment: ICashBookAttachment = sampleWithRequiredData;
        const cashBookAttachmentCollection: ICashBookAttachment[] = [
          {
            ...cashBookAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCashBookAttachmentToCollectionIfMissing(cashBookAttachmentCollection, cashBookAttachment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CashBookAttachment to an array that doesn't contain it", () => {
        const cashBookAttachment: ICashBookAttachment = sampleWithRequiredData;
        const cashBookAttachmentCollection: ICashBookAttachment[] = [sampleWithPartialData];
        expectedResult = service.addCashBookAttachmentToCollectionIfMissing(cashBookAttachmentCollection, cashBookAttachment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cashBookAttachment);
      });

      it('should add only unique CashBookAttachment to an array', () => {
        const cashBookAttachmentArray: ICashBookAttachment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cashBookAttachmentCollection: ICashBookAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addCashBookAttachmentToCollectionIfMissing(cashBookAttachmentCollection, ...cashBookAttachmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cashBookAttachment: ICashBookAttachment = sampleWithRequiredData;
        const cashBookAttachment2: ICashBookAttachment = sampleWithPartialData;
        expectedResult = service.addCashBookAttachmentToCollectionIfMissing([], cashBookAttachment, cashBookAttachment2);
        expect(expectedResult).toEqual([cashBookAttachment, cashBookAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const cashBookAttachment: ICashBookAttachment = sampleWithRequiredData;
        expectedResult = service.addCashBookAttachmentToCollectionIfMissing([], null, cashBookAttachment, undefined);
        expect(expectedResult).toEqual([cashBookAttachment]);
      });

      it('should return initial array if no CashBookAttachment is added', () => {
        const cashBookAttachmentCollection: ICashBookAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addCashBookAttachmentToCollectionIfMissing(cashBookAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(cashBookAttachmentCollection);
      });
    });

    describe('compareCashBookAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCashBookAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24340 };
        const entity2 = null;

        const compareResult1 = service.compareCashBookAttachment(entity1, entity2);
        const compareResult2 = service.compareCashBookAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24340 };
        const entity2 = { id: 11865 };

        const compareResult1 = service.compareCashBookAttachment(entity1, entity2);
        const compareResult2 = service.compareCashBookAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 24340 };
        const entity2 = { id: 24340 };

        const compareResult1 = service.compareCashBookAttachment(entity1, entity2);
        const compareResult2 = service.compareCashBookAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
