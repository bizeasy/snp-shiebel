import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShoppingList } from '../shopping-list.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shopping-list.test-samples';

import { ShoppingListService } from './shopping-list.service';

const requireRestSample: IShoppingList = {
  ...sampleWithRequiredData,
};

describe('ShoppingList Service', () => {
  let service: ShoppingListService;
  let httpMock: HttpTestingController;
  let expectedResult: IShoppingList | IShoppingList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShoppingListService);
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

    it('should create a ShoppingList', () => {
      const shoppingList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shoppingList).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShoppingList', () => {
      const shoppingList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shoppingList).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShoppingList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShoppingList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShoppingList', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShoppingListToCollectionIfMissing', () => {
      it('should add a ShoppingList to an empty array', () => {
        const shoppingList: IShoppingList = sampleWithRequiredData;
        expectedResult = service.addShoppingListToCollectionIfMissing([], shoppingList);
        expect(expectedResult).toEqual([shoppingList]);
      });

      it('should not add a ShoppingList to an array that contains it', () => {
        const shoppingList: IShoppingList = sampleWithRequiredData;
        const shoppingListCollection: IShoppingList[] = [
          {
            ...shoppingList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShoppingListToCollectionIfMissing(shoppingListCollection, shoppingList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShoppingList to an array that doesn't contain it", () => {
        const shoppingList: IShoppingList = sampleWithRequiredData;
        const shoppingListCollection: IShoppingList[] = [sampleWithPartialData];
        expectedResult = service.addShoppingListToCollectionIfMissing(shoppingListCollection, shoppingList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shoppingList);
      });

      it('should add only unique ShoppingList to an array', () => {
        const shoppingListArray: IShoppingList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shoppingListCollection: IShoppingList[] = [sampleWithRequiredData];
        expectedResult = service.addShoppingListToCollectionIfMissing(shoppingListCollection, ...shoppingListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shoppingList: IShoppingList = sampleWithRequiredData;
        const shoppingList2: IShoppingList = sampleWithPartialData;
        expectedResult = service.addShoppingListToCollectionIfMissing([], shoppingList, shoppingList2);
        expect(expectedResult).toEqual([shoppingList, shoppingList2]);
      });

      it('should accept null and undefined values', () => {
        const shoppingList: IShoppingList = sampleWithRequiredData;
        expectedResult = service.addShoppingListToCollectionIfMissing([], null, shoppingList, undefined);
        expect(expectedResult).toEqual([shoppingList]);
      });

      it('should return initial array if no ShoppingList is added', () => {
        const shoppingListCollection: IShoppingList[] = [sampleWithRequiredData];
        expectedResult = service.addShoppingListToCollectionIfMissing(shoppingListCollection, undefined, null);
        expect(expectedResult).toEqual(shoppingListCollection);
      });
    });

    describe('compareShoppingList', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShoppingList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18255 };
        const entity2 = null;

        const compareResult1 = service.compareShoppingList(entity1, entity2);
        const compareResult2 = service.compareShoppingList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18255 };
        const entity2 = { id: 11571 };

        const compareResult1 = service.compareShoppingList(entity1, entity2);
        const compareResult2 = service.compareShoppingList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 18255 };
        const entity2 = { id: 18255 };

        const compareResult1 = service.compareShoppingList(entity1, entity2);
        const compareResult2 = service.compareShoppingList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
