import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IItemIssuance } from '../item-issuance.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../item-issuance.test-samples';

import { ItemIssuanceService, RestItemIssuance } from './item-issuance.service';

const requireRestSample: RestItemIssuance = {
  ...sampleWithRequiredData,
  issuedDate: sampleWithRequiredData.issuedDate?.toJSON(),
};

describe('ItemIssuance Service', () => {
  let service: ItemIssuanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IItemIssuance | IItemIssuance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ItemIssuanceService);
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

    it('should create a ItemIssuance', () => {
      const itemIssuance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(itemIssuance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemIssuance', () => {
      const itemIssuance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(itemIssuance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemIssuance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemIssuance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ItemIssuance', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addItemIssuanceToCollectionIfMissing', () => {
      it('should add a ItemIssuance to an empty array', () => {
        const itemIssuance: IItemIssuance = sampleWithRequiredData;
        expectedResult = service.addItemIssuanceToCollectionIfMissing([], itemIssuance);
        expect(expectedResult).toEqual([itemIssuance]);
      });

      it('should not add a ItemIssuance to an array that contains it', () => {
        const itemIssuance: IItemIssuance = sampleWithRequiredData;
        const itemIssuanceCollection: IItemIssuance[] = [
          {
            ...itemIssuance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addItemIssuanceToCollectionIfMissing(itemIssuanceCollection, itemIssuance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemIssuance to an array that doesn't contain it", () => {
        const itemIssuance: IItemIssuance = sampleWithRequiredData;
        const itemIssuanceCollection: IItemIssuance[] = [sampleWithPartialData];
        expectedResult = service.addItemIssuanceToCollectionIfMissing(itemIssuanceCollection, itemIssuance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemIssuance);
      });

      it('should add only unique ItemIssuance to an array', () => {
        const itemIssuanceArray: IItemIssuance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const itemIssuanceCollection: IItemIssuance[] = [sampleWithRequiredData];
        expectedResult = service.addItemIssuanceToCollectionIfMissing(itemIssuanceCollection, ...itemIssuanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemIssuance: IItemIssuance = sampleWithRequiredData;
        const itemIssuance2: IItemIssuance = sampleWithPartialData;
        expectedResult = service.addItemIssuanceToCollectionIfMissing([], itemIssuance, itemIssuance2);
        expect(expectedResult).toEqual([itemIssuance, itemIssuance2]);
      });

      it('should accept null and undefined values', () => {
        const itemIssuance: IItemIssuance = sampleWithRequiredData;
        expectedResult = service.addItemIssuanceToCollectionIfMissing([], null, itemIssuance, undefined);
        expect(expectedResult).toEqual([itemIssuance]);
      });

      it('should return initial array if no ItemIssuance is added', () => {
        const itemIssuanceCollection: IItemIssuance[] = [sampleWithRequiredData];
        expectedResult = service.addItemIssuanceToCollectionIfMissing(itemIssuanceCollection, undefined, null);
        expect(expectedResult).toEqual(itemIssuanceCollection);
      });
    });

    describe('compareItemIssuance', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareItemIssuance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24352 };
        const entity2 = null;

        const compareResult1 = service.compareItemIssuance(entity1, entity2);
        const compareResult2 = service.compareItemIssuance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24352 };
        const entity2 = { id: 8405 };

        const compareResult1 = service.compareItemIssuance(entity1, entity2);
        const compareResult2 = service.compareItemIssuance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 24352 };
        const entity2 = { id: 24352 };

        const compareResult1 = service.compareItemIssuance(entity1, entity2);
        const compareResult2 = service.compareItemIssuance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
