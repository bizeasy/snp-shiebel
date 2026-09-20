import dayjs from 'dayjs/esm';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { IFacility } from 'app/entities/facility/facility.model';

export interface IFacilityContactMech {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  facility?: IFacility | null;
  contactMech?: IContactMech | null;
  contactMechPurpose?: IContactMechPurpose | null;
}

export type NewFacilityContactMech = Omit<IFacilityContactMech, 'id'> & { id: null };
