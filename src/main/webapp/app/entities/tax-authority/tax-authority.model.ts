import { IGeo } from 'app/entities/geo/geo.model';

export interface ITaxAuthority {
  id: number;
  name?: string | null;
  geo?: IGeo | null;
}

export type NewTaxAuthority = Omit<ITaxAuthority, 'id'> & { id: null };
