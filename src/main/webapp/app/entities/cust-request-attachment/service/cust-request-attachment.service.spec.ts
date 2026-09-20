import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustRequestAttachment } from '../cust-request-attachment.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../cust-request-attachment.test-samples';

import { CustRequestAttachmentService } from './cust-request-attachment.service';

const requireRestSample: ICustRequestAttachment = {
  ...sampleWithRequiredData,
};

describe('CustRequestAttachment Service', () => {
  let service: CustRequestAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustRequestAttachment | ICustRequestAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustRequestAttachmentService);
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

    it('should create a CustRequestAttachment', () => {
      const custRequestAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(custRequestAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustRequestAttachment', () => {
      const custRequestAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(custRequestAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustRequestAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustRequestAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustRequestAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustRequestAttachmentToCollectionIfMissing', () => {
      it('should add a CustRequestAttachment to an empty array', () => {
        const custRequestAttachment: ICustRequestAttachment = sampleWithRequiredData;
        expectedResult = service.addCustRequestAttachmentToCollectionIfMissing([], custRequestAttachment);
        expect(expectedResult).toEqual([custRequestAttachment]);
      });

      it('should not add a CustRequestAttachment to an array that contains it', () => {
        const custRequestAttachment: ICustRequestAttachment = sampleWithRequiredData;
        const custRequestAttachmentCollection: ICustRequestAttachment[] = [
          {
            ...custRequestAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustRequestAttachmentToCollectionIfMissing(custRequestAttachmentCollection, custRequestAttachment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustRequestAttachment to an array that doesn't contain it", () => {
        const custRequestAttachment: ICustRequestAttachment = sampleWithRequiredData;
        const custRequestAttachmentCollection: ICustRequestAttachment[] = [sampleWithPartialData];
        expectedResult = service.addCustRequestAttachmentToCollectionIfMissing(custRequestAttachmentCollection, custRequestAttachment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(custRequestAttachment);
      });

      it('should add only unique CustRequestAttachment to an array', () => {
        const custRequestAttachmentArray: ICustRequestAttachment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const custRequestAttachmentCollection: ICustRequestAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestAttachmentToCollectionIfMissing(
          custRequestAttachmentCollection,
          ...custRequestAttachmentArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const custRequestAttachment: ICustRequestAttachment = sampleWithRequiredData;
        const custRequestAttachment2: ICustRequestAttachment = sampleWithPartialData;
        expectedResult = service.addCustRequestAttachmentToCollectionIfMissing([], custRequestAttachment, custRequestAttachment2);
        expect(expectedResult).toEqual([custRequestAttachment, custRequestAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const custRequestAttachment: ICustRequestAttachment = sampleWithRequiredData;
        expectedResult = service.addCustRequestAttachmentToCollectionIfMissing([], null, custRequestAttachment, undefined);
        expect(expectedResult).toEqual([custRequestAttachment]);
      });

      it('should return initial array if no CustRequestAttachment is added', () => {
        const custRequestAttachmentCollection: ICustRequestAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestAttachmentToCollectionIfMissing(custRequestAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(custRequestAttachmentCollection);
      });
    });

    describe('compareCustRequestAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustRequestAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31720 };
        const entity2 = null;

        const compareResult1 = service.compareCustRequestAttachment(entity1, entity2);
        const compareResult2 = service.compareCustRequestAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31720 };
        const entity2 = { id: 30483 };

        const compareResult1 = service.compareCustRequestAttachment(entity1, entity2);
        const compareResult2 = service.compareCustRequestAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 31720 };
        const entity2 = { id: 31720 };

        const compareResult1 = service.compareCustRequestAttachment(entity1, entity2);
        const compareResult2 = service.compareCustRequestAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
