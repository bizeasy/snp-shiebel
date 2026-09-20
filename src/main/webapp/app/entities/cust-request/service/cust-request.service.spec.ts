import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustRequest } from '../cust-request.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cust-request.test-samples';

import { CustRequestService, RestCustRequest } from './cust-request.service';

const requireRestSample: RestCustRequest = {
  ...sampleWithRequiredData,
  custRequestDate: sampleWithRequiredData.custRequestDate?.toJSON(),
  responseRequiredDate: sampleWithRequiredData.responseRequiredDate?.toJSON(),
  openDateTime: sampleWithRequiredData.openDateTime?.toJSON(),
  closedDateTime: sampleWithRequiredData.closedDateTime?.toJSON(),
};

describe('CustRequest Service', () => {
  let service: CustRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustRequest | ICustRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustRequestService);
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

    it('should create a CustRequest', () => {
      const custRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(custRequest).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustRequest', () => {
      const custRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(custRequest).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustRequest', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustRequestToCollectionIfMissing', () => {
      it('should add a CustRequest to an empty array', () => {
        const custRequest: ICustRequest = sampleWithRequiredData;
        expectedResult = service.addCustRequestToCollectionIfMissing([], custRequest);
        expect(expectedResult).toEqual([custRequest]);
      });

      it('should not add a CustRequest to an array that contains it', () => {
        const custRequest: ICustRequest = sampleWithRequiredData;
        const custRequestCollection: ICustRequest[] = [
          {
            ...custRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustRequestToCollectionIfMissing(custRequestCollection, custRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustRequest to an array that doesn't contain it", () => {
        const custRequest: ICustRequest = sampleWithRequiredData;
        const custRequestCollection: ICustRequest[] = [sampleWithPartialData];
        expectedResult = service.addCustRequestToCollectionIfMissing(custRequestCollection, custRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(custRequest);
      });

      it('should add only unique CustRequest to an array', () => {
        const custRequestArray: ICustRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const custRequestCollection: ICustRequest[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestToCollectionIfMissing(custRequestCollection, ...custRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const custRequest: ICustRequest = sampleWithRequiredData;
        const custRequest2: ICustRequest = sampleWithPartialData;
        expectedResult = service.addCustRequestToCollectionIfMissing([], custRequest, custRequest2);
        expect(expectedResult).toEqual([custRequest, custRequest2]);
      });

      it('should accept null and undefined values', () => {
        const custRequest: ICustRequest = sampleWithRequiredData;
        expectedResult = service.addCustRequestToCollectionIfMissing([], null, custRequest, undefined);
        expect(expectedResult).toEqual([custRequest]);
      });

      it('should return initial array if no CustRequest is added', () => {
        const custRequestCollection: ICustRequest[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestToCollectionIfMissing(custRequestCollection, undefined, null);
        expect(expectedResult).toEqual(custRequestCollection);
      });
    });

    describe('compareCustRequest', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17760 };
        const entity2 = null;

        const compareResult1 = service.compareCustRequest(entity1, entity2);
        const compareResult2 = service.compareCustRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17760 };
        const entity2 = { id: 10199 };

        const compareResult1 = service.compareCustRequest(entity1, entity2);
        const compareResult2 = service.compareCustRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 17760 };
        const entity2 = { id: 17760 };

        const compareResult1 = service.compareCustRequest(entity1, entity2);
        const compareResult2 = service.compareCustRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
