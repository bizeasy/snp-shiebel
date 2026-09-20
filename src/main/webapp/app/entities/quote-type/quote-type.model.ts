export interface IQuoteType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewQuoteType = Omit<IQuoteType, 'id'> & { id: null };
