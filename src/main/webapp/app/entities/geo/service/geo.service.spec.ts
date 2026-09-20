import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IGeo } from '../geo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../geo.test-samples';

import { GeoService } from './geo.service';

const requireRestSample: IGeo = {
  ...sampleWithRequiredData,
};

describe('Geo Service', () => {
  let service: GeoService;
  let httpMock: HttpTestingController;
  let expectedResult: IGeo | IGeo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GeoService);
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

    it('should create a Geo', () => {
      const geo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(geo).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Geo', () => {
      const geo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(geo).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Geo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Geo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Geo', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addGeoToCollectionIfMissing', () => {
      it('should add a Geo to an empty array', () => {
        const geo: IGeo = sampleWithRequiredData;
        expectedResult = service.addGeoToCollectionIfMissing([], geo);
        expect(expectedResult).toEqual([geo]);
      });

      it('should not add a Geo to an array that contains it', () => {
        const geo: IGeo = sampleWithRequiredData;
        const geoCollection: IGeo[] = [
          {
            ...geo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGeoToCollectionIfMissing(geoCollection, geo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Geo to an array that doesn't contain it", () => {
        const geo: IGeo = sampleWithRequiredData;
        const geoCollection: IGeo[] = [sampleWithPartialData];
        expectedResult = service.addGeoToCollectionIfMissing(geoCollection, geo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(geo);
      });

      it('should add only unique Geo to an array', () => {
        const geoArray: IGeo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const geoCollection: IGeo[] = [sampleWithRequiredData];
        expectedResult = service.addGeoToCollectionIfMissing(geoCollection, ...geoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const geo: IGeo = sampleWithRequiredData;
        const geo2: IGeo = sampleWithPartialData;
        expectedResult = service.addGeoToCollectionIfMissing([], geo, geo2);
        expect(expectedResult).toEqual([geo, geo2]);
      });

      it('should accept null and undefined values', () => {
        const geo: IGeo = sampleWithRequiredData;
        expectedResult = service.addGeoToCollectionIfMissing([], null, geo, undefined);
        expect(expectedResult).toEqual([geo]);
      });

      it('should return initial array if no Geo is added', () => {
        const geoCollection: IGeo[] = [sampleWithRequiredData];
        expectedResult = service.addGeoToCollectionIfMissing(geoCollection, undefined, null);
        expect(expectedResult).toEqual(geoCollection);
      });
    });

    describe('compareGeo', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGeo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 16292 };
        const entity2 = null;

        const compareResult1 = service.compareGeo(entity1, entity2);
        const compareResult2 = service.compareGeo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 16292 };
        const entity2 = { id: 23406 };

        const compareResult1 = service.compareGeo(entity1, entity2);
        const compareResult2 = service.compareGeo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 16292 };
        const entity2 = { id: 16292 };

        const compareResult1 = service.compareGeo(entity1, entity2);
        const compareResult2 = service.compareGeo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
