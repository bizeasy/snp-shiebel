import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { IStatus } from 'app/entities/status/status.model';

export interface IProductFacilityUser {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  costPerDay?: number | null;
  totalCost?: number | null;
  discount?: number | null;
  productFacility?: IProductFacility | null;
  party?: IParty | null;
  status?: IStatus | null;
}

export type NewProductFacilityUser = Omit<IProductFacilityUser, 'id'> & { id: null };
