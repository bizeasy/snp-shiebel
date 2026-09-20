import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../sales-channel.test-samples';

import { SalesChannelFormService } from './sales-channel-form.service';

describe('SalesChannel Form Service', () => {
  let service: SalesChannelFormService;

  beforeEach(() => {
    service = TestBed.inject(SalesChannelFormService);
  });

  describe('Service methods', () => {
    describe('createSalesChannelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalesChannelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            sequenceNo: expect.any(Object),
          }),
        );
      });

      it('passing ISalesChannel should create a new form with FormGroup', () => {
        const formGroup = service.createSalesChannelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            sequenceNo: expect.any(Object),
          }),
        );
      });
    });

    describe('getSalesChannel', () => {
      it('should return NewSalesChannel for default SalesChannel initial value', () => {
        const formGroup = service.createSalesChannelFormGroup(sampleWithNewData);

        const salesChannel = service.getSalesChannel(formGroup);

        expect(salesChannel).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalesChannel for empty SalesChannel initial value', () => {
        const formGroup = service.createSalesChannelFormGroup();

        const salesChannel = service.getSalesChannel(formGroup);

        expect(salesChannel).toMatchObject({});
      });

      it('should return ISalesChannel', () => {
        const formGroup = service.createSalesChannelFormGroup(sampleWithRequiredData);

        const salesChannel = service.getSalesChannel(formGroup);

        expect(salesChannel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalesChannel should not enable id FormControl', () => {
        const formGroup = service.createSalesChannelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalesChannel should disable id FormControl', () => {
        const formGroup = service.createSalesChannelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
