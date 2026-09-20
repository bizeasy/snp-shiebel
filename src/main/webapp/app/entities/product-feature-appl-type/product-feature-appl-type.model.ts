export interface IProductFeatureApplType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductFeatureApplType = Omit<IProductFeatureApplType, 'id'> & { id: null };
