import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IContactMechType } from '../contact-mech-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../contact-mech-type.test-samples';

import { ContactMechTypeService } from './contact-mech-type.service';

const requireRestSample: IContactMechType = {
  ...sampleWithRequiredData,
};

describe('ContactMechType Service', () => {
  let service: ContactMechTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IContactMechType | IContactMechType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContactMechTypeService);
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

    it('should create a ContactMechType', () => {
      const contactMechType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contactMechType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactMechType', () => {
      const contactMechType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contactMechType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContactMechType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContactMechType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContactMechType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addContactMechTypeToCollectionIfMissing', () => {
      it('should add a ContactMechType to an empty array', () => {
        const contactMechType: IContactMechType = sampleWithRequiredData;
        expectedResult = service.addContactMechTypeToCollectionIfMissing([], contactMechType);
        expect(expectedResult).toEqual([contactMechType]);
      });

      it('should not add a ContactMechType to an array that contains it', () => {
        const contactMechType: IContactMechType = sampleWithRequiredData;
        const contactMechTypeCollection: IContactMechType[] = [
          {
            ...contactMechType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContactMechTypeToCollectionIfMissing(contactMechTypeCollection, contactMechType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactMechType to an array that doesn't contain it", () => {
        const contactMechType: IContactMechType = sampleWithRequiredData;
        const contactMechTypeCollection: IContactMechType[] = [sampleWithPartialData];
        expectedResult = service.addContactMechTypeToCollectionIfMissing(contactMechTypeCollection, contactMechType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactMechType);
      });

      it('should add only unique ContactMechType to an array', () => {
        const contactMechTypeArray: IContactMechType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contactMechTypeCollection: IContactMechType[] = [sampleWithRequiredData];
        expectedResult = service.addContactMechTypeToCollectionIfMissing(contactMechTypeCollection, ...contactMechTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactMechType: IContactMechType = sampleWithRequiredData;
        const contactMechType2: IContactMechType = sampleWithPartialData;
        expectedResult = service.addContactMechTypeToCollectionIfMissing([], contactMechType, contactMechType2);
        expect(expectedResult).toEqual([contactMechType, contactMechType2]);
      });

      it('should accept null and undefined values', () => {
        const contactMechType: IContactMechType = sampleWithRequiredData;
        expectedResult = service.addContactMechTypeToCollectionIfMissing([], null, contactMechType, undefined);
        expect(expectedResult).toEqual([contactMechType]);
      });

      it('should return initial array if no ContactMechType is added', () => {
        const contactMechTypeCollection: IContactMechType[] = [sampleWithRequiredData];
        expectedResult = service.addContactMechTypeToCollectionIfMissing(contactMechTypeCollection, undefined, null);
        expect(expectedResult).toEqual(contactMechTypeCollection);
      });
    });

    describe('compareContactMechType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContactMechType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10228 };
        const entity2 = null;

        const compareResult1 = service.compareContactMechType(entity1, entity2);
        const compareResult2 = service.compareContactMechType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10228 };
        const entity2 = { id: 22225 };

        const compareResult1 = service.compareContactMechType(entity1, entity2);
        const compareResult2 = service.compareContactMechType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 10228 };
        const entity2 = { id: 10228 };

        const compareResult1 = service.compareContactMechType(entity1, entity2);
        const compareResult2 = service.compareContactMechType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
