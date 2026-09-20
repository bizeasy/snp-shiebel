import dayjs from 'dayjs/esm';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IInvoiceType } from 'app/entities/invoice-type/invoice-type.model';
import { IOrder } from 'app/entities/order/order.model';
import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IInvoice {
  id: number;
  invoiceDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  paidDate?: dayjs.Dayjs | null;
  referenceNumber?: string | null;
  invoiceMessage?: string | null;
  externalId?: string | null;
  entryDate?: dayjs.Dayjs | null;
  grandTotal?: number | null;
  projectName?: string | null;
  description?: string | null;
  remarks?: string | null;
  paymentTerms?: string | null;
  deliveryTerms?: string | null;
  info?: string | null;
  invoiceType?: IInvoiceType | null;
  order?: IOrder | null;
  fromParty?: IParty | null;
  toParty?: IParty | null;
  roleType?: IRoleType | null;
  status?: IStatus | null;
  contactMech?: IContactMech | null;
  currencyUom?: IUom | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
