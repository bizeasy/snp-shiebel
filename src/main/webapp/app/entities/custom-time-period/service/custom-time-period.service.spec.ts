import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { ICustomTimePeriod } from '../custom-time-period.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../custom-time-period.test-samples';

import { CustomTimePeriodService, RestCustomTimePeriod } from './custom-time-period.service';

const requireRestSample: RestCustomTimePeriod = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.format(DATE_FORMAT),
  thruDate: sampleWithRequiredData.thruDate?.format(DATE_FORMAT),
};

describe('CustomTimePeriod Service', () => {
  let service: CustomTimePeriodService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomTimePeriod | ICustomTimePeriod[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CustomTimePeriodService);
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

    it('should create a CustomTimePeriod', () => {
      const customTimePeriod = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customTimePeriod).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomTimePeriod', () => {
      const customTimePeriod = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customTimePeriod).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomTimePeriod', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomTimePeriod', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomTimePeriod', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCustomTimePeriodToCollectionIfMissing', () => {
      it('should add a CustomTimePeriod to an empty array', () => {
        const customTimePeriod: ICustomTimePeriod = sampleWithRequiredData;
        expectedResult = service.addCustomTimePeriodToCollectionIfMissing([], customTimePeriod);
        expect(expectedResult).toEqual([customTimePeriod]);
      });

      it('should not add a CustomTimePeriod to an array that contains it', () => {
        const customTimePeriod: ICustomTimePeriod = sampleWithRequiredData;
        const customTimePeriodCollection: ICustomTimePeriod[] = [
          {
            ...customTimePeriod,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomTimePeriodToCollectionIfMissing(customTimePeriodCollection, customTimePeriod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomTimePeriod to an array that doesn't contain it", () => {
        const customTimePeriod: ICustomTimePeriod = sampleWithRequiredData;
        const customTimePeriodCollection: ICustomTimePeriod[] = [sampleWithPartialData];
        expectedResult = service.addCustomTimePeriodToCollectionIfMissing(customTimePeriodCollection, customTimePeriod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customTimePeriod);
      });

      it('should add only unique CustomTimePeriod to an array', () => {
        const customTimePeriodArray: ICustomTimePeriod[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customTimePeriodCollection: ICustomTimePeriod[] = [sampleWithRequiredData];
        expectedResult = service.addCustomTimePeriodToCollectionIfMissing(customTimePeriodCollection, ...customTimePeriodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customTimePeriod: ICustomTimePeriod = sampleWithRequiredData;
        const customTimePeriod2: ICustomTimePeriod = sampleWithPartialData;
        expectedResult = service.addCustomTimePeriodToCollectionIfMissing([], customTimePeriod, customTimePeriod2);
        expect(expectedResult).toEqual([customTimePeriod, customTimePeriod2]);
      });

      it('should accept null and undefined values', () => {
        const customTimePeriod: ICustomTimePeriod = sampleWithRequiredData;
        expectedResult = service.addCustomTimePeriodToCollectionIfMissing([], null, customTimePeriod, undefined);
        expect(expectedResult).toEqual([customTimePeriod]);
      });

      it('should return initial array if no CustomTimePeriod is added', () => {
        const customTimePeriodCollection: ICustomTimePeriod[] = [sampleWithRequiredData];
        expectedResult = service.addCustomTimePeriodToCollectionIfMissing(customTimePeriodCollection, undefined, null);
        expect(expectedResult).toEqual(customTimePeriodCollection);
      });
    });

    describe('compareCustomTimePeriod', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomTimePeriod(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19478 };
        const entity2 = null;

        const compareResult1 = service.compareCustomTimePeriod(entity1, entity2);
        const compareResult2 = service.compareCustomTimePeriod(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19478 };
        const entity2 = { id: 17611 };

        const compareResult1 = service.compareCustomTimePeriod(entity1, entity2);
        const compareResult2 = service.compareCustomTimePeriod(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 19478 };
        const entity2 = { id: 19478 };

        const compareResult1 = service.compareCustomTimePeriod(entity1, entity2);
        const compareResult2 = service.compareCustomTimePeriod(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
