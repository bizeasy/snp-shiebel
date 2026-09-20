import { ICustRequestAttachment, NewCustRequestAttachment } from './cust-request-attachment.model';

export const sampleWithRequiredData: ICustRequestAttachment = {
  id: 4894,
};

export const sampleWithPartialData: ICustRequestAttachment = {
  id: 22100,
  name: 'weary hmph geez',
};

export const sampleWithFullData: ICustRequestAttachment = {
  id: 16973,
  name: 'mid aw drat',
};

export const sampleWithNewData: NewCustRequestAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
