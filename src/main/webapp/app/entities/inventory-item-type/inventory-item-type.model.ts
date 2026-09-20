export interface IInventoryItemType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewInventoryItemType = Omit<IInventoryItemType, 'id'> & { id: null };
