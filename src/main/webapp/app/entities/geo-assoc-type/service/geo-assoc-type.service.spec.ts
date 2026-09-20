import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IGeoAssocType } from '../geo-assoc-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../geo-assoc-type.test-samples';

import { GeoAssocTypeService } from './geo-assoc-type.service';

const requireRestSample: IGeoAssocType = {
  ...sampleWithRequiredData,
};

describe('GeoAssocType Service', () => {
  let service: GeoAssocTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IGeoAssocType | IGeoAssocType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GeoAssocTypeService);
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

    it('should create a GeoAssocType', () => {
      const geoAssocType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(geoAssocType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GeoAssocType', () => {
      const geoAssocType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(geoAssocType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GeoAssocType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GeoAssocType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GeoAssocType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addGeoAssocTypeToCollectionIfMissing', () => {
      it('should add a GeoAssocType to an empty array', () => {
        const geoAssocType: IGeoAssocType = sampleWithRequiredData;
        expectedResult = service.addGeoAssocTypeToCollectionIfMissing([], geoAssocType);
        expect(expectedResult).toEqual([geoAssocType]);
      });

      it('should not add a GeoAssocType to an array that contains it', () => {
        const geoAssocType: IGeoAssocType = sampleWithRequiredData;
        const geoAssocTypeCollection: IGeoAssocType[] = [
          {
            ...geoAssocType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGeoAssocTypeToCollectionIfMissing(geoAssocTypeCollection, geoAssocType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GeoAssocType to an array that doesn't contain it", () => {
        const geoAssocType: IGeoAssocType = sampleWithRequiredData;
        const geoAssocTypeCollection: IGeoAssocType[] = [sampleWithPartialData];
        expectedResult = service.addGeoAssocTypeToCollectionIfMissing(geoAssocTypeCollection, geoAssocType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(geoAssocType);
      });

      it('should add only unique GeoAssocType to an array', () => {
        const geoAssocTypeArray: IGeoAssocType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const geoAssocTypeCollection: IGeoAssocType[] = [sampleWithRequiredData];
        expectedResult = service.addGeoAssocTypeToCollectionIfMissing(geoAssocTypeCollection, ...geoAssocTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const geoAssocType: IGeoAssocType = sampleWithRequiredData;
        const geoAssocType2: IGeoAssocType = sampleWithPartialData;
        expectedResult = service.addGeoAssocTypeToCollectionIfMissing([], geoAssocType, geoAssocType2);
        expect(expectedResult).toEqual([geoAssocType, geoAssocType2]);
      });

      it('should accept null and undefined values', () => {
        const geoAssocType: IGeoAssocType = sampleWithRequiredData;
        expectedResult = service.addGeoAssocTypeToCollectionIfMissing([], null, geoAssocType, undefined);
        expect(expectedResult).toEqual([geoAssocType]);
      });

      it('should return initial array if no GeoAssocType is added', () => {
        const geoAssocTypeCollection: IGeoAssocType[] = [sampleWithRequiredData];
        expectedResult = service.addGeoAssocTypeToCollectionIfMissing(geoAssocTypeCollection, undefined, null);
        expect(expectedResult).toEqual(geoAssocTypeCollection);
      });
    });

    describe('compareGeoAssocType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGeoAssocType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9608 };
        const entity2 = null;

        const compareResult1 = service.compareGeoAssocType(entity1, entity2);
        const compareResult2 = service.compareGeoAssocType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9608 };
        const entity2 = { id: 15746 };

        const compareResult1 = service.compareGeoAssocType(entity1, entity2);
        const compareResult2 = service.compareGeoAssocType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 9608 };
        const entity2 = { id: 9608 };

        const compareResult1 = service.compareGeoAssocType(entity1, entity2);
        const compareResult2 = service.compareGeoAssocType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
