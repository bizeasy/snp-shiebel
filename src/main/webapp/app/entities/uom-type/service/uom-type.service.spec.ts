import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IUomType } from '../uom-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../uom-type.test-samples';

import { UomTypeService } from './uom-type.service';

const requireRestSample: IUomType = {
  ...sampleWithRequiredData,
};

describe('UomType Service', () => {
  let service: UomTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IUomType | IUomType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UomTypeService);
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

    it('should create a UomType', () => {
      const uomType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(uomType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UomType', () => {
      const uomType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(uomType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UomType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UomType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UomType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addUomTypeToCollectionIfMissing', () => {
      it('should add a UomType to an empty array', () => {
        const uomType: IUomType = sampleWithRequiredData;
        expectedResult = service.addUomTypeToCollectionIfMissing([], uomType);
        expect(expectedResult).toEqual([uomType]);
      });

      it('should not add a UomType to an array that contains it', () => {
        const uomType: IUomType = sampleWithRequiredData;
        const uomTypeCollection: IUomType[] = [
          {
            ...uomType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUomTypeToCollectionIfMissing(uomTypeCollection, uomType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UomType to an array that doesn't contain it", () => {
        const uomType: IUomType = sampleWithRequiredData;
        const uomTypeCollection: IUomType[] = [sampleWithPartialData];
        expectedResult = service.addUomTypeToCollectionIfMissing(uomTypeCollection, uomType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uomType);
      });

      it('should add only unique UomType to an array', () => {
        const uomTypeArray: IUomType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const uomTypeCollection: IUomType[] = [sampleWithRequiredData];
        expectedResult = service.addUomTypeToCollectionIfMissing(uomTypeCollection, ...uomTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const uomType: IUomType = sampleWithRequiredData;
        const uomType2: IUomType = sampleWithPartialData;
        expectedResult = service.addUomTypeToCollectionIfMissing([], uomType, uomType2);
        expect(expectedResult).toEqual([uomType, uomType2]);
      });

      it('should accept null and undefined values', () => {
        const uomType: IUomType = sampleWithRequiredData;
        expectedResult = service.addUomTypeToCollectionIfMissing([], null, uomType, undefined);
        expect(expectedResult).toEqual([uomType]);
      });

      it('should return initial array if no UomType is added', () => {
        const uomTypeCollection: IUomType[] = [sampleWithRequiredData];
        expectedResult = service.addUomTypeToCollectionIfMissing(uomTypeCollection, undefined, null);
        expect(expectedResult).toEqual(uomTypeCollection);
      });
    });

    describe('compareUomType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUomType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28679 };
        const entity2 = null;

        const compareResult1 = service.compareUomType(entity1, entity2);
        const compareResult2 = service.compareUomType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28679 };
        const entity2 = { id: 18459 };

        const compareResult1 = service.compareUomType(entity1, entity2);
        const compareResult2 = service.compareUomType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28679 };
        const entity2 = { id: 28679 };

        const compareResult1 = service.compareUomType(entity1, entity2);
        const compareResult2 = service.compareUomType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
