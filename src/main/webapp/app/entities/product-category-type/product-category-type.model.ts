export interface IProductCategoryType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductCategoryType = Omit<IProductCategoryType, 'id'> & { id: null };
