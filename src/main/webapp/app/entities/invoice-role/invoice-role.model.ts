import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IInvoiceRole {
  id: number;
  invoice?: IInvoice | null;
  invoiceItem?: IInvoiceItem | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewInvoiceRole = Omit<IInvoiceRole, 'id'> & { id: null };
