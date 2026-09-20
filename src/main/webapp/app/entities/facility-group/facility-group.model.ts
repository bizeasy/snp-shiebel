import { IFacilityGroupType } from 'app/entities/facility-group-type/facility-group-type.model';

export interface IFacilityGroup {
  id: number;
  name?: string | null;
  description?: string | null;
  facilityGroupType?: IFacilityGroupType | null;
  parentFacilityGroup?: IFacilityGroup | null;
}

export type NewFacilityGroup = Omit<IFacilityGroup, 'id'> & { id: null };
