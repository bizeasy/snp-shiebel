import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductAttachment } from '../product-attachment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-attachment.test-samples';

import { ProductAttachmentService } from './product-attachment.service';

const requireRestSample: IProductAttachment = {
  ...sampleWithRequiredData,
};

describe('ProductAttachment Service', () => {
  let service: ProductAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductAttachment | IProductAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductAttachmentService);
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

    it('should create a ProductAttachment', () => {
      const productAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductAttachment', () => {
      const productAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductAttachmentToCollectionIfMissing', () => {
      it('should add a ProductAttachment to an empty array', () => {
        const productAttachment: IProductAttachment = sampleWithRequiredData;
        expectedResult = service.addProductAttachmentToCollectionIfMissing([], productAttachment);
        expect(expectedResult).toEqual([productAttachment]);
      });

      it('should not add a ProductAttachment to an array that contains it', () => {
        const productAttachment: IProductAttachment = sampleWithRequiredData;
        const productAttachmentCollection: IProductAttachment[] = [
          {
            ...productAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductAttachmentToCollectionIfMissing(productAttachmentCollection, productAttachment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductAttachment to an array that doesn't contain it", () => {
        const productAttachment: IProductAttachment = sampleWithRequiredData;
        const productAttachmentCollection: IProductAttachment[] = [sampleWithPartialData];
        expectedResult = service.addProductAttachmentToCollectionIfMissing(productAttachmentCollection, productAttachment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productAttachment);
      });

      it('should add only unique ProductAttachment to an array', () => {
        const productAttachmentArray: IProductAttachment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productAttachmentCollection: IProductAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addProductAttachmentToCollectionIfMissing(productAttachmentCollection, ...productAttachmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productAttachment: IProductAttachment = sampleWithRequiredData;
        const productAttachment2: IProductAttachment = sampleWithPartialData;
        expectedResult = service.addProductAttachmentToCollectionIfMissing([], productAttachment, productAttachment2);
        expect(expectedResult).toEqual([productAttachment, productAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const productAttachment: IProductAttachment = sampleWithRequiredData;
        expectedResult = service.addProductAttachmentToCollectionIfMissing([], null, productAttachment, undefined);
        expect(expectedResult).toEqual([productAttachment]);
      });

      it('should return initial array if no ProductAttachment is added', () => {
        const productAttachmentCollection: IProductAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addProductAttachmentToCollectionIfMissing(productAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(productAttachmentCollection);
      });
    });

    describe('compareProductAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8437 };
        const entity2 = null;

        const compareResult1 = service.compareProductAttachment(entity1, entity2);
        const compareResult2 = service.compareProductAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8437 };
        const entity2 = { id: 6267 };

        const compareResult1 = service.compareProductAttachment(entity1, entity2);
        const compareResult2 = service.compareProductAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8437 };
        const entity2 = { id: 8437 };

        const compareResult1 = service.compareProductAttachment(entity1, entity2);
        const compareResult2 = service.compareProductAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
