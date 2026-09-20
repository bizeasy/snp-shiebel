import { IPostalAddress, NewPostalAddress } from './postal-address.model';

export const sampleWithRequiredData: IPostalAddress = {
  id: 25910,
};

export const sampleWithPartialData: IPostalAddress = {
  id: 12055,
  countryStr: 'outlaw unfinished loudly',
  isIndianAddress: false,
};

export const sampleWithFullData: IPostalAddress = {
  id: 31783,
  toName: 'deficient cop-out pivot',
  address1: 'wherever',
  address2: 'dutiful than',
  city: 'South August',
  landmark: 'mash',
  postalCode: 'monasticis',
  isDefault: true,
  customAddressType: 'for',
  stateStr: 'meh',
  countryStr: 'celsius',
  isIndianAddress: true,
  note: 'curly',
  directions: 'someplace queasily teammate',
};

export const sampleWithNewData: NewPostalAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
