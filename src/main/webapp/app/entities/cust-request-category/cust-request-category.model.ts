import { ICustRequestType } from 'app/entities/cust-request-type/cust-request-type.model';

export interface ICustRequestCategory {
  id: number;
  name?: string | null;
  description?: string | null;
  custRequestType?: ICustRequestType | null;
}

export type NewCustRequestCategory = Omit<ICustRequestCategory, 'id'> & { id: null };
