import dayjs from 'dayjs/esm';

import { IFacility } from 'app/entities/facility/facility.model';
import { IFacilityGroup } from 'app/entities/facility-group/facility-group.model';

export interface IFacilityGroupMember {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  sequenceNo?: number | null;
  facility?: IFacility | null;
  facilityGroup?: IFacilityGroup | null;
}

export type NewFacilityGroupMember = Omit<IFacilityGroupMember, 'id'> & { id: null };
