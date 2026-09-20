import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../keyword-type.test-samples';

import { KeywordTypeFormService } from './keyword-type-form.service';

describe('KeywordType Form Service', () => {
  let service: KeywordTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(KeywordTypeFormService);
  });

  describe('Service methods', () => {
    describe('createKeywordTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKeywordTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IKeywordType should create a new form with FormGroup', () => {
        const formGroup = service.createKeywordTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getKeywordType', () => {
      it('should return NewKeywordType for default KeywordType initial value', () => {
        const formGroup = service.createKeywordTypeFormGroup(sampleWithNewData);

        const keywordType = service.getKeywordType(formGroup);

        expect(keywordType).toMatchObject(sampleWithNewData);
      });

      it('should return NewKeywordType for empty KeywordType initial value', () => {
        const formGroup = service.createKeywordTypeFormGroup();

        const keywordType = service.getKeywordType(formGroup);

        expect(keywordType).toMatchObject({});
      });

      it('should return IKeywordType', () => {
        const formGroup = service.createKeywordTypeFormGroup(sampleWithRequiredData);

        const keywordType = service.getKeywordType(formGroup);

        expect(keywordType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKeywordType should not enable id FormControl', () => {
        const formGroup = service.createKeywordTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKeywordType should disable id FormControl', () => {
        const formGroup = service.createKeywordTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
