import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPaymentGatewayConfig } from '../payment-gateway-config.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../payment-gateway-config.test-samples';

import { PaymentGatewayConfigService } from './payment-gateway-config.service';

const requireRestSample: IPaymentGatewayConfig = {
  ...sampleWithRequiredData,
};

describe('PaymentGatewayConfig Service', () => {
  let service: PaymentGatewayConfigService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaymentGatewayConfig | IPaymentGatewayConfig[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentGatewayConfigService);
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

    it('should create a PaymentGatewayConfig', () => {
      const paymentGatewayConfig = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paymentGatewayConfig).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentGatewayConfig', () => {
      const paymentGatewayConfig = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paymentGatewayConfig).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentGatewayConfig', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentGatewayConfig', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaymentGatewayConfig', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPaymentGatewayConfigToCollectionIfMissing', () => {
      it('should add a PaymentGatewayConfig to an empty array', () => {
        const paymentGatewayConfig: IPaymentGatewayConfig = sampleWithRequiredData;
        expectedResult = service.addPaymentGatewayConfigToCollectionIfMissing([], paymentGatewayConfig);
        expect(expectedResult).toEqual([paymentGatewayConfig]);
      });

      it('should not add a PaymentGatewayConfig to an array that contains it', () => {
        const paymentGatewayConfig: IPaymentGatewayConfig = sampleWithRequiredData;
        const paymentGatewayConfigCollection: IPaymentGatewayConfig[] = [
          {
            ...paymentGatewayConfig,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaymentGatewayConfigToCollectionIfMissing(paymentGatewayConfigCollection, paymentGatewayConfig);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentGatewayConfig to an array that doesn't contain it", () => {
        const paymentGatewayConfig: IPaymentGatewayConfig = sampleWithRequiredData;
        const paymentGatewayConfigCollection: IPaymentGatewayConfig[] = [sampleWithPartialData];
        expectedResult = service.addPaymentGatewayConfigToCollectionIfMissing(paymentGatewayConfigCollection, paymentGatewayConfig);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentGatewayConfig);
      });

      it('should add only unique PaymentGatewayConfig to an array', () => {
        const paymentGatewayConfigArray: IPaymentGatewayConfig[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paymentGatewayConfigCollection: IPaymentGatewayConfig[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentGatewayConfigToCollectionIfMissing(paymentGatewayConfigCollection, ...paymentGatewayConfigArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentGatewayConfig: IPaymentGatewayConfig = sampleWithRequiredData;
        const paymentGatewayConfig2: IPaymentGatewayConfig = sampleWithPartialData;
        expectedResult = service.addPaymentGatewayConfigToCollectionIfMissing([], paymentGatewayConfig, paymentGatewayConfig2);
        expect(expectedResult).toEqual([paymentGatewayConfig, paymentGatewayConfig2]);
      });

      it('should accept null and undefined values', () => {
        const paymentGatewayConfig: IPaymentGatewayConfig = sampleWithRequiredData;
        expectedResult = service.addPaymentGatewayConfigToCollectionIfMissing([], null, paymentGatewayConfig, undefined);
        expect(expectedResult).toEqual([paymentGatewayConfig]);
      });

      it('should return initial array if no PaymentGatewayConfig is added', () => {
        const paymentGatewayConfigCollection: IPaymentGatewayConfig[] = [sampleWithRequiredData];
        expectedResult = service.addPaymentGatewayConfigToCollectionIfMissing(paymentGatewayConfigCollection, undefined, null);
        expect(expectedResult).toEqual(paymentGatewayConfigCollection);
      });
    });

    describe('comparePaymentGatewayConfig', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaymentGatewayConfig(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28126 };
        const entity2 = null;

        const compareResult1 = service.comparePaymentGatewayConfig(entity1, entity2);
        const compareResult2 = service.comparePaymentGatewayConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28126 };
        const entity2 = { id: 7027 };

        const compareResult1 = service.comparePaymentGatewayConfig(entity1, entity2);
        const compareResult2 = service.comparePaymentGatewayConfig(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 28126 };
        const entity2 = { id: 28126 };

        const compareResult1 = service.comparePaymentGatewayConfig(entity1, entity2);
        const compareResult2 = service.comparePaymentGatewayConfig(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
