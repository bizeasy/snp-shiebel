export interface IPaymentGatewayConfig {
  id: number;
  name?: string | null;
  authUrl?: string | null;
  releaseUrl?: string | null;
  refundUrl?: string | null;
}

export type NewPaymentGatewayConfig = Omit<IPaymentGatewayConfig, 'id'> & { id: null };
