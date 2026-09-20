export interface IOrderType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewOrderType = Omit<IOrderType, 'id'> & { id: null };
