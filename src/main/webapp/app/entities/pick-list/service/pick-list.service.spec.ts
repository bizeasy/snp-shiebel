import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPickList } from '../pick-list.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../pick-list.test-samples';

import { PickListService, RestPickList } from './pick-list.service';

const requireRestSample: RestPickList = {
  ...sampleWithRequiredData,
  pickListDate: sampleWithRequiredData.pickListDate?.toJSON(),
};

describe('PickList Service', () => {
  let service: PickListService;
  let httpMock: HttpTestingController;
  let expectedResult: IPickList | IPickList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PickListService);
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

    it('should create a PickList', () => {
      const pickList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pickList).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PickList', () => {
      const pickList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pickList).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PickList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PickList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PickList', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPickListToCollectionIfMissing', () => {
      it('should add a PickList to an empty array', () => {
        const pickList: IPickList = sampleWithRequiredData;
        expectedResult = service.addPickListToCollectionIfMissing([], pickList);
        expect(expectedResult).toEqual([pickList]);
      });

      it('should not add a PickList to an array that contains it', () => {
        const pickList: IPickList = sampleWithRequiredData;
        const pickListCollection: IPickList[] = [
          {
            ...pickList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPickListToCollectionIfMissing(pickListCollection, pickList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PickList to an array that doesn't contain it", () => {
        const pickList: IPickList = sampleWithRequiredData;
        const pickListCollection: IPickList[] = [sampleWithPartialData];
        expectedResult = service.addPickListToCollectionIfMissing(pickListCollection, pickList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pickList);
      });

      it('should add only unique PickList to an array', () => {
        const pickListArray: IPickList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pickListCollection: IPickList[] = [sampleWithRequiredData];
        expectedResult = service.addPickListToCollectionIfMissing(pickListCollection, ...pickListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pickList: IPickList = sampleWithRequiredData;
        const pickList2: IPickList = sampleWithPartialData;
        expectedResult = service.addPickListToCollectionIfMissing([], pickList, pickList2);
        expect(expectedResult).toEqual([pickList, pickList2]);
      });

      it('should accept null and undefined values', () => {
        const pickList: IPickList = sampleWithRequiredData;
        expectedResult = service.addPickListToCollectionIfMissing([], null, pickList, undefined);
        expect(expectedResult).toEqual([pickList]);
      });

      it('should return initial array if no PickList is added', () => {
        const pickListCollection: IPickList[] = [sampleWithRequiredData];
        expectedResult = service.addPickListToCollectionIfMissing(pickListCollection, undefined, null);
        expect(expectedResult).toEqual(pickListCollection);
      });
    });

    describe('comparePickList', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePickList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15448 };
        const entity2 = null;

        const compareResult1 = service.comparePickList(entity1, entity2);
        const compareResult2 = service.comparePickList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15448 };
        const entity2 = { id: 863 };

        const compareResult1 = service.comparePickList(entity1, entity2);
        const compareResult2 = service.comparePickList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 15448 };
        const entity2 = { id: 15448 };

        const compareResult1 = service.comparePickList(entity1, entity2);
        const compareResult2 = service.comparePickList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
