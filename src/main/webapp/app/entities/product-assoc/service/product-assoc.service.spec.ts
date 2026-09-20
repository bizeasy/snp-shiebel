import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IProductAssoc } from '../product-assoc.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../product-assoc.test-samples';

import { ProductAssocService, RestProductAssoc } from './product-assoc.service';

const requireRestSample: RestProductAssoc = {
  ...sampleWithRequiredData,
  fromDate: sampleWithRequiredData.fromDate?.toJSON(),
  thruDate: sampleWithRequiredData.thruDate?.toJSON(),
};

describe('ProductAssoc Service', () => {
  let service: ProductAssocService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductAssoc | IProductAssoc[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProductAssocService);
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

    it('should create a ProductAssoc', () => {
      const productAssoc = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productAssoc).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductAssoc', () => {
      const productAssoc = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productAssoc).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductAssoc', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductAssoc', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductAssoc', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addProductAssocToCollectionIfMissing', () => {
      it('should add a ProductAssoc to an empty array', () => {
        const productAssoc: IProductAssoc = sampleWithRequiredData;
        expectedResult = service.addProductAssocToCollectionIfMissing([], productAssoc);
        expect(expectedResult).toEqual([productAssoc]);
      });

      it('should not add a ProductAssoc to an array that contains it', () => {
        const productAssoc: IProductAssoc = sampleWithRequiredData;
        const productAssocCollection: IProductAssoc[] = [
          {
            ...productAssoc,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductAssocToCollectionIfMissing(productAssocCollection, productAssoc);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductAssoc to an array that doesn't contain it", () => {
        const productAssoc: IProductAssoc = sampleWithRequiredData;
        const productAssocCollection: IProductAssoc[] = [sampleWithPartialData];
        expectedResult = service.addProductAssocToCollectionIfMissing(productAssocCollection, productAssoc);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productAssoc);
      });

      it('should add only unique ProductAssoc to an array', () => {
        const productAssocArray: IProductAssoc[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productAssocCollection: IProductAssoc[] = [sampleWithRequiredData];
        expectedResult = service.addProductAssocToCollectionIfMissing(productAssocCollection, ...productAssocArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productAssoc: IProductAssoc = sampleWithRequiredData;
        const productAssoc2: IProductAssoc = sampleWithPartialData;
        expectedResult = service.addProductAssocToCollectionIfMissing([], productAssoc, productAssoc2);
        expect(expectedResult).toEqual([productAssoc, productAssoc2]);
      });

      it('should accept null and undefined values', () => {
        const productAssoc: IProductAssoc = sampleWithRequiredData;
        expectedResult = service.addProductAssocToCollectionIfMissing([], null, productAssoc, undefined);
        expect(expectedResult).toEqual([productAssoc]);
      });

      it('should return initial array if no ProductAssoc is added', () => {
        const productAssocCollection: IProductAssoc[] = [sampleWithRequiredData];
        expectedResult = service.addProductAssocToCollectionIfMissing(productAssocCollection, undefined, null);
        expect(expectedResult).toEqual(productAssocCollection);
      });
    });

    describe('compareProductAssoc', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductAssoc(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20189 };
        const entity2 = null;

        const compareResult1 = service.compareProductAssoc(entity1, entity2);
        const compareResult2 = service.compareProductAssoc(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20189 };
        const entity2 = { id: 19160 };

        const compareResult1 = service.compareProductAssoc(entity1, entity2);
        const compareResult2 = service.compareProductAssoc(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 20189 };
        const entity2 = { id: 20189 };

        const compareResult1 = service.compareProductAssoc(entity1, entity2);
        const compareResult2 = service.compareProductAssoc(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
