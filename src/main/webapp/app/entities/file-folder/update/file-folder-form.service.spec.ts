import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../file-folder.test-samples';

import { FileFolderFormService } from './file-folder-form.service';

describe('FileFolder Form Service', () => {
  let service: FileFolderFormService;

  beforeEach(() => {
    service = TestBed.inject(FileFolderFormService);
  });

  describe('Service methods', () => {
    describe('createFileFolderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFileFolderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            path: expect.any(Object),
            isSystemFolder: expect.any(Object),
            isTrashed: expect.any(Object),
            createdDate: expect.any(Object),
            parent: expect.any(Object),
            owner: expect.any(Object),
          }),
        );
      });

      it('passing IFileFolder should create a new form with FormGroup', () => {
        const formGroup = service.createFileFolderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            path: expect.any(Object),
            isSystemFolder: expect.any(Object),
            isTrashed: expect.any(Object),
            createdDate: expect.any(Object),
            parent: expect.any(Object),
            owner: expect.any(Object),
          }),
        );
      });
    });

    describe('getFileFolder', () => {
      it('should return NewFileFolder for default FileFolder initial value', () => {
        const formGroup = service.createFileFolderFormGroup(sampleWithNewData);

        const fileFolder = service.getFileFolder(formGroup);

        expect(fileFolder).toMatchObject(sampleWithNewData);
      });

      it('should return NewFileFolder for empty FileFolder initial value', () => {
        const formGroup = service.createFileFolderFormGroup();

        const fileFolder = service.getFileFolder(formGroup);

        expect(fileFolder).toMatchObject({});
      });

      it('should return IFileFolder', () => {
        const formGroup = service.createFileFolderFormGroup(sampleWithRequiredData);

        const fileFolder = service.getFileFolder(formGroup);

        expect(fileFolder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFileFolder should not enable id FormControl', () => {
        const formGroup = service.createFileFolderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFileFolder should disable id FormControl', () => {
        const formGroup = service.createFileFolderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
