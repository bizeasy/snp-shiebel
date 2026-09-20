export interface IInvoiceType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewInvoiceType = Omit<IInvoiceType, 'id'> & { id: null };
