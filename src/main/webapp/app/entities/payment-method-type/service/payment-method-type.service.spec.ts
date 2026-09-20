import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPaymentMethodType } from '../payment-method-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../payment-method-type.test-samples';

import { PaymentMethodTypeService } from './payment-method-type.service';

const requireRestSample: IPaymentMethodType = {
  ...sampleWithRequiredData,
};

describe('PaymentMethodType Service', () => {
  let service: PaymentMethodTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaymentMethodType | IPaymentMethodType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentMethodTypeService);
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

    it('should create a PaymentMethodType', () => {
      const paymentMethodType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paymentMethodType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentMethodType', () => {
      const paymentMethodType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paymentMethodType).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentMethodType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentMethodType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaymentMethodType', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPaymentMethodTypeToCollectionIfMissing', () => {
      it('should add a PaymentMethodType to an empty array', () => {
        const paymentMethodType: IPaymentMethodType = sampleWithRequiredData;
        expectedResult = service.addPaymentMethodTypeToCollectionIfMissing([], paymentMethodType);
        expect(expectedResult).toEqual([paymentMethodType]);
      });

      it('should not add a PaymentMethodType to an array that contains it', () => {
        const paymentMethodType: IPaymentMethodType = sampleWithRequiredData;
        const paymentMethodTypeCollection: IPaymentMethodType[] = [
          {
            ...paymentMethodType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaymentMethodTypeToCollectionIfMissing(paymentMethodTypeCollection, paymentMethodType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentMethodType to an array that doesn't contain it", () => {
        const paymentMethodType: IPaymentMethodType = sampleWithRequiredData;
        const paymentMethodTypeCollection: IPaymentMethodType[] = [sampleWithPartialData];
        expectedResult = service.addPaymentMethodTypeToCollectionIfMissing(paymentMethodTypeCollection, paymentMethodType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentMethodType);
      });

      it('should add only unique PaymentMethodType to an array', () => {
        const paymentMethodTypeArray: IPaymentMethodType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paymentMethodTypeCollection: IPaymentMethodType[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentMethodTypeToCollectionIfMissing(paymentMethodTypeCollection, ...paymentMethodTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentMethodType: IPaymentMethodType = sampleWithRequiredData;
        const paymentMethodType2: IPaymentMethodType = sampleWithPartialData;
        expectedResult = service.addPaymentMethodTypeToCollectionIfMissing([], paymentMethodType, paymentMethodType2);
        expect(expectedResult).toEqual([paymentMethodType, paymentMethodType2]);
      });

      it('should accept null and undefined values', () => {
        const paymentMethodType: IPaymentMethodType = sampleWithRequiredData;
        expectedResult = service.addPaymentMethodTypeToCollectionIfMissing([], null, paymentMethodType, undefined);
        expect(expectedResult).toEqual([paymentMethodType]);
      });

      it('should return initial array if no PaymentMethodType is added', () => {
        const paymentMethodTypeCollection: IPaymentMethodType[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentMethodTypeToCollectionIfMissing(paymentMethodTypeCollection, undefined, null);
        expect(expectedResult).toEqual(paymentMethodTypeCollection);
      });
    });

    describe('comparePaymentMethodType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaymentMethodType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6662 };
        const entity2 = null;

        const compareResult1 = service.comparePaymentMethodType(entity1, entity2);
        const compareResult2 = service.comparePaymentMethodType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6662 };
        const entity2 = { id: 13122 };

        const compareResult1 = service.comparePaymentMethodType(entity1, entity2);
        const compareResult2 = service.comparePaymentMethodType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6662 };
        const entity2 = { id: 6662 };

        const compareResult1 = service.comparePaymentMethodType(entity1, entity2);
        const compareResult2 = service.comparePaymentMethodType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
