import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../file-share.test-samples';

import { FileShareFormService } from './file-share-form.service';

describe('FileShare Form Service', () => {
  let service: FileShareFormService;

  beforeEach(() => {
    service = TestBed.inject(FileShareFormService);
  });

  describe('Service methods', () => {
    describe('createFileShareFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFileShareFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            permission: expect.any(Object),
            sharedWithEmail: expect.any(Object),
            sharedDate: expect.any(Object),
            expiryDate: expect.any(Object),
            accessToken: expect.any(Object),
            isRevoked: expect.any(Object),
            folder: expect.any(Object),
            attachment: expect.any(Object),
            sharedWith: expect.any(Object),
            sharedBy: expect.any(Object),
          }),
        );
      });

      it('passing IFileShare should create a new form with FormGroup', () => {
        const formGroup = service.createFileShareFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            permission: expect.any(Object),
            sharedWithEmail: expect.any(Object),
            sharedDate: expect.any(Object),
            expiryDate: expect.any(Object),
            accessToken: expect.any(Object),
            isRevoked: expect.any(Object),
            folder: expect.any(Object),
            attachment: expect.any(Object),
            sharedWith: expect.any(Object),
            sharedBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getFileShare', () => {
      it('should return NewFileShare for default FileShare initial value', () => {
        const formGroup = service.createFileShareFormGroup(sampleWithNewData);

        const fileShare = service.getFileShare(formGroup);

        expect(fileShare).toMatchObject(sampleWithNewData);
      });

      it('should return NewFileShare for empty FileShare initial value', () => {
        const formGroup = service.createFileShareFormGroup();

        const fileShare = service.getFileShare(formGroup);

        expect(fileShare).toMatchObject({});
      });

      it('should return IFileShare', () => {
        const formGroup = service.createFileShareFormGroup(sampleWithRequiredData);

        const fileShare = service.getFileShare(formGroup);

        expect(fileShare).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFileShare should not enable id FormControl', () => {
        const formGroup = service.createFileShareFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFileShare should disable id FormControl', () => {
        const formGroup = service.createFileShareFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
