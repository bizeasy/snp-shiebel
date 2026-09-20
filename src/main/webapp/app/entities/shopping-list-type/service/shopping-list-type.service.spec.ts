import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShoppingListType } from '../shopping-list-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shopping-list-type.test-samples';

import { ShoppingListTypeService } from './shopping-list-type.service';

const requireRestSample: IShoppingListType = {
  ...sampleWithRequiredData,
};

describe('ShoppingListType Service', () => {
  let service: ShoppingListTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IShoppingListType | IShoppingListType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShoppingListTypeService);
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

    it('should create a ShoppingListType', () => {
      const shoppingListType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shoppingListType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShoppingListType', () => {
      const shoppingListType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shoppingListType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShoppingListType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShoppingListType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShoppingListType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShoppingListTypeToCollectionIfMissing', () => {
      it('should add a ShoppingListType to an empty array', () => {
        const shoppingListType: IShoppingListType = sampleWithRequiredData;
        expectedResult = service.addShoppingListTypeToCollectionIfMissing([], shoppingListType);
        expect(expectedResult).toEqual([shoppingListType]);
      });

      it('should not add a ShoppingListType to an array that contains it', () => {
        const shoppingListType: IShoppingListType = sampleWithRequiredData;
        const shoppingListTypeCollection: IShoppingListType[] = [
          {
            ...shoppingListType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShoppingListTypeToCollectionIfMissing(shoppingListTypeCollection, shoppingListType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShoppingListType to an array that doesn't contain it", () => {
        const shoppingListType: IShoppingListType = sampleWithRequiredData;
        const shoppingListTypeCollection: IShoppingListType[] = [sampleWithPartialData];
        expectedResult = service.addShoppingListTypeToCollectionIfMissing(shoppingListTypeCollection, shoppingListType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shoppingListType);
      });

      it('should add only unique ShoppingListType to an array', () => {
        const shoppingListTypeArray: IShoppingListType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shoppingListTypeCollection: IShoppingListType[] = [sampleWithRequiredData];
        expectedResult = service.addShoppingListTypeToCollectionIfMissing(shoppingListTypeCollection, ...shoppingListTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shoppingListType: IShoppingListType = sampleWithRequiredData;
        const shoppingListType2: IShoppingListType = sampleWithPartialData;
        expectedResult = service.addShoppingListTypeToCollectionIfMissing([], shoppingListType, shoppingListType2);
        expect(expectedResult).toEqual([shoppingListType, shoppingListType2]);
      });

      it('should accept null and undefined values', () => {
        const shoppingListType: IShoppingListType = sampleWithRequiredData;
        expectedResult = service.addShoppingListTypeToCollectionIfMissing([], null, shoppingListType, undefined);
        expect(expectedResult).toEqual([shoppingListType]);
      });

      it('should return initial array if no ShoppingListType is added', () => {
        const shoppingListTypeCollection: IShoppingListType[] = [sampleWithRequiredData];
        expectedResult = service.addShoppingListTypeToCollectionIfMissing(shoppingListTypeCollection, undefined, null);
        expect(expectedResult).toEqual(shoppingListTypeCollection);
      });
    });

    describe('compareShoppingListType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShoppingListType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6209 };
        const entity2 = null;

        const compareResult1 = service.compareShoppingListType(entity1, entity2);
        const compareResult2 = service.compareShoppingListType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6209 };
        const entity2 = { id: 25325 };

        const compareResult1 = service.compareShoppingListType(entity1, entity2);
        const compareResult2 = service.compareShoppingListType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6209 };
        const entity2 = { id: 6209 };

        const compareResult1 = service.compareShoppingListType(entity1, entity2);
        const compareResult2 = service.compareShoppingListType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
