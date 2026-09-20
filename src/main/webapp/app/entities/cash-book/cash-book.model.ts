import dayjs from 'dayjs/esm';

import { IDepartment } from 'app/entities/department/department.model';
import { RecurrenceFrequency } from 'app/entities/enumerations/recurrence-frequency.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IOrder } from 'app/entities/order/order.model';
import { IParty } from 'app/entities/party/party.model';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { IStatus } from 'app/entities/status/status.model';
import { ITransactionCategory } from 'app/entities/transaction-category/transaction-category.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface ICashBook {
  id: number;
  transactionDate?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  amount?: number | null;
  description?: string | null;
  vendorCustomerName?: string | null;
  invoiceNo?: string | null;
  invoiceDate?: dayjs.Dayjs | null;
  purpose?: string | null;
  remarks?: string | null;
  project?: string | null;
  usageUnits?: string | null;
  recurringTransaction?: boolean | null;
  frequency?: keyof typeof RecurrenceFrequency | null;
  transactionType?: ITransactionType | null;
  transactionCategory?: ITransactionCategory | null;
  paymentMethodType?: IPaymentMethodType | null;
  department?: IDepartment | null;
  currencyUom?: IUom | null;
  status?: IStatus | null;
  order?: IOrder | null;
  invoice?: IInvoice | null;
  fromParty?: IParty | null;
  toParty?: IParty | null;
}

export type NewCashBook = Omit<ICashBook, 'id'> & { id: null };
