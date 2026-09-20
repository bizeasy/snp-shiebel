import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';

export interface IInventoryItemVariance {
  id: number;
  varianceReason?: string | null;
  atpVar?: number | null;
  qohVar?: number | null;
  comments?: string | null;
  inventoryItem?: IInventoryItem | null;
}

export type NewInventoryItemVariance = Omit<IInventoryItemVariance, 'id'> & { id: null };
