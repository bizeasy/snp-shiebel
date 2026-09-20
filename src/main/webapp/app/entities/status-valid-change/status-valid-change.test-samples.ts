import { IStatusValidChange, NewStatusValidChange } from './status-valid-change.model';

export const sampleWithRequiredData: IStatusValidChange = {
  id: 26206,
};

export const sampleWithPartialData: IStatusValidChange = {
  id: 19838,
  transitionName: 'testify',
};

export const sampleWithFullData: IStatusValidChange = {
  id: 19903,
  transitionName: 'although',
};

export const sampleWithNewData: NewStatusValidChange = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
