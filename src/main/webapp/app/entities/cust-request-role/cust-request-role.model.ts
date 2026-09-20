import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { ICustRequestItem } from 'app/entities/cust-request-item/cust-request-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface ICustRequestRole {
  id: number;
  custRequest?: ICustRequest | null;
  custRequestItem?: ICustRequestItem | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewCustRequestRole = Omit<ICustRequestRole, 'id'> & { id: null };
