export interface IShipmentMethodType {
  id: number;
  name?: string | null;
  description?: string | null;
  sequenceNo?: number | null;
}

export type NewShipmentMethodType = Omit<IShipmentMethodType, 'id'> & { id: null };
