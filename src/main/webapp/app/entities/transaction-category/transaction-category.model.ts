export interface ITransactionCategory {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewTransactionCategory = Omit<ITransactionCategory, 'id'> & { id: null };
