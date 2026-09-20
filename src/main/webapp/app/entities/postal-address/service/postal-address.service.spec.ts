import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPostalAddress } from '../postal-address.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../postal-address.test-samples';

import { PostalAddressService } from './postal-address.service';

const requireRestSample: IPostalAddress = {
  ...sampleWithRequiredData,
};

describe('PostalAddress Service', () => {
  let service: PostalAddressService;
  let httpMock: HttpTestingController;
  let expectedResult: IPostalAddress | IPostalAddress[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PostalAddressService);
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

    it('should create a PostalAddress', () => {
      const postalAddress = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(postalAddress).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PostalAddress', () => {
      const postalAddress = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(postalAddress).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PostalAddress', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PostalAddress', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PostalAddress', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPostalAddressToCollectionIfMissing', () => {
      it('should add a PostalAddress to an empty array', () => {
        const postalAddress: IPostalAddress = sampleWithRequiredData;
        expectedResult = service.addPostalAddressToCollectionIfMissing([], postalAddress);
        expect(expectedResult).toEqual([postalAddress]);
      });

      it('should not add a PostalAddress to an array that contains it', () => {
        const postalAddress: IPostalAddress = sampleWithRequiredData;
        const postalAddressCollection: IPostalAddress[] = [
          {
            ...postalAddress,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPostalAddressToCollectionIfMissing(postalAddressCollection, postalAddress);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PostalAddress to an array that doesn't contain it", () => {
        const postalAddress: IPostalAddress = sampleWithRequiredData;
        const postalAddressCollection: IPostalAddress[] = [sampleWithPartialData];
        expectedResult = service.addPostalAddressToCollectionIfMissing(postalAddressCollection, postalAddress);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(postalAddress);
      });

      it('should add only unique PostalAddress to an array', () => {
        const postalAddressArray: IPostalAddress[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const postalAddressCollection: IPostalAddress[] = [sampleWithRequiredData];
        expectedResult = service.addPostalAddressToCollectionIfMissing(postalAddressCollection, ...postalAddressArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const postalAddress: IPostalAddress = sampleWithRequiredData;
        const postalAddress2: IPostalAddress = sampleWithPartialData;
        expectedResult = service.addPostalAddressToCollectionIfMissing([], postalAddress, postalAddress2);
        expect(expectedResult).toEqual([postalAddress, postalAddress2]);
      });

      it('should accept null and undefined values', () => {
        const postalAddress: IPostalAddress = sampleWithRequiredData;
        expectedResult = service.addPostalAddressToCollectionIfMissing([], null, postalAddress, undefined);
        expect(expectedResult).toEqual([postalAddress]);
      });

      it('should return initial array if no PostalAddress is added', () => {
        const postalAddressCollection: IPostalAddress[] = [sampleWithRequiredData];
        expectedResult = service.addPostalAddressToCollectionIfMissing(postalAddressCollection, undefined, null);
        expect(expectedResult).toEqual(postalAddressCollection);
      });
    });

    describe('comparePostalAddress', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePostalAddress(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25687 };
        const entity2 = null;

        const compareResult1 = service.comparePostalAddress(entity1, entity2);
        const compareResult2 = service.comparePostalAddress(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25687 };
        const entity2 = { id: 32221 };

        const compareResult1 = service.comparePostalAddress(entity1, entity2);
        const compareResult2 = service.comparePostalAddress(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25687 };
        const entity2 = { id: 25687 };

        const compareResult1 = service.comparePostalAddress(entity1, entity2);
        const compareResult2 = service.comparePostalAddress(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
