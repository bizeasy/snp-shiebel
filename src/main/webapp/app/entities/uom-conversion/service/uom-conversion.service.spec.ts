import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IUomConversion } from '../uom-conversion.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../uom-conversion.test-samples';

import { RestUomConversion, UomConversionService } from './uom-conversion.service';

const requireRestSample: RestUomConversion = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  toDate: sampleWithRequiredData.toDate?.toJSON(),
};

describe('UomConversion Service', () => {
  let service: UomConversionService;
  let httpMock: HttpTestingController;
  let expectedResult: IUomConversion | IUomConversion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UomConversionService);
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

    it('should create a UomConversion', () => {
      const uomConversion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(uomConversion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UomConversion', () => {
      const uomConversion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(uomConversion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UomConversion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UomConversion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UomConversion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addUomConversionToCollectionIfMissing', () => {
      it('should add a UomConversion to an empty array', () => {
        const uomConversion: IUomConversion = sampleWithRequiredData;
        expectedResult = service.addUomConversionToCollectionIfMissing([], uomConversion);
        expect(expectedResult).toEqual([uomConversion]);
      });

      it('should not add a UomConversion to an array that contains it', () => {
        const uomConversion: IUomConversion = sampleWithRequiredData;
        const uomConversionCollection: IUomConversion[] = [
          {
            ...uomConversion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUomConversionToCollectionIfMissing(uomConversionCollection, uomConversion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UomConversion to an array that doesn't contain it", () => {
        const uomConversion: IUomConversion = sampleWithRequiredData;
        const uomConversionCollection: IUomConversion[] = [sampleWithPartialData];
        expectedResult = service.addUomConversionToCollectionIfMissing(uomConversionCollection, uomConversion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uomConversion);
      });

      it('should add only unique UomConversion to an array', () => {
        const uomConversionArray: IUomConversion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const uomConversionCollection: IUomConversion[] = [sampleWithRequiredData];
        expectedResult = service.addUomConversionToCollectionIfMissing(uomConversionCollection, ...uomConversionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const uomConversion: IUomConversion = sampleWithRequiredData;
        const uomConversion2: IUomConversion = sampleWithPartialData;
        expectedResult = service.addUomConversionToCollectionIfMissing([], uomConversion, uomConversion2);
        expect(expectedResult).toEqual([uomConversion, uomConversion2]);
      });

      it('should accept null and undefined values', () => {
        const uomConversion: IUomConversion = sampleWithRequiredData;
        expectedResult = service.addUomConversionToCollectionIfMissing([], null, uomConversion, undefined);
        expect(expectedResult).toEqual([uomConversion]);
      });

      it('should return initial array if no UomConversion is added', () => {
        const uomConversionCollection: IUomConversion[] = [sampleWithRequiredData];
        expectedResult = service.addUomConversionToCollectionIfMissing(uomConversionCollection, undefined, null);
        expect(expectedResult).toEqual(uomConversionCollection);
      });
    });

    describe('compareUomConversion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUomConversion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13785 };
        const entity2 = null;

        const compareResult1 = service.compareUomConversion(entity1, entity2);
        const compareResult2 = service.compareUomConversion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13785 };
        const entity2 = { id: 18422 };

        const compareResult1 = service.compareUomConversion(entity1, entity2);
        const compareResult2 = service.compareUomConversion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 13785 };
        const entity2 = { id: 13785 };

        const compareResult1 = service.compareUomConversion(entity1, entity2);
        const compareResult2 = service.compareUomConversion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
