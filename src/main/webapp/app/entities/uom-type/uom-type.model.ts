export interface IUomType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewUomType = Omit<IUomType, 'id'> & { id: null };
