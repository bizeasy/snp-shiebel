import dayjs from 'dayjs/esm';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { IProduct } from 'app/entities/product/product.model';
import { IShoppingListItem } from 'app/entities/shopping-list-item/shopping-list-item.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface ICustRequestItem {
  id: number;
  sequenceNo?: number | null;
  priority?: number | null;
  requiredByDate?: dayjs.Dayjs | null;
  quantity?: number | null;
  selectedAmount?: number | null;
  maxAmount?: number | null;
  description?: string | null;
  story?: string | null;
  custRequest?: ICustRequest | null;
  status?: IStatus | null;
  product?: IProduct | null;
  uom?: IUom | null;
  shoppingListItem?: IShoppingListItem | null;
}

export type NewCustRequestItem = Omit<ICustRequestItem, 'id'> & { id: null };
