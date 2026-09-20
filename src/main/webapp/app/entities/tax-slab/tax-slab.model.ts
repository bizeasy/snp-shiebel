export interface ITaxSlab {
  id: number;
  name?: string | null;
  rate?: number | null;
}

export type NewTaxSlab = Omit<ITaxSlab, 'id'> & { id: null };
