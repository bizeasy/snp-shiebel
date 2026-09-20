import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IContentType } from '../content-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../content-type.test-samples';

import { ContentTypeService } from './content-type.service';

const requireRestSample: IContentType = {
  ...sampleWithRequiredData,
};

describe('ContentType Service', () => {
  let service: ContentTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IContentType | IContentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContentTypeService);
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

    it('should create a ContentType', () => {
      const contentType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contentType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContentType', () => {
      const contentType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contentType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContentType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContentType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContentType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addContentTypeToCollectionIfMissing', () => {
      it('should add a ContentType to an empty array', () => {
        const contentType: IContentType = sampleWithRequiredData;
        expectedResult = service.addContentTypeToCollectionIfMissing([], contentType);
        expect(expectedResult).toEqual([contentType]);
      });

      it('should not add a ContentType to an array that contains it', () => {
        const contentType: IContentType = sampleWithRequiredData;
        const contentTypeCollection: IContentType[] = [
          {
            ...contentType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContentTypeToCollectionIfMissing(contentTypeCollection, contentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContentType to an array that doesn't contain it", () => {
        const contentType: IContentType = sampleWithRequiredData;
        const contentTypeCollection: IContentType[] = [sampleWithPartialData];
        expectedResult = service.addContentTypeToCollectionIfMissing(contentTypeCollection, contentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contentType);
      });

      it('should add only unique ContentType to an array', () => {
        const contentTypeArray: IContentType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contentTypeCollection: IContentType[] = [sampleWithRequiredData];
        expectedResult = service.addContentTypeToCollectionIfMissing(contentTypeCollection, ...contentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contentType: IContentType = sampleWithRequiredData;
        const contentType2: IContentType = sampleWithPartialData;
        expectedResult = service.addContentTypeToCollectionIfMissing([], contentType, contentType2);
        expect(expectedResult).toEqual([contentType, contentType2]);
      });

      it('should accept null and undefined values', () => {
        const contentType: IContentType = sampleWithRequiredData;
        expectedResult = service.addContentTypeToCollectionIfMissing([], null, contentType, undefined);
        expect(expectedResult).toEqual([contentType]);
      });

      it('should return initial array if no ContentType is added', () => {
        const contentTypeCollection: IContentType[] = [sampleWithRequiredData];
        expectedResult = service.addContentTypeToCollectionIfMissing(contentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(contentTypeCollection);
      });
    });

    describe('compareContentType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContentType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8896 };
        const entity2 = null;

        const compareResult1 = service.compareContentType(entity1, entity2);
        const compareResult2 = service.compareContentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8896 };
        const entity2 = { id: 14829 };

        const compareResult1 = service.compareContentType(entity1, entity2);
        const compareResult2 = service.compareContentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8896 };
        const entity2 = { id: 8896 };

        const compareResult1 = service.compareContentType(entity1, entity2);
        const compareResult2 = service.compareContentType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
