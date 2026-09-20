import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IGeoAssoc } from '../geo-assoc.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../geo-assoc.test-samples';

import { GeoAssocService } from './geo-assoc.service';

const requireRestSample: IGeoAssoc = {
  ...sampleWithRequiredData,
};

describe('GeoAssoc Service', () => {
  let service: GeoAssocService;
  let httpMock: HttpTestingController;
  let expectedResult: IGeoAssoc | IGeoAssoc[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GeoAssocService);
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

    it('should create a GeoAssoc', () => {
      const geoAssoc = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(geoAssoc).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GeoAssoc', () => {
      const geoAssoc = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(geoAssoc).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GeoAssoc', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GeoAssoc', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GeoAssoc', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addGeoAssocToCollectionIfMissing', () => {
      it('should add a GeoAssoc to an empty array', () => {
        const geoAssoc: IGeoAssoc = sampleWithRequiredData;
        expectedResult = service.addGeoAssocToCollectionIfMissing([], geoAssoc);
        expect(expectedResult).toEqual([geoAssoc]);
      });

      it('should not add a GeoAssoc to an array that contains it', () => {
        const geoAssoc: IGeoAssoc = sampleWithRequiredData;
        const geoAssocCollection: IGeoAssoc[] = [
          {
            ...geoAssoc,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGeoAssocToCollectionIfMissing(geoAssocCollection, geoAssoc);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GeoAssoc to an array that doesn't contain it", () => {
        const geoAssoc: IGeoAssoc = sampleWithRequiredData;
        const geoAssocCollection: IGeoAssoc[] = [sampleWithPartialData];
        expectedResult = service.addGeoAssocToCollectionIfMissing(geoAssocCollection, geoAssoc);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(geoAssoc);
      });

      it('should add only unique GeoAssoc to an array', () => {
        const geoAssocArray: IGeoAssoc[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const geoAssocCollection: IGeoAssoc[] = [sampleWithRequiredData];
        expectedResult = service.addGeoAssocToCollectionIfMissing(geoAssocCollection, ...geoAssocArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const geoAssoc: IGeoAssoc = sampleWithRequiredData;
        const geoAssoc2: IGeoAssoc = sampleWithPartialData;
        expectedResult = service.addGeoAssocToCollectionIfMissing([], geoAssoc, geoAssoc2);
        expect(expectedResult).toEqual([geoAssoc, geoAssoc2]);
      });

      it('should accept null and undefined values', () => {
        const geoAssoc: IGeoAssoc = sampleWithRequiredData;
        expectedResult = service.addGeoAssocToCollectionIfMissing([], null, geoAssoc, undefined);
        expect(expectedResult).toEqual([geoAssoc]);
      });

      it('should return initial array if no GeoAssoc is added', () => {
        const geoAssocCollection: IGeoAssoc[] = [sampleWithRequiredData];
        expectedResult = service.addGeoAssocToCollectionIfMissing(geoAssocCollection, undefined, null);
        expect(expectedResult).toEqual(geoAssocCollection);
      });
    });

    describe('compareGeoAssoc', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGeoAssoc(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5823 };
        const entity2 = null;

        const compareResult1 = service.compareGeoAssoc(entity1, entity2);
        const compareResult2 = service.compareGeoAssoc(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5823 };
        const entity2 = { id: 16124 };

        const compareResult1 = service.compareGeoAssoc(entity1, entity2);
        const compareResult2 = service.compareGeoAssoc(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 5823 };
        const entity2 = { id: 5823 };

        const compareResult1 = service.compareGeoAssoc(entity1, entity2);
        const compareResult2 = service.compareGeoAssoc(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
