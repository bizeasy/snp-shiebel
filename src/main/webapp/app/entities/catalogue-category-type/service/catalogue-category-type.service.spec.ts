import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICatalogueCategoryType } from '../catalogue-category-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../catalogue-category-type.test-samples';

import { CatalogueCategoryTypeService } from './catalogue-category-type.service';

const requireRestSample: ICatalogueCategoryType = {
  ...sampleWithRequiredData,
};

describe('CatalogueCategoryType Service', () => {
  let service: CatalogueCategoryTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICatalogueCategoryType | ICatalogueCategoryType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogueCategoryTypeService);
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

    it('should create a CatalogueCategoryType', () => {
      const catalogueCategoryType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(catalogueCategoryType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CatalogueCategoryType', () => {
      const catalogueCategoryType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(catalogueCategoryType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CatalogueCategoryType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CatalogueCategoryType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CatalogueCategoryType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCatalogueCategoryTypeToCollectionIfMissing', () => {
      it('should add a CatalogueCategoryType to an empty array', () => {
        const catalogueCategoryType: ICatalogueCategoryType = sampleWithRequiredData;
        expectedResult = service.addCatalogueCategoryTypeToCollectionIfMissing([], catalogueCategoryType);
        expect(expectedResult).toEqual([catalogueCategoryType]);
      });

      it('should not add a CatalogueCategoryType to an array that contains it', () => {
        const catalogueCategoryType: ICatalogueCategoryType = sampleWithRequiredData;
        const catalogueCategoryTypeCollection: ICatalogueCategoryType[] = [
          {
            ...catalogueCategoryType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCatalogueCategoryTypeToCollectionIfMissing(catalogueCategoryTypeCollection, catalogueCategoryType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CatalogueCategoryType to an array that doesn't contain it", () => {
        const catalogueCategoryType: ICatalogueCategoryType = sampleWithRequiredData;
        const catalogueCategoryTypeCollection: ICatalogueCategoryType[] = [sampleWithPartialData];
        expectedResult = service.addCatalogueCategoryTypeToCollectionIfMissing(catalogueCategoryTypeCollection, catalogueCategoryType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogueCategoryType);
      });

      it('should add only unique CatalogueCategoryType to an array', () => {
        const catalogueCategoryTypeArray: ICatalogueCategoryType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const catalogueCategoryTypeCollection: ICatalogueCategoryType[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogueCategoryTypeToCollectionIfMissing(
          catalogueCategoryTypeCollection,
          ...catalogueCategoryTypeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogueCategoryType: ICatalogueCategoryType = sampleWithRequiredData;
        const catalogueCategoryType2: ICatalogueCategoryType = sampleWithPartialData;
        expectedResult = service.addCatalogueCategoryTypeToCollectionIfMissing([], catalogueCategoryType, catalogueCategoryType2);
        expect(expectedResult).toEqual([catalogueCategoryType, catalogueCategoryType2]);
      });

      it('should accept null and undefined values', () => {
        const catalogueCategoryType: ICatalogueCategoryType = sampleWithRequiredData;
        expectedResult = service.addCatalogueCategoryTypeToCollectionIfMissing([], null, catalogueCategoryType, undefined);
        expect(expectedResult).toEqual([catalogueCategoryType]);
      });

      it('should return initial array if no CatalogueCategoryType is added', () => {
        const catalogueCategoryTypeCollection: ICatalogueCategoryType[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogueCategoryTypeToCollectionIfMissing(catalogueCategoryTypeCollection, undefined, null);
        expect(expectedResult).toEqual(catalogueCategoryTypeCollection);
      });
    });

    describe('compareCatalogueCategoryType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCatalogueCategoryType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27421 };
        const entity2 = null;

        const compareResult1 = service.compareCatalogueCategoryType(entity1, entity2);
        const compareResult2 = service.compareCatalogueCategoryType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27421 };
        const entity2 = { id: 16331 };

        const compareResult1 = service.compareCatalogueCategoryType(entity1, entity2);
        const compareResult2 = service.compareCatalogueCategoryType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 27421 };
        const entity2 = { id: 27421 };

        const compareResult1 = service.compareCatalogueCategoryType(entity1, entity2);
        const compareResult2 = service.compareCatalogueCategoryType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
