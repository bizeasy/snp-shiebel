import { ITransactionCategory } from 'app/entities/transaction-category/transaction-category.model';

export interface ITransactionType {
  id: number;
  name?: string | null;
  description?: string | null;
  category?: ITransactionCategory | null;
}

export type NewTransactionType = Omit<ITransactionType, 'id'> & { id: null };
