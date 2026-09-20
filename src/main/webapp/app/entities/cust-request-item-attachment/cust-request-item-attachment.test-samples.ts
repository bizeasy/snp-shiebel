import { ICustRequestItemAttachment, NewCustRequestItemAttachment } from './cust-request-item-attachment.model';

export const sampleWithRequiredData: ICustRequestItemAttachment = {
  id: 22141,
};

export const sampleWithPartialData: ICustRequestItemAttachment = {
  id: 17433,
  name: 'certainly than of',
};

export const sampleWithFullData: ICustRequestItemAttachment = {
  id: 18338,
  name: 'immediate yippee',
};

export const sampleWithNewData: NewCustRequestItemAttachment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
