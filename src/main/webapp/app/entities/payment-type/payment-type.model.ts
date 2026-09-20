export interface IPaymentType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewPaymentType = Omit<IPaymentType, 'id'> & { id: null };
