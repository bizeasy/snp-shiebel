import { ICustRequestRole, NewCustRequestRole } from './cust-request-role.model';

export const sampleWithRequiredData: ICustRequestRole = {
  id: 19994,
};

export const sampleWithPartialData: ICustRequestRole = {
  id: 10016,
};

export const sampleWithFullData: ICustRequestRole = {
  id: 23982,
};

export const sampleWithNewData: NewCustRequestRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
