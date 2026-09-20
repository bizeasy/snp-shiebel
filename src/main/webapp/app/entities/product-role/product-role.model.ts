import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IProductRole {
  id: number;
  product?: IProduct | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewProductRole = Omit<IProductRole, 'id'> & { id: null };
