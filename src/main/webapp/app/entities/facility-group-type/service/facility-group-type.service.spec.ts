import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacilityGroupType } from '../facility-group-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../facility-group-type.test-samples';

import { FacilityGroupTypeService } from './facility-group-type.service';

const requireRestSample: IFacilityGroupType = {
  ...sampleWithRequiredData,
};

describe('FacilityGroupType Service', () => {
  let service: FacilityGroupTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacilityGroupType | IFacilityGroupType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityGroupTypeService);
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

    it('should create a FacilityGroupType', () => {
      const facilityGroupType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facilityGroupType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacilityGroupType', () => {
      const facilityGroupType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facilityGroupType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacilityGroupType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacilityGroupType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacilityGroupType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityGroupTypeToCollectionIfMissing', () => {
      it('should add a FacilityGroupType to an empty array', () => {
        const facilityGroupType: IFacilityGroupType = sampleWithRequiredData;
        expectedResult = service.addFacilityGroupTypeToCollectionIfMissing([], facilityGroupType);
        expect(expectedResult).toEqual([facilityGroupType]);
      });

      it('should not add a FacilityGroupType to an array that contains it', () => {
        const facilityGroupType: IFacilityGroupType = sampleWithRequiredData;
        const facilityGroupTypeCollection: IFacilityGroupType[] = [
          {
            ...facilityGroupType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityGroupTypeToCollectionIfMissing(facilityGroupTypeCollection, facilityGroupType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacilityGroupType to an array that doesn't contain it", () => {
        const facilityGroupType: IFacilityGroupType = sampleWithRequiredData;
        const facilityGroupTypeCollection: IFacilityGroupType[] = [sampleWithPartialData];
        expectedResult = service.addFacilityGroupTypeToCollectionIfMissing(facilityGroupTypeCollection, facilityGroupType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facilityGroupType);
      });

      it('should add only unique FacilityGroupType to an array', () => {
        const facilityGroupTypeArray: IFacilityGroupType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facilityGroupTypeCollection: IFacilityGroupType[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityGroupTypeToCollectionIfMissing(facilityGroupTypeCollection, ...facilityGroupTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facilityGroupType: IFacilityGroupType = sampleWithRequiredData;
        const facilityGroupType2: IFacilityGroupType = sampleWithPartialData;
        expectedResult = service.addFacilityGroupTypeToCollectionIfMissing([], facilityGroupType, facilityGroupType2);
        expect(expectedResult).toEqual([facilityGroupType, facilityGroupType2]);
      });

      it('should accept null and undefined values', () => {
        const facilityGroupType: IFacilityGroupType = sampleWithRequiredData;
        expectedResult = service.addFacilityGroupTypeToCollectionIfMissing([], null, facilityGroupType, undefined);
        expect(expectedResult).toEqual([facilityGroupType]);
      });

      it('should return initial array if no FacilityGroupType is added', () => {
        const facilityGroupTypeCollection: IFacilityGroupType[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityGroupTypeToCollectionIfMissing(facilityGroupTypeCollection, undefined, null);
        expect(expectedResult).toEqual(facilityGroupTypeCollection);
      });
    });

    describe('compareFacilityGroupType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacilityGroupType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7510 };
        const entity2 = null;

        const compareResult1 = service.compareFacilityGroupType(entity1, entity2);
        const compareResult2 = service.compareFacilityGroupType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7510 };
        const entity2 = { id: 23372 };

        const compareResult1 = service.compareFacilityGroupType(entity1, entity2);
        const compareResult2 = service.compareFacilityGroupType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7510 };
        const entity2 = { id: 7510 };

        const compareResult1 = service.compareFacilityGroupType(entity1, entity2);
        const compareResult2 = service.compareFacilityGroupType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
