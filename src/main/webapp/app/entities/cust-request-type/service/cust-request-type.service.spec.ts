import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICustRequestType } from '../cust-request-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cust-request-type.test-samples';

import { CustRequestTypeService } from './cust-request-type.service';

const requireRestSample: ICustRequestType = {
  ...sampleWithRequiredData,
};

describe('CustRequestType Service', () => {
  let service: CustRequestTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustRequestType | ICustRequestType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustRequestTypeService);
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

    it('should create a CustRequestType', () => {
      const custRequestType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(custRequestType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustRequestType', () => {
      const custRequestType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(custRequestType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustRequestType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustRequestType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustRequestType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustRequestTypeToCollectionIfMissing', () => {
      it('should add a CustRequestType to an empty array', () => {
        const custRequestType: ICustRequestType = sampleWithRequiredData;
        expectedResult = service.addCustRequestTypeToCollectionIfMissing([], custRequestType);
        expect(expectedResult).toEqual([custRequestType]);
      });

      it('should not add a CustRequestType to an array that contains it', () => {
        const custRequestType: ICustRequestType = sampleWithRequiredData;
        const custRequestTypeCollection: ICustRequestType[] = [
          {
            ...custRequestType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustRequestTypeToCollectionIfMissing(custRequestTypeCollection, custRequestType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustRequestType to an array that doesn't contain it", () => {
        const custRequestType: ICustRequestType = sampleWithRequiredData;
        const custRequestTypeCollection: ICustRequestType[] = [sampleWithPartialData];
        expectedResult = service.addCustRequestTypeToCollectionIfMissing(custRequestTypeCollection, custRequestType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(custRequestType);
      });

      it('should add only unique CustRequestType to an array', () => {
        const custRequestTypeArray: ICustRequestType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const custRequestTypeCollection: ICustRequestType[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestTypeToCollectionIfMissing(custRequestTypeCollection, ...custRequestTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const custRequestType: ICustRequestType = sampleWithRequiredData;
        const custRequestType2: ICustRequestType = sampleWithPartialData;
        expectedResult = service.addCustRequestTypeToCollectionIfMissing([], custRequestType, custRequestType2);
        expect(expectedResult).toEqual([custRequestType, custRequestType2]);
      });

      it('should accept null and undefined values', () => {
        const custRequestType: ICustRequestType = sampleWithRequiredData;
        expectedResult = service.addCustRequestTypeToCollectionIfMissing([], null, custRequestType, undefined);
        expect(expectedResult).toEqual([custRequestType]);
      });

      it('should return initial array if no CustRequestType is added', () => {
        const custRequestTypeCollection: ICustRequestType[] = [sampleWithRequiredData];
        expectedResult = service.addCustRequestTypeToCollectionIfMissing(custRequestTypeCollection, undefined, null);
        expect(expectedResult).toEqual(custRequestTypeCollection);
      });
    });

    describe('compareCustRequestType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustRequestType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4424 };
        const entity2 = null;

        const compareResult1 = service.compareCustRequestType(entity1, entity2);
        const compareResult2 = service.compareCustRequestType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4424 };
        const entity2 = { id: 951 };

        const compareResult1 = service.compareCustRequestType(entity1, entity2);
        const compareResult2 = service.compareCustRequestType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4424 };
        const entity2 = { id: 4424 };

        const compareResult1 = service.compareCustRequestType(entity1, entity2);
        const compareResult2 = service.compareCustRequestType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
