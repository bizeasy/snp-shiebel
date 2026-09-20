import dayjs from 'dayjs/esm';

import { IAttachment, NewAttachment } from './attachment.model';

export const sampleWithRequiredData: IAttachment = {
  id: 15526,
  name: 'ouch',
};

export const sampleWithPartialData: IAttachment = {
  id: 25247,
  name: 'yellow fixed quickly',
  fileAttachment: '../fake-data/blob/hipster.png',
  fileAttachmentContentType: 'unknown',
  attachmentUrl: 'like lawful bah',
  fileSize: 26377,
  checksum: 'arrogantly',
};

export const sampleWithFullData: IAttachment = {
  id: 21727,
  name: 'hierarchy ha editor',
  fileAttachment: '../fake-data/blob/hipster.png',
  fileAttachmentContentType: 'unknown',
  attachmentUrl: 'unless',
  mimeType: 'outside long',
  fileSize: 25433,
  uploadedDate: dayjs('2023-12-14T07:36'),
  isTrashed: false,
  checksum: 'but voluntarily pish',
};

export const sampleWithNewData: NewAttachment = {
  name: 'fatally responsibility',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
