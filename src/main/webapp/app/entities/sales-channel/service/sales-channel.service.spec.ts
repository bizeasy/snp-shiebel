import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ISalesChannel } from '../sales-channel.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../sales-channel.test-samples';

import { SalesChannelService } from './sales-channel.service';

const requireRestSample: ISalesChannel = {
  ...sampleWithRequiredData,
};

describe('SalesChannel Service', () => {
  let service: SalesChannelService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalesChannel | ISalesChannel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SalesChannelService);
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

    it('should create a SalesChannel', () => {
      const salesChannel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salesChannel).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SalesChannel', () => {
      const salesChannel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salesChannel).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SalesChannel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SalesChannel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SalesChannel', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addSalesChannelToCollectionIfMissing', () => {
      it('should add a SalesChannel to an empty array', () => {
        const salesChannel: ISalesChannel = sampleWithRequiredData;
        expectedResult = service.addSalesChannelToCollectionIfMissing([], salesChannel);
        expect(expectedResult).toEqual([salesChannel]);
      });

      it('should not add a SalesChannel to an array that contains it', () => {
        const salesChannel: ISalesChannel = sampleWithRequiredData;
        const salesChannelCollection: ISalesChannel[] = [
          {
            ...salesChannel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalesChannelToCollectionIfMissing(salesChannelCollection, salesChannel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SalesChannel to an array that doesn't contain it", () => {
        const salesChannel: ISalesChannel = sampleWithRequiredData;
        const salesChannelCollection: ISalesChannel[] = [sampleWithPartialData];
        expectedResult = service.addSalesChannelToCollectionIfMissing(salesChannelCollection, salesChannel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salesChannel);
      });

      it('should add only unique SalesChannel to an array', () => {
        const salesChannelArray: ISalesChannel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salesChannelCollection: ISalesChannel[] = [sampleWithRequiredData];
        expectedResult = service.addSalesChannelToCollectionIfMissing(salesChannelCollection, ...salesChannelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salesChannel: ISalesChannel = sampleWithRequiredData;
        const salesChannel2: ISalesChannel = sampleWithPartialData;
        expectedResult = service.addSalesChannelToCollectionIfMissing([], salesChannel, salesChannel2);
        expect(expectedResult).toEqual([salesChannel, salesChannel2]);
      });

      it('should accept null and undefined values', () => {
        const salesChannel: ISalesChannel = sampleWithRequiredData;
        expectedResult = service.addSalesChannelToCollectionIfMissing([], null, salesChannel, undefined);
        expect(expectedResult).toEqual([salesChannel]);
      });

      it('should return initial array if no SalesChannel is added', () => {
        const salesChannelCollection: ISalesChannel[] = [sampleWithRequiredData];
        expectedResult = service.addSalesChannelToCollectionIfMissing(salesChannelCollection, undefined, null);
        expect(expectedResult).toEqual(salesChannelCollection);
      });
    });

    describe('compareSalesChannel', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalesChannel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25587 };
        const entity2 = null;

        const compareResult1 = service.compareSalesChannel(entity1, entity2);
        const compareResult2 = service.compareSalesChannel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25587 };
        const entity2 = { id: 32514 };

        const compareResult1 = service.compareSalesChannel(entity1, entity2);
        const compareResult2 = service.compareSalesChannel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25587 };
        const entity2 = { id: 25587 };

        const compareResult1 = service.compareSalesChannel(entity1, entity2);
        const compareResult2 = service.compareSalesChannel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
