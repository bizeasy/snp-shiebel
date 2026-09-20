import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IPayment } from 'app/entities/payment/payment.model';

export interface IPaymentApplication {
  id: number;
  amountApplied?: number | null;
  payment?: IPayment | null;
  toPayment?: IPayment | null;
  invoice?: IInvoice | null;
  invoiceItem?: IInvoiceItem | null;
  order?: IOrder | null;
  orderItem?: IOrderItem | null;
}

export type NewPaymentApplication = Omit<IPaymentApplication, 'id'> & { id: null };
