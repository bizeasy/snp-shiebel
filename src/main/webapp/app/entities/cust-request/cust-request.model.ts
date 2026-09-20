import dayjs from 'dayjs/esm';

import { ICustRequestCategory } from 'app/entities/cust-request-category/cust-request-category.model';
import { ICustRequestType } from 'app/entities/cust-request-type/cust-request-type.model';
import { IParty } from 'app/entities/party/party.model';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface ICustRequest {
  id: number;
  name?: string | null;
  description?: string | null;
  priority?: number | null;
  custRequestDate?: dayjs.Dayjs | null;
  responseRequiredDate?: dayjs.Dayjs | null;
  openDateTime?: dayjs.Dayjs | null;
  closedDateTime?: dayjs.Dayjs | null;
  internalComment?: string | null;
  billed?: boolean | null;
  custEstimatedDurationDays?: number | null;
  custRequestType?: ICustRequestType | null;
  custRequestCategory?: ICustRequestCategory | null;
  status?: IStatus | null;
  fromParty?: IParty | null;
  productStore?: IProductStore | null;
  salesChannel?: ISalesChannel | null;
  currencyUom?: IUom | null;
}

export type NewCustRequest = Omit<ICustRequest, 'id'> & { id: null };
