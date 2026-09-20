import { IInvoiceItemType } from 'app/entities/invoice-item-type/invoice-item-type.model';
import { IInvoiceType } from 'app/entities/invoice-type/invoice-type.model';

export interface IInvoiceItemTypeMap {
  id: number;
  invoiceItemMapKey?: string | null;
  invoiceType?: IInvoiceType | null;
  invoiceItemType?: IInvoiceItemType | null;
}

export type NewInvoiceItemTypeMap = Omit<IInvoiceItemTypeMap, 'id'> & { id: null };
