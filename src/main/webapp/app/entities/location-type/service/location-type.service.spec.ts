import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ILocationType } from '../location-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../location-type.test-samples';

import { LocationTypeService } from './location-type.service';

const requireRestSample: ILocationType = {
  ...sampleWithRequiredData,
};

describe('LocationType Service', () => {
  let service: LocationTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ILocationType | ILocationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LocationTypeService);
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

    it('should create a LocationType', () => {
      const locationType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(locationType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LocationType', () => {
      const locationType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(locationType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LocationType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LocationType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LocationType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addLocationTypeToCollectionIfMissing', () => {
      it('should add a LocationType to an empty array', () => {
        const locationType: ILocationType = sampleWithRequiredData;
        expectedResult = service.addLocationTypeToCollectionIfMissing([], locationType);
        expect(expectedResult).toEqual([locationType]);
      });

      it('should not add a LocationType to an array that contains it', () => {
        const locationType: ILocationType = sampleWithRequiredData;
        const locationTypeCollection: ILocationType[] = [
          {
            ...locationType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLocationTypeToCollectionIfMissing(locationTypeCollection, locationType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LocationType to an array that doesn't contain it", () => {
        const locationType: ILocationType = sampleWithRequiredData;
        const locationTypeCollection: ILocationType[] = [sampleWithPartialData];
        expectedResult = service.addLocationTypeToCollectionIfMissing(locationTypeCollection, locationType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locationType);
      });

      it('should add only unique LocationType to an array', () => {
        const locationTypeArray: ILocationType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const locationTypeCollection: ILocationType[] = [sampleWithRequiredData];
        expectedResult = service.addLocationTypeToCollectionIfMissing(locationTypeCollection, ...locationTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const locationType: ILocationType = sampleWithRequiredData;
        const locationType2: ILocationType = sampleWithPartialData;
        expectedResult = service.addLocationTypeToCollectionIfMissing([], locationType, locationType2);
        expect(expectedResult).toEqual([locationType, locationType2]);
      });

      it('should accept null and undefined values', () => {
        const locationType: ILocationType = sampleWithRequiredData;
        expectedResult = service.addLocationTypeToCollectionIfMissing([], null, locationType, undefined);
        expect(expectedResult).toEqual([locationType]);
      });

      it('should return initial array if no LocationType is added', () => {
        const locationTypeCollection: ILocationType[] = [sampleWithRequiredData];
        expectedResult = service.addLocationTypeToCollectionIfMissing(locationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(locationTypeCollection);
      });
    });

    describe('compareLocationType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLocationType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9674 };
        const entity2 = null;

        const compareResult1 = service.compareLocationType(entity1, entity2);
        const compareResult2 = service.compareLocationType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9674 };
        const entity2 = { id: 23011 };

        const compareResult1 = service.compareLocationType(entity1, entity2);
        const compareResult2 = service.compareLocationType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 9674 };
        const entity2 = { id: 9674 };

        const compareResult1 = service.compareLocationType(entity1, entity2);
        const compareResult2 = service.compareLocationType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
