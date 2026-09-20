import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IFileFolder } from '../file-folder.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../file-folder.test-samples';

import { FileFolderService, RestFileFolder } from './file-folder.service';

const requireRestSample: RestFileFolder = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('FileFolder Service', () => {
  let service: FileFolderService;
  let httpMock: HttpTestingController;
  let expectedResult: IFileFolder | IFileFolder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FileFolderService);
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

    it('should create a FileFolder', () => {
      const fileFolder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fileFolder).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileFolder', () => {
      const fileFolder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fileFolder).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FileFolder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FileFolder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FileFolder', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addFileFolderToCollectionIfMissing', () => {
      it('should add a FileFolder to an empty array', () => {
        const fileFolder: IFileFolder = sampleWithRequiredData;
        expectedResult = service.addFileFolderToCollectionIfMissing([], fileFolder);
        expect(expectedResult).toEqual([fileFolder]);
      });

      it('should not add a FileFolder to an array that contains it', () => {
        const fileFolder: IFileFolder = sampleWithRequiredData;
        const fileFolderCollection: IFileFolder[] = [
          {
            ...fileFolder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFileFolderToCollectionIfMissing(fileFolderCollection, fileFolder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileFolder to an array that doesn't contain it", () => {
        const fileFolder: IFileFolder = sampleWithRequiredData;
        const fileFolderCollection: IFileFolder[] = [sampleWithPartialData];
        expectedResult = service.addFileFolderToCollectionIfMissing(fileFolderCollection, fileFolder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileFolder);
      });

      it('should add only unique FileFolder to an array', () => {
        const fileFolderArray: IFileFolder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fileFolderCollection: IFileFolder[] = [sampleWithRequiredData];
        expectedResult = service.addFileFolderToCollectionIfMissing(fileFolderCollection, ...fileFolderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileFolder: IFileFolder = sampleWithRequiredData;
        const fileFolder2: IFileFolder = sampleWithPartialData;
        expectedResult = service.addFileFolderToCollectionIfMissing([], fileFolder, fileFolder2);
        expect(expectedResult).toEqual([fileFolder, fileFolder2]);
      });

      it('should accept null and undefined values', () => {
        const fileFolder: IFileFolder = sampleWithRequiredData;
        expectedResult = service.addFileFolderToCollectionIfMissing([], null, fileFolder, undefined);
        expect(expectedResult).toEqual([fileFolder]);
      });

      it('should return initial array if no FileFolder is added', () => {
        const fileFolderCollection: IFileFolder[] = [sampleWithRequiredData];
        expectedResult = service.addFileFolderToCollectionIfMissing(fileFolderCollection, undefined, null);
        expect(expectedResult).toEqual(fileFolderCollection);
      });
    });

    describe('compareFileFolder', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFileFolder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26348 };
        const entity2 = null;

        const compareResult1 = service.compareFileFolder(entity1, entity2);
        const compareResult2 = service.compareFileFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26348 };
        const entity2 = { id: 8213 };

        const compareResult1 = service.compareFileFolder(entity1, entity2);
        const compareResult2 = service.compareFileFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 26348 };
        const entity2 = { id: 26348 };

        const compareResult1 = service.compareFileFolder(entity1, entity2);
        const compareResult2 = service.compareFileFolder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
