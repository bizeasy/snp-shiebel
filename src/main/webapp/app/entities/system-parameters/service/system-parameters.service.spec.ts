import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ISystemParameters } from '../system-parameters.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../system-parameters.test-samples';

import { SystemParametersService } from './system-parameters.service';

const requireRestSample: ISystemParameters = {
  ...sampleWithRequiredData,
};

describe('SystemParameters Service', () => {
  let service: SystemParametersService;
  let httpMock: HttpTestingController;
  let expectedResult: ISystemParameters | ISystemParameters[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SystemParametersService);
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

    it('should create a SystemParameters', () => {
      const systemParameters = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(systemParameters).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemParameters', () => {
      const systemParameters = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(systemParameters).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemParameters', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemParameters', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SystemParameters', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addSystemParametersToCollectionIfMissing', () => {
      it('should add a SystemParameters to an empty array', () => {
        const systemParameters: ISystemParameters = sampleWithRequiredData;
        expectedResult = service.addSystemParametersToCollectionIfMissing([], systemParameters);
        expect(expectedResult).toEqual([systemParameters]);
      });

      it('should not add a SystemParameters to an array that contains it', () => {
        const systemParameters: ISystemParameters = sampleWithRequiredData;
        const systemParametersCollection: ISystemParameters[] = [
          {
            ...systemParameters,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, systemParameters);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemParameters to an array that doesn't contain it", () => {
        const systemParameters: ISystemParameters = sampleWithRequiredData;
        const systemParametersCollection: ISystemParameters[] = [sampleWithPartialData];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, systemParameters);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemParameters);
      });

      it('should add only unique SystemParameters to an array', () => {
        const systemParametersArray: ISystemParameters[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const systemParametersCollection: ISystemParameters[] = [sampleWithRequiredData];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, ...systemParametersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemParameters: ISystemParameters = sampleWithRequiredData;
        const systemParameters2: ISystemParameters = sampleWithPartialData;
        expectedResult = service.addSystemParametersToCollectionIfMissing([], systemParameters, systemParameters2);
        expect(expectedResult).toEqual([systemParameters, systemParameters2]);
      });

      it('should accept null and undefined values', () => {
        const systemParameters: ISystemParameters = sampleWithRequiredData;
        expectedResult = service.addSystemParametersToCollectionIfMissing([], null, systemParameters, undefined);
        expect(expectedResult).toEqual([systemParameters]);
      });

      it('should return initial array if no SystemParameters is added', () => {
        const systemParametersCollection: ISystemParameters[] = [sampleWithRequiredData];
        expectedResult = service.addSystemParametersToCollectionIfMissing(systemParametersCollection, undefined, null);
        expect(expectedResult).toEqual(systemParametersCollection);
      });
    });

    describe('compareSystemParameters', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSystemParameters(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7684 };
        const entity2 = null;

        const compareResult1 = service.compareSystemParameters(entity1, entity2);
        const compareResult2 = service.compareSystemParameters(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7684 };
        const entity2 = { id: 594 };

        const compareResult1 = service.compareSystemParameters(entity1, entity2);
        const compareResult2 = service.compareSystemParameters(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7684 };
        const entity2 = { id: 7684 };

        const compareResult1 = service.compareSystemParameters(entity1, entity2);
        const compareResult2 = service.compareSystemParameters(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
