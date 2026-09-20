import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOtpAuth } from '../otp-auth.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../otp-auth.test-samples';

import { OtpAuthService, RestOtpAuth } from './otp-auth.service';

const requireRestSample: RestOtpAuth = {
  ...sampleWithRequiredData,
  otpSentTime: sampleWithRequiredData.otpSentTime?.toJSON(),
};

describe('OtpAuth Service', () => {
  let service: OtpAuthService;
  let httpMock: HttpTestingController;
  let expectedResult: IOtpAuth | IOtpAuth[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OtpAuthService);
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

    it('should create a OtpAuth', () => {
      const otpAuth = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(otpAuth).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OtpAuth', () => {
      const otpAuth = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(otpAuth).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OtpAuth', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OtpAuth', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OtpAuth', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOtpAuthToCollectionIfMissing', () => {
      it('should add a OtpAuth to an empty array', () => {
        const otpAuth: IOtpAuth = sampleWithRequiredData;
        expectedResult = service.addOtpAuthToCollectionIfMissing([], otpAuth);
        expect(expectedResult).toEqual([otpAuth]);
      });

      it('should not add a OtpAuth to an array that contains it', () => {
        const otpAuth: IOtpAuth = sampleWithRequiredData;
        const otpAuthCollection: IOtpAuth[] = [
          {
            ...otpAuth,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOtpAuthToCollectionIfMissing(otpAuthCollection, otpAuth);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OtpAuth to an array that doesn't contain it", () => {
        const otpAuth: IOtpAuth = sampleWithRequiredData;
        const otpAuthCollection: IOtpAuth[] = [sampleWithPartialData];
        expectedResult = service.addOtpAuthToCollectionIfMissing(otpAuthCollection, otpAuth);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(otpAuth);
      });

      it('should add only unique OtpAuth to an array', () => {
        const otpAuthArray: IOtpAuth[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const otpAuthCollection: IOtpAuth[] = [sampleWithRequiredData];
        expectedResult = service.addOtpAuthToCollectionIfMissing(otpAuthCollection, ...otpAuthArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const otpAuth: IOtpAuth = sampleWithRequiredData;
        const otpAuth2: IOtpAuth = sampleWithPartialData;
        expectedResult = service.addOtpAuthToCollectionIfMissing([], otpAuth, otpAuth2);
        expect(expectedResult).toEqual([otpAuth, otpAuth2]);
      });

      it('should accept null and undefined values', () => {
        const otpAuth: IOtpAuth = sampleWithRequiredData;
        expectedResult = service.addOtpAuthToCollectionIfMissing([], null, otpAuth, undefined);
        expect(expectedResult).toEqual([otpAuth]);
      });

      it('should return initial array if no OtpAuth is added', () => {
        const otpAuthCollection: IOtpAuth[] = [sampleWithRequiredData];
        expectedResult = service.addOtpAuthToCollectionIfMissing(otpAuthCollection, undefined, null);
        expect(expectedResult).toEqual(otpAuthCollection);
      });
    });

    describe('compareOtpAuth', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOtpAuth(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23261 };
        const entity2 = null;

        const compareResult1 = service.compareOtpAuth(entity1, entity2);
        const compareResult2 = service.compareOtpAuth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23261 };
        const entity2 = { id: 5252 };

        const compareResult1 = service.compareOtpAuth(entity1, entity2);
        const compareResult2 = service.compareOtpAuth(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 23261 };
        const entity2 = { id: 23261 };

        const compareResult1 = service.compareOtpAuth(entity1, entity2);
        const compareResult2 = service.compareOtpAuth(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
