export interface IProductAssocType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductAssocType = Omit<IProductAssocType, 'id'> & { id: null };
