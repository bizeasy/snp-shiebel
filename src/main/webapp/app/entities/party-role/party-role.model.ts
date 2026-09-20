import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IPartyRole {
  id: number;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewPartyRole = Omit<IPartyRole, 'id'> & { id: null };
