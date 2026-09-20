export interface IProductPricePurpose {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewProductPricePurpose = Omit<IProductPricePurpose, 'id'> & { id: null };
