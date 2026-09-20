export interface IShipmentType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewShipmentType = Omit<IShipmentType, 'id'> & { id: null };
