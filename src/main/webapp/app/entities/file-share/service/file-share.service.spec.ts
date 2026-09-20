import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFileShare } from '../file-share.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../file-share.test-samples';

import { FileShareService, RestFileShare } from './file-share.service';

const requireRestSample: RestFileShare = {
  ...sampleWithRequiredData,
  sharedDate: sampleWithRequiredData.sharedDate?.toJSON(),
  expiryDate: sampleWithRequiredData.expiryDate?.toJSON(),
};

describe('FileShare Service', () => {
  let service: FileShareService;
  let httpMock: HttpTestingController;
  let expectedResult: IFileShare | IFileShare[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FileShareService);
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

    it('should create a FileShare', () => {
      const fileShare = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fileShare).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileShare', () => {
      const fileShare = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fileShare).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FileShare', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FileShare', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FileShare', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFileShareToCollectionIfMissing', () => {
      it('should add a FileShare to an empty array', () => {
        const fileShare: IFileShare = sampleWithRequiredData;
        expectedResult = service.addFileShareToCollectionIfMissing([], fileShare);
        expect(expectedResult).toEqual([fileShare]);
      });

      it('should not add a FileShare to an array that contains it', () => {
        const fileShare: IFileShare = sampleWithRequiredData;
        const fileShareCollection: IFileShare[] = [
          {
            ...fileShare,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFileShareToCollectionIfMissing(fileShareCollection, fileShare);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileShare to an array that doesn't contain it", () => {
        const fileShare: IFileShare = sampleWithRequiredData;
        const fileShareCollection: IFileShare[] = [sampleWithPartialData];
        expectedResult = service.addFileShareToCollectionIfMissing(fileShareCollection, fileShare);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileShare);
      });

      it('should add only unique FileShare to an array', () => {
        const fileShareArray: IFileShare[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fileShareCollection: IFileShare[] = [sampleWithRequiredData];
        expectedResult = service.addFileShareToCollectionIfMissing(fileShareCollection, ...fileShareArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileShare: IFileShare = sampleWithRequiredData;
        const fileShare2: IFileShare = sampleWithPartialData;
        expectedResult = service.addFileShareToCollectionIfMissing([], fileShare, fileShare2);
        expect(expectedResult).toEqual([fileShare, fileShare2]);
      });

      it('should accept null and undefined values', () => {
        const fileShare: IFileShare = sampleWithRequiredData;
        expectedResult = service.addFileShareToCollectionIfMissing([], null, fileShare, undefined);
        expect(expectedResult).toEqual([fileShare]);
      });

      it('should return initial array if no FileShare is added', () => {
        const fileShareCollection: IFileShare[] = [sampleWithRequiredData];
        expectedResult = service.addFileShareToCollectionIfMissing(fileShareCollection, undefined, null);
        expect(expectedResult).toEqual(fileShareCollection);
      });
    });

    describe('compareFileShare', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFileShare(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11663 };
        const entity2 = null;

        const compareResult1 = service.compareFileShare(entity1, entity2);
        const compareResult2 = service.compareFileShare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11663 };
        const entity2 = { id: 31237 };

        const compareResult1 = service.compareFileShare(entity1, entity2);
        const compareResult2 = service.compareFileShare(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 11663 };
        const entity2 = { id: 11663 };

        const compareResult1 = service.compareFileShare(entity1, entity2);
        const compareResult2 = service.compareFileShare(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
