import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IUom } from '../uom.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../uom.test-samples';

import { UomService } from './uom.service';

const requireRestSample: IUom = {
  ...sampleWithRequiredData,
};

describe('Uom Service', () => {
  let service: UomService;
  let httpMock: HttpTestingController;
  let expectedResult: IUom | IUom[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UomService);
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

    it('should create a Uom', () => {
      const uom = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(uom).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Uom', () => {
      const uom = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(uom).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Uom', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Uom', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Uom', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addUomToCollectionIfMissing', () => {
      it('should add a Uom to an empty array', () => {
        const uom: IUom = sampleWithRequiredData;
        expectedResult = service.addUomToCollectionIfMissing([], uom);
        expect(expectedResult).toEqual([uom]);
      });

      it('should not add a Uom to an array that contains it', () => {
        const uom: IUom = sampleWithRequiredData;
        const uomCollection: IUom[] = [
          {
            ...uom,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUomToCollectionIfMissing(uomCollection, uom);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Uom to an array that doesn't contain it", () => {
        const uom: IUom = sampleWithRequiredData;
        const uomCollection: IUom[] = [sampleWithPartialData];
        expectedResult = service.addUomToCollectionIfMissing(uomCollection, uom);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uom);
      });

      it('should add only unique Uom to an array', () => {
        const uomArray: IUom[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const uomCollection: IUom[] = [sampleWithRequiredData];
        expectedResult = service.addUomToCollectionIfMissing(uomCollection, ...uomArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const uom: IUom = sampleWithRequiredData;
        const uom2: IUom = sampleWithPartialData;
        expectedResult = service.addUomToCollectionIfMissing([], uom, uom2);
        expect(expectedResult).toEqual([uom, uom2]);
      });

      it('should accept null and undefined values', () => {
        const uom: IUom = sampleWithRequiredData;
        expectedResult = service.addUomToCollectionIfMissing([], null, uom, undefined);
        expect(expectedResult).toEqual([uom]);
      });

      it('should return initial array if no Uom is added', () => {
        const uomCollection: IUom[] = [sampleWithRequiredData];
        expectedResult = service.addUomToCollectionIfMissing(uomCollection, undefined, null);
        expect(expectedResult).toEqual(uomCollection);
      });
    });

    describe('compareUom', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUom(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6576 };
        const entity2 = null;

        const compareResult1 = service.compareUom(entity1, entity2);
        const compareResult2 = service.compareUom(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6576 };
        const entity2 = { id: 20168 };

        const compareResult1 = service.compareUom(entity1, entity2);
        const compareResult2 = service.compareUom(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6576 };
        const entity2 = { id: 6576 };

        const compareResult1 = service.compareUom(entity1, entity2);
        const compareResult2 = service.compareUom(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
