import { ICatalogue, NewCatalogue } from './catalogue.model';

export const sampleWithRequiredData: ICatalogue = {
  id: 15807,
  name: 'gullible',
};

export const sampleWithPartialData: ICatalogue = {
  id: 27371,
  name: 'pfft muddy',
  sequenceNo: 26685,
};

export const sampleWithFullData: ICatalogue = {
  id: 5490,
  name: 'longingly wilderness',
  description: 'but likewise reschedule',
  sequenceNo: 24279,
  imagePath: 'reorganisation',
  mobileImagePath: 'geez nicely',
  altImage1: 'brr',
  altImage2: 'vainly whisper anguished',
  altImage3: 'wetly',
  bannerUrl: 'guacamole amnesty',
  isBanner: true,
};

export const sampleWithNewData: NewCatalogue = {
  name: 'barring function',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
