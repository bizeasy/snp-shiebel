import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPaymentGatewayResponse } from '../payment-gateway-response.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../payment-gateway-response.test-samples';

import { PaymentGatewayResponseService } from './payment-gateway-response.service';

const requireRestSample: IPaymentGatewayResponse = {
  ...sampleWithRequiredData,
};

describe('PaymentGatewayResponse Service', () => {
  let service: PaymentGatewayResponseService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaymentGatewayResponse | IPaymentGatewayResponse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentGatewayResponseService);
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

    it('should create a PaymentGatewayResponse', () => {
      const paymentGatewayResponse = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paymentGatewayResponse).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentGatewayResponse', () => {
      const paymentGatewayResponse = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paymentGatewayResponse).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentGatewayResponse', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentGatewayResponse', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaymentGatewayResponse', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPaymentGatewayResponseToCollectionIfMissing', () => {
      it('should add a PaymentGatewayResponse to an empty array', () => {
        const paymentGatewayResponse: IPaymentGatewayResponse = sampleWithRequiredData;
        expectedResult = service.addPaymentGatewayResponseToCollectionIfMissing([], paymentGatewayResponse);
        expect(expectedResult).toEqual([paymentGatewayResponse]);
      });

      it('should not add a PaymentGatewayResponse to an array that contains it', () => {
        const paymentGatewayResponse: IPaymentGatewayResponse = sampleWithRequiredData;
        const paymentGatewayResponseCollection: IPaymentGatewayResponse[] = [
          {
            ...paymentGatewayResponse,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaymentGatewayResponseToCollectionIfMissing(paymentGatewayResponseCollection, paymentGatewayResponse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentGatewayResponse to an array that doesn't contain it", () => {
        const paymentGatewayResponse: IPaymentGatewayResponse = sampleWithRequiredData;
        const paymentGatewayResponseCollection: IPaymentGatewayResponse[] = [sampleWithPartialData];
        expectedResult = service.addPaymentGatewayResponseToCollectionIfMissing(paymentGatewayResponseCollection, paymentGatewayResponse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentGatewayResponse);
      });

      it('should add only unique PaymentGatewayResponse to an array', () => {
        const paymentGatewayResponseArray: IPaymentGatewayResponse[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paymentGatewayResponseCollection: IPaymentGatewayResponse[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentGatewayResponseToCollectionIfMissing(
          paymentGatewayResponseCollection,
          ...paymentGatewayResponseArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentGatewayResponse: IPaymentGatewayResponse = sampleWithRequiredData;
        const paymentGatewayResponse2: IPaymentGatewayResponse = sampleWithPartialData;
        expectedResult = service.addPaymentGatewayResponseToCollectionIfMissing([], paymentGatewayResponse, paymentGatewayResponse2);
        expect(expectedResult).toEqual([paymentGatewayResponse, paymentGatewayResponse2]);
      });

      it('should accept null and undefined values', () => {
        const paymentGatewayResponse: IPaymentGatewayResponse = sampleWithRequiredData;
        expectedResult = service.addPaymentGatewayResponseToCollectionIfMissing([], null, paymentGatewayResponse, undefined);
        expect(expectedResult).toEqual([paymentGatewayResponse]);
      });

      it('should return initial array if no PaymentGatewayResponse is added', () => {
        const paymentGatewayResponseCollection: IPaymentGatewayResponse[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentGatewayResponseToCollectionIfMissing(paymentGatewayResponseCollection, undefined, null);
        expect(expectedResult).toEqual(paymentGatewayResponseCollection);
      });
    });

    describe('comparePaymentGatewayResponse', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaymentGatewayResponse(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25625 };
        const entity2 = null;

        const compareResult1 = service.comparePaymentGatewayResponse(entity1, entity2);
        const compareResult2 = service.comparePaymentGatewayResponse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25625 };
        const entity2 = { id: 13199 };

        const compareResult1 = service.comparePaymentGatewayResponse(entity1, entity2);
        const compareResult2 = service.comparePaymentGatewayResponse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25625 };
        const entity2 = { id: 25625 };

        const compareResult1 = service.comparePaymentGatewayResponse(entity1, entity2);
        const compareResult2 = service.comparePaymentGatewayResponse(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
