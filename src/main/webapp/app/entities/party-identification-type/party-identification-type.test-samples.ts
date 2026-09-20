import { IPartyIdentificationType, NewPartyIdentificationType } from './party-identification-type.model';

export const sampleWithRequiredData: IPartyIdentificationType = {
  id: 22598,
  name: 'spotless who',
};

export const sampleWithPartialData: IPartyIdentificationType = {
  id: 10467,
  name: 'whoa mill',
  description: 'when bewail caption',
};

export const sampleWithFullData: IPartyIdentificationType = {
  id: 22345,
  name: 'up',
  description: 'among cassava',
};

export const sampleWithNewData: NewPartyIdentificationType = {
  name: 'sweetly darn',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
