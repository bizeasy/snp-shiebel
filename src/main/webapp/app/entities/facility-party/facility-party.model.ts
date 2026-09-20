import dayjs from 'dayjs/esm';

import { IFacility } from 'app/entities/facility/facility.model';
import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IFacilityParty {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  facility?: IFacility | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewFacilityParty = Omit<IFacilityParty, 'id'> & { id: null };
