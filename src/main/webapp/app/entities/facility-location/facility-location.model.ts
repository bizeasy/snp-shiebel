import { IFacility } from 'app/entities/facility/facility.model';
import { ILocationType } from 'app/entities/location-type/location-type.model';

export interface IFacilityLocation {
  id: number;
  locationSeqId?: string | null;
  areaId?: string | null;
  aisleId?: string | null;
  sectionId?: string | null;
  levelId?: string | null;
  positionId?: string | null;
  facility?: IFacility | null;
  locationType?: ILocationType | null;
}

export type NewFacilityLocation = Omit<IFacilityLocation, 'id'> & { id: null };
