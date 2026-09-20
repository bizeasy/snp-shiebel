import { IPaymentGatewayConfig } from 'app/entities/payment-gateway-config/payment-gateway-config.model';
import { IPaymentMethodType } from 'app/entities/payment-method-type/payment-method-type.model';

export interface IPaymentGatewayResponse {
  id: number;
  referenceNumber?: string | null;
  amount?: number | null;
  gatewayMessage?: string | null;
  paymentGatewayConfig?: IPaymentGatewayConfig | null;
  paymentMethodType?: IPaymentMethodType | null;
}

export type NewPaymentGatewayResponse = Omit<IPaymentGatewayResponse, 'id'> & { id: null };
