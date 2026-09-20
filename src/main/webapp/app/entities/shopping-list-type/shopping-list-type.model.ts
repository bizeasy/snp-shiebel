export interface IShoppingListType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewShoppingListType = Omit<IShoppingListType, 'id'> & { id: null };
