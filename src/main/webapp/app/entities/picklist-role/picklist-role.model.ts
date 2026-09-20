import { IParty } from 'app/entities/party/party.model';
import { IPickList } from 'app/entities/pick-list/pick-list.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IPicklistRole {
  id: number;
  pickList?: IPickList | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewPicklistRole = Omit<IPicklistRole, 'id'> & { id: null };
