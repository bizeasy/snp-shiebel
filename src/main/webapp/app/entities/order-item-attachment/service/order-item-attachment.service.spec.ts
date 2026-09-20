import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderItemAttachment } from '../order-item-attachment.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../order-item-attachment.test-samples';

import { OrderItemAttachmentService } from './order-item-attachment.service';

const requireRestSample: IOrderItemAttachment = {
  ...sampleWithRequiredData,
};

describe('OrderItemAttachment Service', () => {
  let service: OrderItemAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderItemAttachment | IOrderItemAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderItemAttachmentService);
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

    it('should create a OrderItemAttachment', () => {
      const orderItemAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderItemAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderItemAttachment', () => {
      const orderItemAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderItemAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderItemAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderItemAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderItemAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderItemAttachmentToCollectionIfMissing', () => {
      it('should add a OrderItemAttachment to an empty array', () => {
        const orderItemAttachment: IOrderItemAttachment = sampleWithRequiredData;
        expectedResult = service.addOrderItemAttachmentToCollectionIfMissing([], orderItemAttachment);
        expect(expectedResult).toEqual([orderItemAttachment]);
      });

      it('should not add a OrderItemAttachment to an array that contains it', () => {
        const orderItemAttachment: IOrderItemAttachment = sampleWithRequiredData;
        const orderItemAttachmentCollection: IOrderItemAttachment[] = [
          {
            ...orderItemAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderItemAttachmentToCollectionIfMissing(orderItemAttachmentCollection, orderItemAttachment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderItemAttachment to an array that doesn't contain it", () => {
        const orderItemAttachment: IOrderItemAttachment = sampleWithRequiredData;
        const orderItemAttachmentCollection: IOrderItemAttachment[] = [sampleWithPartialData];
        expectedResult = service.addOrderItemAttachmentToCollectionIfMissing(orderItemAttachmentCollection, orderItemAttachment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderItemAttachment);
      });

      it('should add only unique OrderItemAttachment to an array', () => {
        const orderItemAttachmentArray: IOrderItemAttachment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderItemAttachmentCollection: IOrderItemAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addOrderItemAttachmentToCollectionIfMissing(orderItemAttachmentCollection, ...orderItemAttachmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderItemAttachment: IOrderItemAttachment = sampleWithRequiredData;
        const orderItemAttachment2: IOrderItemAttachment = sampleWithPartialData;
        expectedResult = service.addOrderItemAttachmentToCollectionIfMissing([], orderItemAttachment, orderItemAttachment2);
        expect(expectedResult).toEqual([orderItemAttachment, orderItemAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const orderItemAttachment: IOrderItemAttachment = sampleWithRequiredData;
        expectedResult = service.addOrderItemAttachmentToCollectionIfMissing([], null, orderItemAttachment, undefined);
        expect(expectedResult).toEqual([orderItemAttachment]);
      });

      it('should return initial array if no OrderItemAttachment is added', () => {
        const orderItemAttachmentCollection: IOrderItemAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addOrderItemAttachmentToCollectionIfMissing(orderItemAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(orderItemAttachmentCollection);
      });
    });

    describe('compareOrderItemAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderItemAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4150 };
        const entity2 = null;

        const compareResult1 = service.compareOrderItemAttachment(entity1, entity2);
        const compareResult2 = service.compareOrderItemAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4150 };
        const entity2 = { id: 26311 };

        const compareResult1 = service.compareOrderItemAttachment(entity1, entity2);
        const compareResult2 = service.compareOrderItemAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4150 };
        const entity2 = { id: 4150 };

        const compareResult1 = service.compareOrderItemAttachment(entity1, entity2);
        const compareResult2 = service.compareOrderItemAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
