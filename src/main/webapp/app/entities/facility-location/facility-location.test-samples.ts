import { IFacilityLocation, NewFacilityLocation } from './facility-location.model';

export const sampleWithRequiredData: IFacilityLocation = {
  id: 15547,
  locationSeqId: 'cautious pish',
};

export const sampleWithPartialData: IFacilityLocation = {
  id: 10427,
  locationSeqId: 'entomb',
  sectionId: 'starch wallop even',
  levelId: 'confound',
};

export const sampleWithFullData: IFacilityLocation = {
  id: 10918,
  locationSeqId: 'sweatshop inspection',
  areaId: 'pish despite cheerfu',
  aisleId: 'huff',
  sectionId: 'cruelly afore',
  levelId: 'now thankfully',
  positionId: 'redesign proceed enc',
};

export const sampleWithNewData: NewFacilityLocation = {
  locationSeqId: 'strictly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
