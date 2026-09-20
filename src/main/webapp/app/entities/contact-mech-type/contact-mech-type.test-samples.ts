import { IContactMechType, NewContactMechType } from './contact-mech-type.model';

export const sampleWithRequiredData: IContactMechType = {
  id: 11111,
  name: 'afterwards',
};

export const sampleWithPartialData: IContactMechType = {
  id: 18945,
  name: 'snack',
  description: 'quip till',
};

export const sampleWithFullData: IContactMechType = {
  id: 16177,
  name: 'digit stealthily ambitiou',
  description: 'microblog',
};

export const sampleWithNewData: NewContactMechType = {
  name: 'questionably elliptical',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
