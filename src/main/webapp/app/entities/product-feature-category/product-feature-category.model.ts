export interface IProductFeatureCategory {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductFeatureCategory = Omit<IProductFeatureCategory, 'id'> & { id: null };
