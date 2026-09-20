import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IStatusValidChange } from '../status-valid-change.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../status-valid-change.test-samples';

import { StatusValidChangeService } from './status-valid-change.service';

const requireRestSample: IStatusValidChange = {
  ...sampleWithRequiredData,
};

describe('StatusValidChange Service', () => {
  let service: StatusValidChangeService;
  let httpMock: HttpTestingController;
  let expectedResult: IStatusValidChange | IStatusValidChange[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StatusValidChangeService);
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

    it('should create a StatusValidChange', () => {
      const statusValidChange = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(statusValidChange).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StatusValidChange', () => {
      const statusValidChange = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(statusValidChange).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StatusValidChange', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StatusValidChange', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StatusValidChange', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addStatusValidChangeToCollectionIfMissing', () => {
      it('should add a StatusValidChange to an empty array', () => {
        const statusValidChange: IStatusValidChange = sampleWithRequiredData;
        expectedResult = service.addStatusValidChangeToCollectionIfMissing([], statusValidChange);
        expect(expectedResult).toEqual([statusValidChange]);
      });

      it('should not add a StatusValidChange to an array that contains it', () => {
        const statusValidChange: IStatusValidChange = sampleWithRequiredData;
        const statusValidChangeCollection: IStatusValidChange[] = [
          {
            ...statusValidChange,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStatusValidChangeToCollectionIfMissing(statusValidChangeCollection, statusValidChange);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StatusValidChange to an array that doesn't contain it", () => {
        const statusValidChange: IStatusValidChange = sampleWithRequiredData;
        const statusValidChangeCollection: IStatusValidChange[] = [sampleWithPartialData];
        expectedResult = service.addStatusValidChangeToCollectionIfMissing(statusValidChangeCollection, statusValidChange);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statusValidChange);
      });

      it('should add only unique StatusValidChange to an array', () => {
        const statusValidChangeArray: IStatusValidChange[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const statusValidChangeCollection: IStatusValidChange[] = [sampleWithRequiredData];
        expectedResult = service.addStatusValidChangeToCollectionIfMissing(statusValidChangeCollection, ...statusValidChangeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const statusValidChange: IStatusValidChange = sampleWithRequiredData;
        const statusValidChange2: IStatusValidChange = sampleWithPartialData;
        expectedResult = service.addStatusValidChangeToCollectionIfMissing([], statusValidChange, statusValidChange2);
        expect(expectedResult).toEqual([statusValidChange, statusValidChange2]);
      });

      it('should accept null and undefined values', () => {
        const statusValidChange: IStatusValidChange = sampleWithRequiredData;
        expectedResult = service.addStatusValidChangeToCollectionIfMissing([], null, statusValidChange, undefined);
        expect(expectedResult).toEqual([statusValidChange]);
      });

      it('should return initial array if no StatusValidChange is added', () => {
        const statusValidChangeCollection: IStatusValidChange[] = [sampleWithRequiredData];
        expectedResult = service.addStatusValidChangeToCollectionIfMissing(statusValidChangeCollection, undefined, null);
        expect(expectedResult).toEqual(statusValidChangeCollection);
      });
    });

    describe('compareStatusValidChange', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStatusValidChange(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8787 };
        const entity2 = null;

        const compareResult1 = service.compareStatusValidChange(entity1, entity2);
        const compareResult2 = service.compareStatusValidChange(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8787 };
        const entity2 = { id: 30744 };

        const compareResult1 = service.compareStatusValidChange(entity1, entity2);
        const compareResult2 = service.compareStatusValidChange(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8787 };
        const entity2 = { id: 8787 };

        const compareResult1 = service.compareStatusValidChange(entity1, entity2);
        const compareResult2 = service.compareStatusValidChange(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
