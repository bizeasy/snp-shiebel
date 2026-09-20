import dayjs from 'dayjs/esm';

import { ICustomTimePeriod } from 'app/entities/custom-time-period/custom-time-period.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IFacility } from 'app/entities/facility/facility.model';
import { IOrderType } from 'app/entities/order-type/order-type.model';
import { IParty } from 'app/entities/party/party.model';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { IQuote } from 'app/entities/quote/quote.model';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';
import { IUomConversion } from 'app/entities/uom-conversion/uom-conversion.model';

export interface IOrder {
  id: number;
  name?: string | null;
  orderRefNo?: string | null;
  externalId?: string | null;
  orderDate?: dayjs.Dayjs | null;
  entryDate?: dayjs.Dayjs | null;
  deliveryDate?: dayjs.Dayjs | null;
  priority?: number | null;
  isRushOrder?: boolean | null;
  needsInventoryIssuance?: boolean | null;
  remainingSubTotal?: number | null;
  grandTotal?: number | null;
  projectName?: string | null;
  version?: number | null;
  versionRef?: string | null;
  description?: string | null;
  remarks?: string | null;
  paymentTerms?: string | null;
  deliveryTerms?: string | null;
  info?: string | null;
  attr1?: string | null;
  attr2?: string | null;
  attr3?: string | null;
  orderType?: IOrderType | null;
  salesChannel?: ISalesChannel | null;
  party?: IParty | null;
  supplier?: IParty | null;
  status?: IStatus | null;
  currencyUom?: IUom | null;
  currencyConversion?: IUomConversion | null;
  quote?: IQuote | null;
  facility?: IFacility | null;
  productStore?: IProductStore | null;
  department?: IDepartment | null;
  customTimePeriod?: ICustomTimePeriod | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
