import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IContactMech } from '../contact-mech.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../contact-mech.test-samples';

import { ContactMechService } from './contact-mech.service';

const requireRestSample: IContactMech = {
  ...sampleWithRequiredData,
};

describe('ContactMech Service', () => {
  let service: ContactMechService;
  let httpMock: HttpTestingController;
  let expectedResult: IContactMech | IContactMech[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContactMechService);
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

    it('should create a ContactMech', () => {
      const contactMech = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactMech', () => {
      const contactMech = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contactMech).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContactMech', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContactMech', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ContactMech', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addContactMechToCollectionIfMissing', () => {
      it('should add a ContactMech to an empty array', () => {
        const contactMech: IContactMech = sampleWithRequiredData;
        expectedResult = service.addContactMechToCollectionIfMissing([], contactMech);
        expect(expectedResult).toEqual([contactMech]);
      });

      it('should not add a ContactMech to an array that contains it', () => {
        const contactMech: IContactMech = sampleWithRequiredData;
        const contactMechCollection: IContactMech[] = [
          {
            ...contactMech,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContactMechToCollectionIfMissing(contactMechCollection, contactMech);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactMech to an array that doesn't contain it", () => {
        const contactMech: IContactMech = sampleWithRequiredData;
        const contactMechCollection: IContactMech[] = [sampleWithPartialData];
        expectedResult = service.addContactMechToCollectionIfMissing(contactMechCollection, contactMech);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactMech);
      });

      it('should add only unique ContactMech to an array', () => {
        const contactMechArray: IContactMech[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contactMechCollection: IContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addContactMechToCollectionIfMissing(contactMechCollection, ...contactMechArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactMech: IContactMech = sampleWithRequiredData;
        const contactMech2: IContactMech = sampleWithPartialData;
        expectedResult = service.addContactMechToCollectionIfMissing([], contactMech, contactMech2);
        expect(expectedResult).toEqual([contactMech, contactMech2]);
      });

      it('should accept null and undefined values', () => {
        const contactMech: IContactMech = sampleWithRequiredData;
        expectedResult = service.addContactMechToCollectionIfMissing([], null, contactMech, undefined);
        expect(expectedResult).toEqual([contactMech]);
      });

      it('should return initial array if no ContactMech is added', () => {
        const contactMechCollection: IContactMech[] = [sampleWithRequiredData];
        expectedResult = service.addContactMechToCollectionIfMissing(contactMechCollection, undefined, null);
        expect(expectedResult).toEqual(contactMechCollection);
      });
    });

    describe('compareContactMech', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContactMech(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5019 };
        const entity2 = null;

        const compareResult1 = service.compareContactMech(entity1, entity2);
        const compareResult2 = service.compareContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5019 };
        const entity2 = { id: 29939 };

        const compareResult1 = service.compareContactMech(entity1, entity2);
        const compareResult2 = service.compareContactMech(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 5019 };
        const entity2 = { id: 5019 };

        const compareResult1 = service.compareContactMech(entity1, entity2);
        const compareResult2 = service.compareContactMech(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
