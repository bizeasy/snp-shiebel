import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShoppingListItem } from '../shopping-list-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shopping-list-item.test-samples';

import { RestShoppingListItem, ShoppingListItemService } from './shopping-list-item.service';

const requireRestSample: RestShoppingListItem = {
  ...sampleWithRequiredData,
  requiredByDate: sampleWithRequiredData.requiredByDate?.toJSON(),
};

describe('ShoppingListItem Service', () => {
  let service: ShoppingListItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IShoppingListItem | IShoppingListItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShoppingListItemService);
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

    it('should create a ShoppingListItem', () => {
      const shoppingListItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shoppingListItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShoppingListItem', () => {
      const shoppingListItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shoppingListItem).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShoppingListItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShoppingListItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShoppingListItem', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShoppingListItemToCollectionIfMissing', () => {
      it('should add a ShoppingListItem to an empty array', () => {
        const shoppingListItem: IShoppingListItem = sampleWithRequiredData;
        expectedResult = service.addShoppingListItemToCollectionIfMissing([], shoppingListItem);
        expect(expectedResult).toEqual([shoppingListItem]);
      });

      it('should not add a ShoppingListItem to an array that contains it', () => {
        const shoppingListItem: IShoppingListItem = sampleWithRequiredData;
        const shoppingListItemCollection: IShoppingListItem[] = [
          {
            ...shoppingListItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShoppingListItemToCollectionIfMissing(shoppingListItemCollection, shoppingListItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShoppingListItem to an array that doesn't contain it", () => {
        const shoppingListItem: IShoppingListItem = sampleWithRequiredData;
        const shoppingListItemCollection: IShoppingListItem[] = [sampleWithPartialData];
        expectedResult = service.addShoppingListItemToCollectionIfMissing(shoppingListItemCollection, shoppingListItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shoppingListItem);
      });

      it('should add only unique ShoppingListItem to an array', () => {
        const shoppingListItemArray: IShoppingListItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shoppingListItemCollection: IShoppingListItem[] = [sampleWithRequiredData];
        expectedResult = service.addShoppingListItemToCollectionIfMissing(shoppingListItemCollection, ...shoppingListItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shoppingListItem: IShoppingListItem = sampleWithRequiredData;
        const shoppingListItem2: IShoppingListItem = sampleWithPartialData;
        expectedResult = service.addShoppingListItemToCollectionIfMissing([], shoppingListItem, shoppingListItem2);
        expect(expectedResult).toEqual([shoppingListItem, shoppingListItem2]);
      });

      it('should accept null and undefined values', () => {
        const shoppingListItem: IShoppingListItem = sampleWithRequiredData;
        expectedResult = service.addShoppingListItemToCollectionIfMissing([], null, shoppingListItem, undefined);
        expect(expectedResult).toEqual([shoppingListItem]);
      });

      it('should return initial array if no ShoppingListItem is added', () => {
        const shoppingListItemCollection: IShoppingListItem[] = [sampleWithRequiredData];
        expectedResult = service.addShoppingListItemToCollectionIfMissing(shoppingListItemCollection, undefined, null);
        expect(expectedResult).toEqual(shoppingListItemCollection);
      });
    });

    describe('compareShoppingListItem', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShoppingListItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26675 };
        const entity2 = null;

        const compareResult1 = service.compareShoppingListItem(entity1, entity2);
        const compareResult2 = service.compareShoppingListItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26675 };
        const entity2 = { id: 6768 };

        const compareResult1 = service.compareShoppingListItem(entity1, entity2);
        const compareResult2 = service.compareShoppingListItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 26675 };
        const entity2 = { id: 26675 };

        const compareResult1 = service.compareShoppingListItem(entity1, entity2);
        const compareResult2 = service.compareShoppingListItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
