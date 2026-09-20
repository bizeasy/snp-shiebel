import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';
import { IPaymentGatewayResponse } from 'app/entities/payment-gateway-response/payment-gateway-response.model';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';
import { IPaymentType } from 'app/entities/payment-type/payment-type.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUom } from 'app/entities/uom/uom.model';

export interface IPayment {
  id: number;
  paymentRefNumber?: string | null;
  effectiveDate?: dayjs.Dayjs | null;
  paymentDate?: dayjs.Dayjs | null;
  amount?: number | null;
  actualAmount?: number | null;
  paymentStatus?: string | null;
  mihpayId?: string | null;
  txnId?: string | null;
  email?: string | null;
  phone?: string | null;
  productInfo?: string | null;
  paymentType?: IPaymentType | null;
  paymentMethodType?: IPaymentMethodType | null;
  paymentMethod?: IPaymentMethod | null;
  paymentGatewayResponse?: IPaymentGatewayResponse | null;
  status?: IStatus | null;
  fromParty?: IParty | null;
  toParty?: IParty | null;
  roleType?: IRoleType | null;
  currencyUom?: IUom | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
