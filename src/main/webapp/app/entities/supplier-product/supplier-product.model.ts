import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface ISupplierProduct {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  supplierProductId?: string | null;
  minOrderQty?: number | null;
  lastPrice?: number | null;
  shippingPrice?: number | null;
  cgst?: number | null;
  igst?: number | null;
  sgst?: number | null;
  totalPrice?: number | null;
  comments?: string | null;
  leadTimeDays?: number | null;
  unitsIncluded?: number | null;
  canDropShip?: boolean | null;
  product?: IProduct | null;
  party?: IParty | null;
  quantityUom?: IUom | null;
  currencyUom?: IUom | null;
}

export type NewSupplierProduct = Omit<ISupplierProduct, 'id'> & { id: null };
