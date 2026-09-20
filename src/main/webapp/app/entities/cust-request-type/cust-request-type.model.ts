import { IParty } from 'app/entities/party/party.model';

export interface ICustRequestType {
  id: number;
  name?: string | null;
  description?: string | null;
  parent?: ICustRequestType | null;
  party?: IParty | null;
}

export type NewCustRequestType = Omit<ICustRequestType, 'id'> & { id: null };
