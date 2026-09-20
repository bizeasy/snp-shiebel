export interface IOrderAdjustmentType {
  id: number;
  name?: string | null;
  description?: string | null;
  parent?: IOrderAdjustmentType | null;
}

export type NewOrderAdjustmentType = Omit<IOrderAdjustmentType, 'id'> & { id: null };
