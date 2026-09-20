import { IFacilityCarrierShipment, NewFacilityCarrierShipment } from './facility-carrier-shipment.model';

export const sampleWithRequiredData: IFacilityCarrierShipment = {
  id: 1376,
};

export const sampleWithPartialData: IFacilityCarrierShipment = {
  id: 23273,
};

export const sampleWithFullData: IFacilityCarrierShipment = {
  id: 31089,
};

export const sampleWithNewData: NewFacilityCarrierShipment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
