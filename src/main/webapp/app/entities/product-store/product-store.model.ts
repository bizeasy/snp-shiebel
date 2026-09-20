import { IParty } from 'app/entities/party/party.model';

export interface IProductStore {
  id: number;
  name?: string | null;
  title?: string | null;
  postalAddress?: string | null;
  owner?: IParty | null;
  admin?: IParty | null;
}

export type NewProductStore = Omit<IProductStore, 'id'> & { id: null };
