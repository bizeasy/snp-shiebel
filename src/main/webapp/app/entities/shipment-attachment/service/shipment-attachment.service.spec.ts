import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IShipmentAttachment } from '../shipment-attachment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../shipment-attachment.test-samples';

import { ShipmentAttachmentService } from './shipment-attachment.service';

const requireRestSample: IShipmentAttachment = {
  ...sampleWithRequiredData,
};

describe('ShipmentAttachment Service', () => {
  let service: ShipmentAttachmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IShipmentAttachment | IShipmentAttachment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipmentAttachmentService);
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

    it('should create a ShipmentAttachment', () => {
      const shipmentAttachment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shipmentAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShipmentAttachment', () => {
      const shipmentAttachment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shipmentAttachment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShipmentAttachment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShipmentAttachment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShipmentAttachment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addShipmentAttachmentToCollectionIfMissing', () => {
      it('should add a ShipmentAttachment to an empty array', () => {
        const shipmentAttachment: IShipmentAttachment = sampleWithRequiredData;
        expectedResult = service.addShipmentAttachmentToCollectionIfMissing([], shipmentAttachment);
        expect(expectedResult).toEqual([shipmentAttachment]);
      });

      it('should not add a ShipmentAttachment to an array that contains it', () => {
        const shipmentAttachment: IShipmentAttachment = sampleWithRequiredData;
        const shipmentAttachmentCollection: IShipmentAttachment[] = [
          {
            ...shipmentAttachment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipmentAttachmentToCollectionIfMissing(shipmentAttachmentCollection, shipmentAttachment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShipmentAttachment to an array that doesn't contain it", () => {
        const shipmentAttachment: IShipmentAttachment = sampleWithRequiredData;
        const shipmentAttachmentCollection: IShipmentAttachment[] = [sampleWithPartialData];
        expectedResult = service.addShipmentAttachmentToCollectionIfMissing(shipmentAttachmentCollection, shipmentAttachment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shipmentAttachment);
      });

      it('should add only unique ShipmentAttachment to an array', () => {
        const shipmentAttachmentArray: IShipmentAttachment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipmentAttachmentCollection: IShipmentAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentAttachmentToCollectionIfMissing(shipmentAttachmentCollection, ...shipmentAttachmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shipmentAttachment: IShipmentAttachment = sampleWithRequiredData;
        const shipmentAttachment2: IShipmentAttachment = sampleWithPartialData;
        expectedResult = service.addShipmentAttachmentToCollectionIfMissing([], shipmentAttachment, shipmentAttachment2);
        expect(expectedResult).toEqual([shipmentAttachment, shipmentAttachment2]);
      });

      it('should accept null and undefined values', () => {
        const shipmentAttachment: IShipmentAttachment = sampleWithRequiredData;
        expectedResult = service.addShipmentAttachmentToCollectionIfMissing([], null, shipmentAttachment, undefined);
        expect(expectedResult).toEqual([shipmentAttachment]);
      });

      it('should return initial array if no ShipmentAttachment is added', () => {
        const shipmentAttachmentCollection: IShipmentAttachment[] = [sampleWithRequiredData];
        expectedResult = service.addShipmentAttachmentToCollectionIfMissing(shipmentAttachmentCollection, undefined, null);
        expect(expectedResult).toEqual(shipmentAttachmentCollection);
      });
    });

    describe('compareShipmentAttachment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShipmentAttachment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 126 };
        const entity2 = null;

        const compareResult1 = service.compareShipmentAttachment(entity1, entity2);
        const compareResult2 = service.compareShipmentAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 126 };
        const entity2 = { id: 12976 };

        const compareResult1 = service.compareShipmentAttachment(entity1, entity2);
        const compareResult2 = service.compareShipmentAttachment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 126 };
        const entity2 = { id: 126 };

        const compareResult1 = service.compareShipmentAttachment(entity1, entity2);
        const compareResult2 = service.compareShipmentAttachment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
