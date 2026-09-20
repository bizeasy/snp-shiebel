import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../lot.test-samples';

import { LotFormService } from './lot-form.service';

describe('Lot Form Service', () => {
  let service: LotFormService;

  beforeEach(() => {
    service = TestBed.inject(LotFormService);
  });

  describe('Service methods', () => {
    describe('createLotFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLotFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            quantity: expect.any(Object),
            expirationDate: expect.any(Object),
            retestDate: expect.any(Object),
          }),
        );
      });

      it('passing ILot should create a new form with FormGroup', () => {
        const formGroup = service.createLotFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            creationDate: expect.any(Object),
            quantity: expect.any(Object),
            expirationDate: expect.any(Object),
            retestDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getLot', () => {
      it('should return NewLot for default Lot initial value', () => {
        const formGroup = service.createLotFormGroup(sampleWithNewData);

        const lot = service.getLot(formGroup);

        expect(lot).toMatchObject(sampleWithNewData);
      });

      it('should return NewLot for empty Lot initial value', () => {
        const formGroup = service.createLotFormGroup();

        const lot = service.getLot(formGroup);

        expect(lot).toMatchObject({});
      });

      it('should return ILot', () => {
        const formGroup = service.createLotFormGroup(sampleWithRequiredData);

        const lot = service.getLot(formGroup);

        expect(lot).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILot should not enable id FormControl', () => {
        const formGroup = service.createLotFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLot should disable id FormControl', () => {
        const formGroup = service.createLotFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
