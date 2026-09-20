import { ISalesChannel, NewSalesChannel } from './sales-channel.model';

export const sampleWithRequiredData: ISalesChannel = {
  id: 9291,
  name: 'regal pleasure unripe',
};

export const sampleWithPartialData: ISalesChannel = {
  id: 15293,
  name: 'showy newsletter',
  description: 'stock knowingly inasmuch',
  sequenceNo: 19424,
};

export const sampleWithFullData: ISalesChannel = {
  id: 13460,
  name: 'ferociously maroon',
  description: 'especially',
  sequenceNo: 1473,
};

export const sampleWithNewData: NewSalesChannel = {
  name: 'tennis rebound',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
