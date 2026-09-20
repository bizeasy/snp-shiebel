import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacilityGroup } from '../facility-group.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../facility-group.test-samples';

import { FacilityGroupService } from './facility-group.service';

const requireRestSample: IFacilityGroup = {
  ...sampleWithRequiredData,
};

describe('FacilityGroup Service', () => {
  let service: FacilityGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacilityGroup | IFacilityGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityGroupService);
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

    it('should create a FacilityGroup', () => {
      const facilityGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facilityGroup).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacilityGroup', () => {
      const facilityGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facilityGroup).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacilityGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacilityGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacilityGroup', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityGroupToCollectionIfMissing', () => {
      it('should add a FacilityGroup to an empty array', () => {
        const facilityGroup: IFacilityGroup = sampleWithRequiredData;
        expectedResult = service.addFacilityGroupToCollectionIfMissing([], facilityGroup);
        expect(expectedResult).toEqual([facilityGroup]);
      });

      it('should not add a FacilityGroup to an array that contains it', () => {
        const facilityGroup: IFacilityGroup = sampleWithRequiredData;
        const facilityGroupCollection: IFacilityGroup[] = [
          {
            ...facilityGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityGroupToCollectionIfMissing(facilityGroupCollection, facilityGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacilityGroup to an array that doesn't contain it", () => {
        const facilityGroup: IFacilityGroup = sampleWithRequiredData;
        const facilityGroupCollection: IFacilityGroup[] = [sampleWithPartialData];
        expectedResult = service.addFacilityGroupToCollectionIfMissing(facilityGroupCollection, facilityGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facilityGroup);
      });

      it('should add only unique FacilityGroup to an array', () => {
        const facilityGroupArray: IFacilityGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facilityGroupCollection: IFacilityGroup[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityGroupToCollectionIfMissing(facilityGroupCollection, ...facilityGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facilityGroup: IFacilityGroup = sampleWithRequiredData;
        const facilityGroup2: IFacilityGroup = sampleWithPartialData;
        expectedResult = service.addFacilityGroupToCollectionIfMissing([], facilityGroup, facilityGroup2);
        expect(expectedResult).toEqual([facilityGroup, facilityGroup2]);
      });

      it('should accept null and undefined values', () => {
        const facilityGroup: IFacilityGroup = sampleWithRequiredData;
        expectedResult = service.addFacilityGroupToCollectionIfMissing([], null, facilityGroup, undefined);
        expect(expectedResult).toEqual([facilityGroup]);
      });

      it('should return initial array if no FacilityGroup is added', () => {
        const facilityGroupCollection: IFacilityGroup[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityGroupToCollectionIfMissing(facilityGroupCollection, undefined, null);
        expect(expectedResult).toEqual(facilityGroupCollection);
      });
    });

    describe('compareFacilityGroup', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacilityGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19761 };
        const entity2 = null;

        const compareResult1 = service.compareFacilityGroup(entity1, entity2);
        const compareResult2 = service.compareFacilityGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19761 };
        const entity2 = { id: 10499 };

        const compareResult1 = service.compareFacilityGroup(entity1, entity2);
        const compareResult2 = service.compareFacilityGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 19761 };
        const entity2 = { id: 19761 };

        const compareResult1 = service.compareFacilityGroup(entity1, entity2);
        const compareResult2 = service.compareFacilityGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
