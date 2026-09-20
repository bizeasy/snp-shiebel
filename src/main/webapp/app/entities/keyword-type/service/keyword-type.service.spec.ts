import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IKeywordType } from '../keyword-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../keyword-type.test-samples';

import { KeywordTypeService } from './keyword-type.service';

const requireRestSample: IKeywordType = {
  ...sampleWithRequiredData,
};

describe('KeywordType Service', () => {
  let service: KeywordTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IKeywordType | IKeywordType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KeywordTypeService);
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

    it('should create a KeywordType', () => {
      const keywordType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(keywordType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KeywordType', () => {
      const keywordType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(keywordType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KeywordType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KeywordType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KeywordType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addKeywordTypeToCollectionIfMissing', () => {
      it('should add a KeywordType to an empty array', () => {
        const keywordType: IKeywordType = sampleWithRequiredData;
        expectedResult = service.addKeywordTypeToCollectionIfMissing([], keywordType);
        expect(expectedResult).toEqual([keywordType]);
      });

      it('should not add a KeywordType to an array that contains it', () => {
        const keywordType: IKeywordType = sampleWithRequiredData;
        const keywordTypeCollection: IKeywordType[] = [
          {
            ...keywordType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKeywordTypeToCollectionIfMissing(keywordTypeCollection, keywordType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KeywordType to an array that doesn't contain it", () => {
        const keywordType: IKeywordType = sampleWithRequiredData;
        const keywordTypeCollection: IKeywordType[] = [sampleWithPartialData];
        expectedResult = service.addKeywordTypeToCollectionIfMissing(keywordTypeCollection, keywordType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(keywordType);
      });

      it('should add only unique KeywordType to an array', () => {
        const keywordTypeArray: IKeywordType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const keywordTypeCollection: IKeywordType[] = [sampleWithRequiredData];
        expectedResult = service.addKeywordTypeToCollectionIfMissing(keywordTypeCollection, ...keywordTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const keywordType: IKeywordType = sampleWithRequiredData;
        const keywordType2: IKeywordType = sampleWithPartialData;
        expectedResult = service.addKeywordTypeToCollectionIfMissing([], keywordType, keywordType2);
        expect(expectedResult).toEqual([keywordType, keywordType2]);
      });

      it('should accept null and undefined values', () => {
        const keywordType: IKeywordType = sampleWithRequiredData;
        expectedResult = service.addKeywordTypeToCollectionIfMissing([], null, keywordType, undefined);
        expect(expectedResult).toEqual([keywordType]);
      });

      it('should return initial array if no KeywordType is added', () => {
        const keywordTypeCollection: IKeywordType[] = [sampleWithRequiredData];
        expectedResult = service.addKeywordTypeToCollectionIfMissing(keywordTypeCollection, undefined, null);
        expect(expectedResult).toEqual(keywordTypeCollection);
      });
    });

    describe('compareKeywordType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKeywordType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27532 };
        const entity2 = null;

        const compareResult1 = service.compareKeywordType(entity1, entity2);
        const compareResult2 = service.compareKeywordType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27532 };
        const entity2 = { id: 31809 };

        const compareResult1 = service.compareKeywordType(entity1, entity2);
        const compareResult2 = service.compareKeywordType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 27532 };
        const entity2 = { id: 27532 };

        const compareResult1 = service.compareKeywordType(entity1, entity2);
        const compareResult2 = service.compareKeywordType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
