import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductReview } from '../product-review.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-review.test-samples';

import { ProductReviewService, RestProductReview } from './product-review.service';

const requireRestSample: RestProductReview = {
  ...sampleWithRequiredData,
  reviewDate: sampleWithRequiredData.reviewDate?.toJSON(),
};

describe('ProductReview Service', () => {
  let service: ProductReviewService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductReview | IProductReview[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductReviewService);
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

    it('should create a ProductReview', () => {
      const productReview = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productReview).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductReview', () => {
      const productReview = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productReview).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductReview', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductReview', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductReview', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductReviewToCollectionIfMissing', () => {
      it('should add a ProductReview to an empty array', () => {
        const productReview: IProductReview = sampleWithRequiredData;
        expectedResult = service.addProductReviewToCollectionIfMissing([], productReview);
        expect(expectedResult).toEqual([productReview]);
      });

      it('should not add a ProductReview to an array that contains it', () => {
        const productReview: IProductReview = sampleWithRequiredData;
        const productReviewCollection: IProductReview[] = [
          {
            ...productReview,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, productReview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductReview to an array that doesn't contain it", () => {
        const productReview: IProductReview = sampleWithRequiredData;
        const productReviewCollection: IProductReview[] = [sampleWithPartialData];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, productReview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productReview);
      });

      it('should add only unique ProductReview to an array', () => {
        const productReviewArray: IProductReview[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productReviewCollection: IProductReview[] = [sampleWithRequiredData];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, ...productReviewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productReview: IProductReview = sampleWithRequiredData;
        const productReview2: IProductReview = sampleWithPartialData;
        expectedResult = service.addProductReviewToCollectionIfMissing([], productReview, productReview2);
        expect(expectedResult).toEqual([productReview, productReview2]);
      });

      it('should accept null and undefined values', () => {
        const productReview: IProductReview = sampleWithRequiredData;
        expectedResult = service.addProductReviewToCollectionIfMissing([], null, productReview, undefined);
        expect(expectedResult).toEqual([productReview]);
      });

      it('should return initial array if no ProductReview is added', () => {
        const productReviewCollection: IProductReview[] = [sampleWithRequiredData];
        expectedResult = service.addProductReviewToCollectionIfMissing(productReviewCollection, undefined, null);
        expect(expectedResult).toEqual(productReviewCollection);
      });
    });

    describe('compareProductReview', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductReview(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3443 };
        const entity2 = null;

        const compareResult1 = service.compareProductReview(entity1, entity2);
        const compareResult2 = service.compareProductReview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3443 };
        const entity2 = { id: 17685 };

        const compareResult1 = service.compareProductReview(entity1, entity2);
        const compareResult2 = service.compareProductReview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 3443 };
        const entity2 = { id: 3443 };

        const compareResult1 = service.compareProductReview(entity1, entity2);
        const compareResult2 = service.compareProductReview(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
