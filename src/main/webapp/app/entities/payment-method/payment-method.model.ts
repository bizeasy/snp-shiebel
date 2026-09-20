import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';

export interface IPaymentMethod {
  id: number;
  name?: string | null;
  description?: string | null;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  paymentMethodType?: IPaymentMethodType | null;
  party?: IParty | null;
}

export type NewPaymentMethod = Omit<IPaymentMethod, 'id'> & { id: null };
