import { IParty } from 'app/entities/party/party.model';
import { IProductStore } from 'app/entities/product-store/product-store.model';

export interface IDepartment {
  id: number;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  parent?: IDepartment | null;
  head?: IParty | null;
  productStore?: IProductStore | null;
}

export type NewDepartment = Omit<IDepartment, 'id'> & { id: null };
