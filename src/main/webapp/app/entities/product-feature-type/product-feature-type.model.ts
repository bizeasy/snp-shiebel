export interface IProductFeatureType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductFeatureType = Omit<IProductFeatureType, 'id'> & { id: null };
