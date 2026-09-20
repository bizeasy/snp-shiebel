import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustRequestItemAttachment } from '../cust-request-item-attachment.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../cust-request-item-attachment.test-samples';

import { CustRequestItemAttachmentService } from './cust-request-item-attachment.service';

const requireRestSample: ICustRequestItemAttachment = {
  ...sampleWithRequiredData,
};

describe('CustRequestItemAttachment Service', () => {
  let service: CustRequestItemAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustRequestItemAttachment | ICustRequestItemAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustRequestItemAttachmentService);
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

    it('should create a CustRequestItemAttachment', () => {
      const custRequestItemAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(custRequestItemAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustRequestItemAttachment', () => {
      const custRequestItemAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(custRequestItemAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustRequestItemAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustRequestItemAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustRequestItemAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustRequestItemAttachmentToCollectionIfMissing', () => {
      it('should add a CustRequestItemAttachment to an empty array', () => {
        const custRequestItemAttachment: ICustRequestItemAttachment = sampleWithRequiredData;
        expectedResult = service.addCustRequestItemAttachmentToCollectionIfMissing([], custRequestItemAttachment);
        expect(expectedResult).toEqual([custRequestItemAttachment]);
      });

      it('should not add a CustRequestItemAttachment to an array that contains it', () => {
        const custRequestItemAttachment: ICustRequestItemAttachment = sampleWithRequiredData;
        const custRequestItemAttachmentCollection: ICustRequestItemAttachment[] = [
          {
            ...custRequestItemAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustRequestItemAttachmentToCollectionIfMissing(
          custRequestItemAttachmentCollection,
          custRequestItemAttachment,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustRequestItemAttachment to an array that doesn't contain it", () => {
        const custRequestItemAttachment: ICustRequestItemAttachment = sampleWithRequiredData;
        const custRequestItemAttachmentCollection: ICustRequestItemAttachment[] = [sampleWithPartialData];
        expectedResult = service.addCustRequestItemAttachmentToCollectionIfMissing(
          custRequestItemAttachmentCollection,
          custRequestItemAttachment,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(custRequestItemAttachment);
      });

      it('should add only unique CustRequestItemAttachment to an array', () => {
        const custRequestItemAttachmentArray: ICustRequestItemAttachment[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const custRequestItemAttachmentCollection: ICustRequestItemAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestItemAttachmentToCollectionIfMissing(
          custRequestItemAttachmentCollection,
          ...custRequestItemAttachmentArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const custRequestItemAttachment: ICustRequestItemAttachment = sampleWithRequiredData;
        const custRequestItemAttachment2: ICustRequestItemAttachment = sampleWithPartialData;
        expectedResult = service.addCustRequestItemAttachmentToCollectionIfMissing(
          [],
          custRequestItemAttachment,
          custRequestItemAttachment2,
        );
        expect(expectedResult).toEqual([custRequestItemAttachment, custRequestItemAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const custRequestItemAttachment: ICustRequestItemAttachment = sampleWithRequiredData;
        expectedResult = service.addCustRequestItemAttachmentToCollectionIfMissing([], null, custRequestItemAttachment, undefined);
        expect(expectedResult).toEqual([custRequestItemAttachment]);
      });

      it('should return initial array if no CustRequestItemAttachment is added', () => {
        const custRequestItemAttachmentCollection: ICustRequestItemAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestItemAttachmentToCollectionIfMissing(custRequestItemAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(custRequestItemAttachmentCollection);
      });
    });

    describe('compareCustRequestItemAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustRequestItemAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 342 };
        const entity2 = null;

        const compareResult1 = service.compareCustRequestItemAttachment(entity1, entity2);
        const compareResult2 = service.compareCustRequestItemAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 342 };
        const entity2 = { id: 23584 };

        const compareResult1 = service.compareCustRequestItemAttachment(entity1, entity2);
        const compareResult2 = service.compareCustRequestItemAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 342 };
        const entity2 = { id: 342 };

        const compareResult1 = service.compareCustRequestItemAttachment(entity1, entity2);
        const compareResult2 = service.compareCustRequestItemAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
