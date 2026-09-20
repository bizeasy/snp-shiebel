import dayjs from 'dayjs/esm';

import { IFacility } from 'app/entities/facility/facility.model';
import { IParty } from 'app/entities/party/party.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IPickList {
  id: number;
  description?: string | null;
  pickListDate?: dayjs.Dayjs | null;
  facility?: IFacility | null;
  status?: IStatus | null;
  party?: IParty | null;
}

export type NewPickList = Omit<IPickList, 'id'> & { id: null };
