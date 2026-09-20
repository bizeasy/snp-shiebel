import dayjs from 'dayjs/esm';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IContactMechPurpose } from 'app/entities/contact-mech-purpose/contact-mech-purpose.model';
import { IParty } from 'app/entities/party/party.model';

export interface IPartyContactMech {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  party?: IParty | null;
  contactMech?: IContactMech | null;
  contactMechPurpose?: IContactMechPurpose | null;
}

export type NewPartyContactMech = Omit<IPartyContactMech, 'id'> & { id: null };
