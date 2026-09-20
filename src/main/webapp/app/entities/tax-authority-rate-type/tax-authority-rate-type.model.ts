import { ITaxAuthority } from 'app/entities/tax-authority/tax-authority.model';
import { ITaxSlab } from 'app/entities/tax-slab/tax-slab.model';

export interface ITaxAuthorityRateType {
  id: number;
  name?: string | null;
  description?: string | null;
  taxAuthority?: ITaxAuthority | null;
  taxSlab?: ITaxSlab | null;
}

export type NewTaxAuthorityRateType = Omit<ITaxAuthorityRateType, 'id'> & { id: null };
