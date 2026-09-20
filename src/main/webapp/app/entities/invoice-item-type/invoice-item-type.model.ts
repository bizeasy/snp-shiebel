export interface IInvoiceItemType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewInvoiceItemType = Omit<IInvoiceItemType, 'id'> & { id: null };
