export interface IPaymentMethodType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewPaymentMethodType = Omit<IPaymentMethodType, 'id'> & { id: null };
