import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IContactMechPurpose } from '../contact-mech-purpose.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../contact-mech-purpose.test-samples';

import { ContactMechPurposeService } from './contact-mech-purpose.service';

const requireRestSample: IContactMechPurpose = {
  ...sampleWithRequiredData,
};

describe('ContactMechPurpose Service', () => {
  let service: ContactMechPurposeService;
  let httpMock: HttpTestingController;
  let expectedResult: IContactMechPurpose | IContactMechPurpose[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContactMechPurposeService);
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

    it('should create a ContactMechPurpose', () => {
      const contactMechPurpose = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contactMechPurpose).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactMechPurpose', () => {
      const contactMechPurpose = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contactMechPurpose).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContactMechPurpose', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContactMechPurpose', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContactMechPurpose', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addContactMechPurposeToCollectionIfMissing', () => {
      it('should add a ContactMechPurpose to an empty array', () => {
        const contactMechPurpose: IContactMechPurpose = sampleWithRequiredData;
        expectedResult = service.addContactMechPurposeToCollectionIfMissing([], contactMechPurpose);
        expect(expectedResult).toEqual([contactMechPurpose]);
      });

      it('should not add a ContactMechPurpose to an array that contains it', () => {
        const contactMechPurpose: IContactMechPurpose = sampleWithRequiredData;
        const contactMechPurposeCollection: IContactMechPurpose[] = [
          {
            ...contactMechPurpose,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContactMechPurposeToCollectionIfMissing(contactMechPurposeCollection, contactMechPurpose);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactMechPurpose to an array that doesn't contain it", () => {
        const contactMechPurpose: IContactMechPurpose = sampleWithRequiredData;
        const contactMechPurposeCollection: IContactMechPurpose[] = [sampleWithPartialData];
        expectedResult = service.addContactMechPurposeToCollectionIfMissing(contactMechPurposeCollection, contactMechPurpose);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactMechPurpose);
      });

      it('should add only unique ContactMechPurpose to an array', () => {
        const contactMechPurposeArray: IContactMechPurpose[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contactMechPurposeCollection: IContactMechPurpose[] = [sampleWithRequiredData];
        expectedResult = service.addContactMechPurposeToCollectionIfMissing(contactMechPurposeCollection, ...contactMechPurposeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactMechPurpose: IContactMechPurpose = sampleWithRequiredData;
        const contactMechPurpose2: IContactMechPurpose = sampleWithPartialData;
        expectedResult = service.addContactMechPurposeToCollectionIfMissing([], contactMechPurpose, contactMechPurpose2);
        expect(expectedResult).toEqual([contactMechPurpose, contactMechPurpose2]);
      });

      it('should accept null and undefined values', () => {
        const contactMechPurpose: IContactMechPurpose = sampleWithRequiredData;
        expectedResult = service.addContactMechPurposeToCollectionIfMissing([], null, contactMechPurpose, undefined);
        expect(expectedResult).toEqual([contactMechPurpose]);
      });

      it('should return initial array if no ContactMechPurpose is added', () => {
        const contactMechPurposeCollection: IContactMechPurpose[] = [sampleWithRequiredData];
        expectedResult = service.addContactMechPurposeToCollectionIfMissing(contactMechPurposeCollection, undefined, null);
        expect(expectedResult).toEqual(contactMechPurposeCollection);
      });
    });

    describe('compareContactMechPurpose', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContactMechPurpose(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25033 };
        const entity2 = null;

        const compareResult1 = service.compareContactMechPurpose(entity1, entity2);
        const compareResult2 = service.compareContactMechPurpose(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25033 };
        const entity2 = { id: 10745 };

        const compareResult1 = service.compareContactMechPurpose(entity1, entity2);
        const compareResult2 = service.compareContactMechPurpose(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25033 };
        const entity2 = { id: 25033 };

        const compareResult1 = service.compareContactMechPurpose(entity1, entity2);
        const compareResult2 = service.compareContactMechPurpose(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
