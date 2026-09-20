import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inventory-transfer.test-samples';

import { InventoryTransferFormService } from './inventory-transfer-form.service';

describe('InventoryTransfer Form Service', () => {
  let service: InventoryTransferFormService;

  beforeEach(() => {
    service = TestBed.inject(InventoryTransferFormService);
  });

  describe('Service methods', () => {
    describe('createInventoryTransferFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventoryTransferFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sentDate: expect.any(Object),
            receivedDate: expect.any(Object),
            comments: expect.any(Object),
            inventoryItem: expect.any(Object),
            status: expect.any(Object),
            facility: expect.any(Object),
            facilityLocation: expect.any(Object),
            facilityTo: expect.any(Object),
            facilityLocationTo: expect.any(Object),
            issuance: expect.any(Object),
          }),
        );
      });

      it('passing IInventoryTransfer should create a new form with FormGroup', () => {
        const formGroup = service.createInventoryTransferFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sentDate: expect.any(Object),
            receivedDate: expect.any(Object),
            comments: expect.any(Object),
            inventoryItem: expect.any(Object),
            status: expect.any(Object),
            facility: expect.any(Object),
            facilityLocation: expect.any(Object),
            facilityTo: expect.any(Object),
            facilityLocationTo: expect.any(Object),
            issuance: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventoryTransfer', () => {
      it('should return NewInventoryTransfer for default InventoryTransfer initial value', () => {
        const formGroup = service.createInventoryTransferFormGroup(sampleWithNewData);

        const inventoryTransfer = service.getInventoryTransfer(formGroup);

        expect(inventoryTransfer).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventoryTransfer for empty InventoryTransfer initial value', () => {
        const formGroup = service.createInventoryTransferFormGroup();

        const inventoryTransfer = service.getInventoryTransfer(formGroup);

        expect(inventoryTransfer).toMatchObject({});
      });

      it('should return IInventoryTransfer', () => {
        const formGroup = service.createInventoryTransferFormGroup(sampleWithRequiredData);

        const inventoryTransfer = service.getInventoryTransfer(formGroup);

        expect(inventoryTransfer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventoryTransfer should not enable id FormControl', () => {
        const formGroup = service.createInventoryTransferFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventoryTransfer should disable id FormControl', () => {
        const formGroup = service.createInventoryTransferFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
