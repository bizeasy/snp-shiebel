import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOrderAttachment } from '../order-attachment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../order-attachment.test-samples';

import { OrderAttachmentService } from './order-attachment.service';

const requireRestSample: IOrderAttachment = {
  ...sampleWithRequiredData,
};

describe('OrderAttachment Service', () => {
  let service: OrderAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderAttachment | IOrderAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrderAttachmentService);
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

    it('should create a OrderAttachment', () => {
      const orderAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderAttachment', () => {
      const orderAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrderAttachmentToCollectionIfMissing', () => {
      it('should add a OrderAttachment to an empty array', () => {
        const orderAttachment: IOrderAttachment = sampleWithRequiredData;
        expectedResult = service.addOrderAttachmentToCollectionIfMissing([], orderAttachment);
        expect(expectedResult).toEqual([orderAttachment]);
      });

      it('should not add a OrderAttachment to an array that contains it', () => {
        const orderAttachment: IOrderAttachment = sampleWithRequiredData;
        const orderAttachmentCollection: IOrderAttachment[] = [
          {
            ...orderAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderAttachmentToCollectionIfMissing(orderAttachmentCollection, orderAttachment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderAttachment to an array that doesn't contain it", () => {
        const orderAttachment: IOrderAttachment = sampleWithRequiredData;
        const orderAttachmentCollection: IOrderAttachment[] = [sampleWithPartialData];
        expectedResult = service.addOrderAttachmentToCollectionIfMissing(orderAttachmentCollection, orderAttachment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderAttachment);
      });

      it('should add only unique OrderAttachment to an array', () => {
        const orderAttachmentArray: IOrderAttachment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderAttachmentCollection: IOrderAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addOrderAttachmentToCollectionIfMissing(orderAttachmentCollection, ...orderAttachmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderAttachment: IOrderAttachment = sampleWithRequiredData;
        const orderAttachment2: IOrderAttachment = sampleWithPartialData;
        expectedResult = service.addOrderAttachmentToCollectionIfMissing([], orderAttachment, orderAttachment2);
        expect(expectedResult).toEqual([orderAttachment, orderAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const orderAttachment: IOrderAttachment = sampleWithRequiredData;
        expectedResult = service.addOrderAttachmentToCollectionIfMissing([], null, orderAttachment, undefined);
        expect(expectedResult).toEqual([orderAttachment]);
      });

      it('should return initial array if no OrderAttachment is added', () => {
        const orderAttachmentCollection: IOrderAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addOrderAttachmentToCollectionIfMissing(orderAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(orderAttachmentCollection);
      });
    });

    describe('compareOrderAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4865 };
        const entity2 = null;

        const compareResult1 = service.compareOrderAttachment(entity1, entity2);
        const compareResult2 = service.compareOrderAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4865 };
        const entity2 = { id: 21727 };

        const compareResult1 = service.compareOrderAttachment(entity1, entity2);
        const compareResult2 = service.compareOrderAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4865 };
        const entity2 = { id: 4865 };

        const compareResult1 = service.compareOrderAttachment(entity1, entity2);
        const compareResult2 = service.compareOrderAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
