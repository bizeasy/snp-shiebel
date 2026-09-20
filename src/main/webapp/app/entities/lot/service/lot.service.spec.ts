import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ILot } from '../lot.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../lot.test-samples';

import { LotService, RestLot } from './lot.service';

const requireRestSample: RestLot = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.toJSON(),
  expirationDate: sampleWithRequiredData.expirationDate?.toJSON(),
  retestDate: sampleWithRequiredData.retestDate?.toJSON(),
};

describe('Lot Service', () => {
  let service: LotService;
  let httpMock: HttpTestingController;
  let expectedResult: ILot | ILot[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LotService);
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

    it('should create a Lot', () => {
      const lot = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(lot).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lot', () => {
      const lot = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(lot).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Lot', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lot', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Lot', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addLotToCollectionIfMissing', () => {
      it('should add a Lot to an empty array', () => {
        const lot: ILot = sampleWithRequiredData;
        expectedResult = service.addLotToCollectionIfMissing([], lot);
        expect(expectedResult).toEqual([lot]);
      });

      it('should not add a Lot to an array that contains it', () => {
        const lot: ILot = sampleWithRequiredData;
        const lotCollection: ILot[] = [
          {
            ...lot,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLotToCollectionIfMissing(lotCollection, lot);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lot to an array that doesn't contain it", () => {
        const lot: ILot = sampleWithRequiredData;
        const lotCollection: ILot[] = [sampleWithPartialData];
        expectedResult = service.addLotToCollectionIfMissing(lotCollection, lot);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lot);
      });

      it('should add only unique Lot to an array', () => {
        const lotArray: ILot[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const lotCollection: ILot[] = [sampleWithRequiredData];
        expectedResult = service.addLotToCollectionIfMissing(lotCollection, ...lotArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lot: ILot = sampleWithRequiredData;
        const lot2: ILot = sampleWithPartialData;
        expectedResult = service.addLotToCollectionIfMissing([], lot, lot2);
        expect(expectedResult).toEqual([lot, lot2]);
      });

      it('should accept null and undefined values', () => {
        const lot: ILot = sampleWithRequiredData;
        expectedResult = service.addLotToCollectionIfMissing([], null, lot, undefined);
        expect(expectedResult).toEqual([lot]);
      });

      it('should return initial array if no Lot is added', () => {
        const lotCollection: ILot[] = [sampleWithRequiredData];
        expectedResult = service.addLotToCollectionIfMissing(lotCollection, undefined, null);
        expect(expectedResult).toEqual(lotCollection);
      });
    });

    describe('compareLot', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLot(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16504 };
        const entity2 = null;

        const compareResult1 = service.compareLot(entity1, entity2);
        const compareResult2 = service.compareLot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16504 };
        const entity2 = { id: 5166 };

        const compareResult1 = service.compareLot(entity1, entity2);
        const compareResult2 = service.compareLot(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 16504 };
        const entity2 = { id: 16504 };

        const compareResult1 = service.compareLot(entity1, entity2);
        const compareResult2 = service.compareLot(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
