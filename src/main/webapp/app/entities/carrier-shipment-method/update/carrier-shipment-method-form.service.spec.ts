import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../carrier-shipment-method.test-samples';

import { CarrierShipmentMethodFormService } from './carrier-shipment-method-form.service';

describe('CarrierShipmentMethod Form Service', () => {
  let service: CarrierShipmentMethodFormService;

  beforeEach(() => {
    service = TestBed.inject(CarrierShipmentMethodFormService);
  });

  describe('Service methods', () => {
    describe('createCarrierShipmentMethodFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCarrierShipmentMethodFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            carrierServiceCode: expect.any(Object),
            shipmentMethodType: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });

      it('passing ICarrierShipmentMethod should create a new form with FormGroup', () => {
        const formGroup = service.createCarrierShipmentMethodFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sequenceNo: expect.any(Object),
            carrierServiceCode: expect.any(Object),
            shipmentMethodType: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
          }),
        );
      });
    });

    describe('getCarrierShipmentMethod', () => {
      it('should return NewCarrierShipmentMethod for default CarrierShipmentMethod initial value', () => {
        const formGroup = service.createCarrierShipmentMethodFormGroup(sampleWithNewData);

        const carrierShipmentMethod = service.getCarrierShipmentMethod(formGroup);

        expect(carrierShipmentMethod).toMatchObject(sampleWithNewData);
      });

      it('should return NewCarrierShipmentMethod for empty CarrierShipmentMethod initial value', () => {
        const formGroup = service.createCarrierShipmentMethodFormGroup();

        const carrierShipmentMethod = service.getCarrierShipmentMethod(formGroup);

        expect(carrierShipmentMethod).toMatchObject({});
      });

      it('should return ICarrierShipmentMethod', () => {
        const formGroup = service.createCarrierShipmentMethodFormGroup(sampleWithRequiredData);

        const carrierShipmentMethod = service.getCarrierShipmentMethod(formGroup);

        expect(carrierShipmentMethod).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICarrierShipmentMethod should not enable id FormControl', () => {
        const formGroup = service.createCarrierShipmentMethodFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCarrierShipmentMethod should disable id FormControl', () => {
        const formGroup = service.createCarrierShipmentMethodFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
