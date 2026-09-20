import dayjs from 'dayjs/esm';

import { ICustRequest } from 'app/entities/cust-request/cust-request.model';
import { IParty } from 'app/entities/party/party.model';
import { IProductStore } from 'app/entities/product-store/product-store.model';
import { IQuoteType } from 'app/entities/quote-type/quote-type.model';
import { ISalesChannel } from 'app/entities/sales-channel/sales-channel.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';
import { IUomConversion } from 'app/entities/uom-conversion/uom-conversion.model';

export interface IQuote {
  id: number;
  name?: string | null;
  offerNo?: string | null;
  issueDate?: dayjs.Dayjs | null;
  validFromDate?: dayjs.Dayjs | null;
  validThruDate?: dayjs.Dayjs | null;
  description?: string | null;
  offerValue?: number | null;
  remarks?: string | null;
  quoteType?: IQuoteType | null;
  custRequest?: ICustRequest | null;
  party?: IParty | null;
  status?: IStatus | null;
  productStore?: IProductStore | null;
  salesChannel?: ISalesChannel | null;
  currencyUom?: IUom | null;
  currencyConversion?: IUomConversion | null;
}

export type NewQuote = Omit<IQuote, 'id'> & { id: null };
