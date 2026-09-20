import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facility-carrier-shipment.test-samples';

import { FacilityCarrierShipmentFormService } from './facility-carrier-shipment-form.service';

describe('FacilityCarrierShipment Form Service', () => {
  let service: FacilityCarrierShipmentFormService;

  beforeEach(() => {
    service = TestBed.inject(FacilityCarrierShipmentFormService);
  });

  describe('Service methods', () => {
    describe('createFacilityCarrierShipmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacilityCarrierShipmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            facility: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
            shipmentMethodType: expect.any(Object),
          }),
        );
      });

      it('passing IFacilityCarrierShipment should create a new form with FormGroup', () => {
        const formGroup = service.createFacilityCarrierShipmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            facility: expect.any(Object),
            party: expect.any(Object),
            roleType: expect.any(Object),
            shipmentMethodType: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacilityCarrierShipment', () => {
      it('should return NewFacilityCarrierShipment for default FacilityCarrierShipment initial value', () => {
        const formGroup = service.createFacilityCarrierShipmentFormGroup(sampleWithNewData);

        const facilityCarrierShipment = service.getFacilityCarrierShipment(formGroup);

        expect(facilityCarrierShipment).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacilityCarrierShipment for empty FacilityCarrierShipment initial value', () => {
        const formGroup = service.createFacilityCarrierShipmentFormGroup();

        const facilityCarrierShipment = service.getFacilityCarrierShipment(formGroup);

        expect(facilityCarrierShipment).toMatchObject({});
      });

      it('should return IFacilityCarrierShipment', () => {
        const formGroup = service.createFacilityCarrierShipmentFormGroup(sampleWithRequiredData);

        const facilityCarrierShipment = service.getFacilityCarrierShipment(formGroup);

        expect(facilityCarrierShipment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacilityCarrierShipment should not enable id FormControl', () => {
        const formGroup = service.createFacilityCarrierShipmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacilityCarrierShipment should disable id FormControl', () => {
        const formGroup = service.createFacilityCarrierShipmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
