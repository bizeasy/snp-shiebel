import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../content-type.test-samples';

import { ContentTypeFormService } from './content-type-form.service';

describe('ContentType Form Service', () => {
  let service: ContentTypeFormService;

  beforeEach(() => {
    service = TestBed.inject(ContentTypeFormService);
  });

  describe('Service methods', () => {
    describe('createContentTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContentTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IContentType should create a new form with FormGroup', () => {
        const formGroup = service.createContentTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getContentType', () => {
      it('should return NewContentType for default ContentType initial value', () => {
        const formGroup = service.createContentTypeFormGroup(sampleWithNewData);

        const contentType = service.getContentType(formGroup);

        expect(contentType).toMatchObject(sampleWithNewData);
      });

      it('should return NewContentType for empty ContentType initial value', () => {
        const formGroup = service.createContentTypeFormGroup();

        const contentType = service.getContentType(formGroup);

        expect(contentType).toMatchObject({});
      });

      it('should return IContentType', () => {
        const formGroup = service.createContentTypeFormGroup(sampleWithRequiredData);

        const contentType = service.getContentType(formGroup);

        expect(contentType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContentType should not enable id FormControl', () => {
        const formGroup = service.createContentTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContentType should disable id FormControl', () => {
        const formGroup = service.createContentTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
