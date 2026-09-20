export interface IProductFeatureGroup {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductFeatureGroup = Omit<IProductFeatureGroup, 'id'> & { id: null };
