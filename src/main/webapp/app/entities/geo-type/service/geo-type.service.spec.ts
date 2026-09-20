import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IGeoType } from '../geo-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../geo-type.test-samples';

import { GeoTypeService } from './geo-type.service';

const requireRestSample: IGeoType = {
  ...sampleWithRequiredData,
};

describe('GeoType Service', () => {
  let service: GeoTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IGeoType | IGeoType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GeoTypeService);
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

    it('should create a GeoType', () => {
      const geoType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(geoType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GeoType', () => {
      const geoType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(geoType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GeoType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GeoType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GeoType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addGeoTypeToCollectionIfMissing', () => {
      it('should add a GeoType to an empty array', () => {
        const geoType: IGeoType = sampleWithRequiredData;
        expectedResult = service.addGeoTypeToCollectionIfMissing([], geoType);
        expect(expectedResult).toEqual([geoType]);
      });

      it('should not add a GeoType to an array that contains it', () => {
        const geoType: IGeoType = sampleWithRequiredData;
        const geoTypeCollection: IGeoType[] = [
          {
            ...geoType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGeoTypeToCollectionIfMissing(geoTypeCollection, geoType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GeoType to an array that doesn't contain it", () => {
        const geoType: IGeoType = sampleWithRequiredData;
        const geoTypeCollection: IGeoType[] = [sampleWithPartialData];
        expectedResult = service.addGeoTypeToCollectionIfMissing(geoTypeCollection, geoType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(geoType);
      });

      it('should add only unique GeoType to an array', () => {
        const geoTypeArray: IGeoType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const geoTypeCollection: IGeoType[] = [sampleWithRequiredData];
        expectedResult = service.addGeoTypeToCollectionIfMissing(geoTypeCollection, ...geoTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const geoType: IGeoType = sampleWithRequiredData;
        const geoType2: IGeoType = sampleWithPartialData;
        expectedResult = service.addGeoTypeToCollectionIfMissing([], geoType, geoType2);
        expect(expectedResult).toEqual([geoType, geoType2]);
      });

      it('should accept null and undefined values', () => {
        const geoType: IGeoType = sampleWithRequiredData;
        expectedResult = service.addGeoTypeToCollectionIfMissing([], null, geoType, undefined);
        expect(expectedResult).toEqual([geoType]);
      });

      it('should return initial array if no GeoType is added', () => {
        const geoTypeCollection: IGeoType[] = [sampleWithRequiredData];
        expectedResult = service.addGeoTypeToCollectionIfMissing(geoTypeCollection, undefined, null);
        expect(expectedResult).toEqual(geoTypeCollection);
      });
    });

    describe('compareGeoType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGeoType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1453 };
        const entity2 = null;

        const compareResult1 = service.compareGeoType(entity1, entity2);
        const compareResult2 = service.compareGeoType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1453 };
        const entity2 = { id: 7532 };

        const compareResult1 = service.compareGeoType(entity1, entity2);
        const compareResult2 = service.compareGeoType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 1453 };
        const entity2 = { id: 1453 };

        const compareResult1 = service.compareGeoType(entity1, entity2);
        const compareResult2 = service.compareGeoType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
