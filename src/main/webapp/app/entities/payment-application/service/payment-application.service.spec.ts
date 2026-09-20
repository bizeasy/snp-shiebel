import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPaymentApplication } from '../payment-application.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../payment-application.test-samples';

import { PaymentApplicationService } from './payment-application.service';

const requireRestSample: IPaymentApplication = {
  ...sampleWithRequiredData,
};

describe('PaymentApplication Service', () => {
  let service: PaymentApplicationService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaymentApplication | IPaymentApplication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentApplicationService);
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

    it('should create a PaymentApplication', () => {
      const paymentApplication = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paymentApplication).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentApplication', () => {
      const paymentApplication = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paymentApplication).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentApplication', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentApplication', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaymentApplication', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPaymentApplicationToCollectionIfMissing', () => {
      it('should add a PaymentApplication to an empty array', () => {
        const paymentApplication: IPaymentApplication = sampleWithRequiredData;
        expectedResult = service.addPaymentApplicationToCollectionIfMissing([], paymentApplication);
        expect(expectedResult).toEqual([paymentApplication]);
      });

      it('should not add a PaymentApplication to an array that contains it', () => {
        const paymentApplication: IPaymentApplication = sampleWithRequiredData;
        const paymentApplicationCollection: IPaymentApplication[] = [
          {
            ...paymentApplication,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaymentApplicationToCollectionIfMissing(paymentApplicationCollection, paymentApplication);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentApplication to an array that doesn't contain it", () => {
        const paymentApplication: IPaymentApplication = sampleWithRequiredData;
        const paymentApplicationCollection: IPaymentApplication[] = [sampleWithPartialData];
        expectedResult = service.addPaymentApplicationToCollectionIfMissing(paymentApplicationCollection, paymentApplication);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentApplication);
      });

      it('should add only unique PaymentApplication to an array', () => {
        const paymentApplicationArray: IPaymentApplication[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paymentApplicationCollection: IPaymentApplication[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentApplicationToCollectionIfMissing(paymentApplicationCollection, ...paymentApplicationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentApplication: IPaymentApplication = sampleWithRequiredData;
        const paymentApplication2: IPaymentApplication = sampleWithPartialData;
        expectedResult = service.addPaymentApplicationToCollectionIfMissing([], paymentApplication, paymentApplication2);
        expect(expectedResult).toEqual([paymentApplication, paymentApplication2]);
      });

      it('should accept null and undefined values', () => {
        const paymentApplication: IPaymentApplication = sampleWithRequiredData;
        expectedResult = service.addPaymentApplicationToCollectionIfMissing([], null, paymentApplication, undefined);
        expect(expectedResult).toEqual([paymentApplication]);
      });

      it('should return initial array if no PaymentApplication is added', () => {
        const paymentApplicationCollection: IPaymentApplication[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentApplicationToCollectionIfMissing(paymentApplicationCollection, undefined, null);
        expect(expectedResult).toEqual(paymentApplicationCollection);
      });
    });

    describe('comparePaymentApplication', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaymentApplication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29590 };
        const entity2 = null;

        const compareResult1 = service.comparePaymentApplication(entity1, entity2);
        const compareResult2 = service.comparePaymentApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29590 };
        const entity2 = { id: 18400 };

        const compareResult1 = service.comparePaymentApplication(entity1, entity2);
        const compareResult2 = service.comparePaymentApplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 29590 };
        const entity2 = { id: 29590 };

        const compareResult1 = service.comparePaymentApplication(entity1, entity2);
        const compareResult2 = service.comparePaymentApplication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
