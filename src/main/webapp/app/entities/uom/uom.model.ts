import { IUomType } from 'app/entities/uom-type/uom-type.model';

export interface IUom {
  id: number;
  name?: string | null;
  description?: string | null;
  sequenceNo?: number | null;
  abbreviation?: string | null;
  uomType?: IUomType | null;
}

export type NewUom = Omit<IUom, 'id'> & { id: null };
