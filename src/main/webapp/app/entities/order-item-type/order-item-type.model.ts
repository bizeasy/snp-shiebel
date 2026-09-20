export interface IOrderItemType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewOrderItemType = Omit<IOrderItemType, 'id'> & { id: null };
