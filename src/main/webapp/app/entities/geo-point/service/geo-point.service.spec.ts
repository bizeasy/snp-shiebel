import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IGeoPoint } from '../geo-point.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../geo-point.test-samples';

import { GeoPointService } from './geo-point.service';

const requireRestSample: IGeoPoint = {
  ...sampleWithRequiredData,
};

describe('GeoPoint Service', () => {
  let service: GeoPointService;
  let httpMock: HttpTestingController;
  let expectedResult: IGeoPoint | IGeoPoint[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GeoPointService);
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

    it('should create a GeoPoint', () => {
      const geoPoint = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(geoPoint).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GeoPoint', () => {
      const geoPoint = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(geoPoint).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GeoPoint', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GeoPoint', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GeoPoint', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addGeoPointToCollectionIfMissing', () => {
      it('should add a GeoPoint to an empty array', () => {
        const geoPoint: IGeoPoint = sampleWithRequiredData;
        expectedResult = service.addGeoPointToCollectionIfMissing([], geoPoint);
        expect(expectedResult).toEqual([geoPoint]);
      });

      it('should not add a GeoPoint to an array that contains it', () => {
        const geoPoint: IGeoPoint = sampleWithRequiredData;
        const geoPointCollection: IGeoPoint[] = [
          {
            ...geoPoint,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGeoPointToCollectionIfMissing(geoPointCollection, geoPoint);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GeoPoint to an array that doesn't contain it", () => {
        const geoPoint: IGeoPoint = sampleWithRequiredData;
        const geoPointCollection: IGeoPoint[] = [sampleWithPartialData];
        expectedResult = service.addGeoPointToCollectionIfMissing(geoPointCollection, geoPoint);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(geoPoint);
      });

      it('should add only unique GeoPoint to an array', () => {
        const geoPointArray: IGeoPoint[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const geoPointCollection: IGeoPoint[] = [sampleWithRequiredData];
        expectedResult = service.addGeoPointToCollectionIfMissing(geoPointCollection, ...geoPointArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const geoPoint: IGeoPoint = sampleWithRequiredData;
        const geoPoint2: IGeoPoint = sampleWithPartialData;
        expectedResult = service.addGeoPointToCollectionIfMissing([], geoPoint, geoPoint2);
        expect(expectedResult).toEqual([geoPoint, geoPoint2]);
      });

      it('should accept null and undefined values', () => {
        const geoPoint: IGeoPoint = sampleWithRequiredData;
        expectedResult = service.addGeoPointToCollectionIfMissing([], null, geoPoint, undefined);
        expect(expectedResult).toEqual([geoPoint]);
      });

      it('should return initial array if no GeoPoint is added', () => {
        const geoPointCollection: IGeoPoint[] = [sampleWithRequiredData];
        expectedResult = service.addGeoPointToCollectionIfMissing(geoPointCollection, undefined, null);
        expect(expectedResult).toEqual(geoPointCollection);
      });
    });

    describe('compareGeoPoint', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGeoPoint(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30719 };
        const entity2 = null;

        const compareResult1 = service.compareGeoPoint(entity1, entity2);
        const compareResult2 = service.compareGeoPoint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30719 };
        const entity2 = { id: 8540 };

        const compareResult1 = service.compareGeoPoint(entity1, entity2);
        const compareResult2 = service.compareGeoPoint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 30719 };
        const entity2 = { id: 30719 };

        const compareResult1 = service.compareGeoPoint(entity1, entity2);
        const compareResult2 = service.compareGeoPoint(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
