import dayjs from 'dayjs/esm';

import { IProduct } from 'app/entities/product/product.model';
import { IShoppingList } from 'app/entities/shopping-list/shopping-list.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IShoppingListItem {
  id: number;
  sequenceNo?: number | null;
  quantity?: number | null;
  requiredByDate?: dayjs.Dayjs | null;
  selectedAmount?: number | null;
  maxAmount?: number | null;
  description?: string | null;
  story?: string | null;
  shoppingList?: IShoppingList | null;
  product?: IProduct | null;
  status?: IStatus | null;
}

export type NewShoppingListItem = Omit<IShoppingListItem, 'id'> & { id: null };
