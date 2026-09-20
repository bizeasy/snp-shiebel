import { IContactMech, NewContactMech } from './contact-mech.model';

export const sampleWithRequiredData: IContactMech = {
  id: 26279,
};

export const sampleWithPartialData: IContactMech = {
  id: 18668,
};

export const sampleWithFullData: IContactMech = {
  id: 14354,
  infoString: 'although yippee draw',
};

export const sampleWithNewData: NewContactMech = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
