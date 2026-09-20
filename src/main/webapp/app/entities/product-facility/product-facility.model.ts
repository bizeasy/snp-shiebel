import { IFacility } from 'app/entities/facility/facility.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductFacility {
  id: number;
  minimumStock?: number | null;
  reorderQty?: number | null;
  daysToShip?: number | null;
  lastInventoryCount?: number | null;
  product?: IProduct | null;
  facility?: IFacility | null;
}

export type NewProductFacility = Omit<IProductFacility, 'id'> & { id: null };
