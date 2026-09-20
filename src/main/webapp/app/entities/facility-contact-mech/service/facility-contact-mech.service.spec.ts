import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFacilityContactMech } from '../facility-contact-mech.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../facility-contact-mech.test-samples';

import { FacilityContactMechService, RestFacilityContactMech } from './facility-contact-mech.service';

const requireRestSample: RestFacilityContactMech = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('FacilityContactMech Service', () => {
  let service: FacilityContactMechService;
  let httpMock: HttpTestingController;
  let expectedResult: IFacilityContactMech | IFacilityContactMech[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FacilityContactMechService);
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

    it('should create a FacilityContactMech', () => {
      const facilityContactMech = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(facilityContactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FacilityContactMech', () => {
      const facilityContactMech = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(facilityContactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FacilityContactMech', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FacilityContactMech', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FacilityContactMech', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFacilityContactMechToCollectionIfMissing', () => {
      it('should add a FacilityContactMech to an empty array', () => {
        const facilityContactMech: IFacilityContactMech = sampleWithRequiredData;
        expectedResult = service.addFacilityContactMechToCollectionIfMissing([], facilityContactMech);
        expect(expectedResult).toEqual([facilityContactMech]);
      });

      it('should not add a FacilityContactMech to an array that contains it', () => {
        const facilityContactMech: IFacilityContactMech = sampleWithRequiredData;
        const facilityContactMechCollection: IFacilityContactMech[] = [
          {
            ...facilityContactMech,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFacilityContactMechToCollectionIfMissing(facilityContactMechCollection, facilityContactMech);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FacilityContactMech to an array that doesn't contain it", () => {
        const facilityContactMech: IFacilityContactMech = sampleWithRequiredData;
        const facilityContactMechCollection: IFacilityContactMech[] = [sampleWithPartialData];
        expectedResult = service.addFacilityContactMechToCollectionIfMissing(facilityContactMechCollection, facilityContactMech);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(facilityContactMech);
      });

      it('should add only unique FacilityContactMech to an array', () => {
        const facilityContactMechArray: IFacilityContactMech[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const facilityContactMechCollection: IFacilityContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityContactMechToCollectionIfMissing(facilityContactMechCollection, ...facilityContactMechArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const facilityContactMech: IFacilityContactMech = sampleWithRequiredData;
        const facilityContactMech2: IFacilityContactMech = sampleWithPartialData;
        expectedResult = service.addFacilityContactMechToCollectionIfMissing([], facilityContactMech, facilityContactMech2);
        expect(expectedResult).toEqual([facilityContactMech, facilityContactMech2]);
      });

      it('should accept null and undefined values', () => {
        const facilityContactMech: IFacilityContactMech = sampleWithRequiredData;
        expectedResult = service.addFacilityContactMechToCollectionIfMissing([], null, facilityContactMech, undefined);
        expect(expectedResult).toEqual([facilityContactMech]);
      });

      it('should return initial array if no FacilityContactMech is added', () => {
        const facilityContactMechCollection: IFacilityContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addFacilityContactMechToCollectionIfMissing(facilityContactMechCollection, undefined, null);
        expect(expectedResult).toEqual(facilityContactMechCollection);
      });
    });

    describe('compareFacilityContactMech', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFacilityContactMech(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21991 };
        const entity2 = null;

        const compareResult1 = service.compareFacilityContactMech(entity1, entity2);
        const compareResult2 = service.compareFacilityContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21991 };
        const entity2 = { id: 18977 };

        const compareResult1 = service.compareFacilityContactMech(entity1, entity2);
        const compareResult2 = service.compareFacilityContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 21991 };
        const entity2 = { id: 21991 };

        const compareResult1 = service.compareFacilityContactMech(entity1, entity2);
        const compareResult2 = service.compareFacilityContactMech(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
