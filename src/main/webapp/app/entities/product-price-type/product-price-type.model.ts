export interface IProductPriceType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductPriceType = Omit<IProductPriceType, 'id'> & { id: null };
