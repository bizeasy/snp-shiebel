import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IReturnHeader } from '../return-header.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../return-header.test-samples';

import { ReturnHeaderService } from './return-header.service';

const requireRestSample: IReturnHeader = {
  ...sampleWithRequiredData,
};

describe('ReturnHeader Service', () => {
  let service: ReturnHeaderService;
  let httpMock: HttpTestingController;
  let expectedResult: IReturnHeader | IReturnHeader[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReturnHeaderService);
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

    it('should create a ReturnHeader', () => {
      const returnHeader = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(returnHeader).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReturnHeader', () => {
      const returnHeader = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(returnHeader).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReturnHeader', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReturnHeader', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReturnHeader', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addReturnHeaderToCollectionIfMissing', () => {
      it('should add a ReturnHeader to an empty array', () => {
        const returnHeader: IReturnHeader = sampleWithRequiredData;
        expectedResult = service.addReturnHeaderToCollectionIfMissing([], returnHeader);
        expect(expectedResult).toEqual([returnHeader]);
      });

      it('should not add a ReturnHeader to an array that contains it', () => {
        const returnHeader: IReturnHeader = sampleWithRequiredData;
        const returnHeaderCollection: IReturnHeader[] = [
          {
            ...returnHeader,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReturnHeaderToCollectionIfMissing(returnHeaderCollection, returnHeader);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReturnHeader to an array that doesn't contain it", () => {
        const returnHeader: IReturnHeader = sampleWithRequiredData;
        const returnHeaderCollection: IReturnHeader[] = [sampleWithPartialData];
        expectedResult = service.addReturnHeaderToCollectionIfMissing(returnHeaderCollection, returnHeader);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(returnHeader);
      });

      it('should add only unique ReturnHeader to an array', () => {
        const returnHeaderArray: IReturnHeader[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const returnHeaderCollection: IReturnHeader[] = [sampleWithRequiredData];
        expectedResult = service.addReturnHeaderToCollectionIfMissing(returnHeaderCollection, ...returnHeaderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const returnHeader: IReturnHeader = sampleWithRequiredData;
        const returnHeader2: IReturnHeader = sampleWithPartialData;
        expectedResult = service.addReturnHeaderToCollectionIfMissing([], returnHeader, returnHeader2);
        expect(expectedResult).toEqual([returnHeader, returnHeader2]);
      });

      it('should accept null and undefined values', () => {
        const returnHeader: IReturnHeader = sampleWithRequiredData;
        expectedResult = service.addReturnHeaderToCollectionIfMissing([], null, returnHeader, undefined);
        expect(expectedResult).toEqual([returnHeader]);
      });

      it('should return initial array if no ReturnHeader is added', () => {
        const returnHeaderCollection: IReturnHeader[] = [sampleWithRequiredData];
        expectedResult = service.addReturnHeaderToCollectionIfMissing(returnHeaderCollection, undefined, null);
        expect(expectedResult).toEqual(returnHeaderCollection);
      });
    });

    describe('compareReturnHeader', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReturnHeader(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1951 };
        const entity2 = null;

        const compareResult1 = service.compareReturnHeader(entity1, entity2);
        const compareResult2 = service.compareReturnHeader(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1951 };
        const entity2 = { id: 32527 };

        const compareResult1 = service.compareReturnHeader(entity1, entity2);
        const compareResult2 = service.compareReturnHeader(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 1951 };
        const entity2 = { id: 1951 };

        const compareResult1 = service.compareReturnHeader(entity1, entity2);
        const compareResult2 = service.compareReturnHeader(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
