import { IPartyIdentification, NewPartyIdentification } from './party-identification.model';

export const sampleWithRequiredData: IPartyIdentification = {
  id: 4809,
  idValue: 'swift coordination next',
};

export const sampleWithPartialData: IPartyIdentification = {
  id: 4780,
  idValue: 'ugh metabolite',
};

export const sampleWithFullData: IPartyIdentification = {
  id: 8187,
  idValue: 'hmph inquisitively',
};

export const sampleWithNewData: NewPartyIdentification = {
  idValue: 'actually piglet sad',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
