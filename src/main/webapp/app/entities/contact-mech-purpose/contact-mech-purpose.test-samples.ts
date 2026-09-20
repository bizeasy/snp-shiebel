import { IContactMechPurpose, NewContactMechPurpose } from './contact-mech-purpose.model';

export const sampleWithRequiredData: IContactMechPurpose = {
  id: 30526,
  name: 'along',
};

export const sampleWithPartialData: IContactMechPurpose = {
  id: 8601,
  name: 'oof following',
};

export const sampleWithFullData: IContactMechPurpose = {
  id: 28431,
  name: 'partially',
  description: 'considering',
};

export const sampleWithNewData: NewContactMechPurpose = {
  name: 'broken quickly before',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
