import { IFacility } from 'app/entities/facility/facility.model';
import { IFacilityLocation } from 'app/entities/facility-location/facility-location.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductFacilityLocation {
  id: number;
  minimumStock?: number | null;
  moveQuantity?: number | null;
  product?: IProduct | null;
  facility?: IFacility | null;
  facilityLocation?: IFacilityLocation | null;
}

export type NewProductFacilityLocation = Omit<IProductFacilityLocation, 'id'> & { id: null };
