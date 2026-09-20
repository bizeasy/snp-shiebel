import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';
import { IPartyRelationshipType } from 'app/entities/party-relationship-type/party-relationship-type.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IPartyRelationship {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  relationshipName?: string | null;
  partyFrom?: IParty | null;
  partyTo?: IParty | null;
  roleTypeFrom?: IRoleType | null;
  roleTypeTo?: IRoleType | null;
  relationshipType?: IPartyRelationshipType | null;
}

export type NewPartyRelationship = Omit<IPartyRelationship, 'id'> & { id: null };
