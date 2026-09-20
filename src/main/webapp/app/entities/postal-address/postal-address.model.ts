import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { IGeo } from 'app/entities/geo/geo.model';
import { IGeoPoint } from 'app/entities/geo-point/geo-point.model';

export interface IPostalAddress {
  id: number;
  toName?: string | null;
  address1?: string | null;
  address2?: string | null;
  city?: string | null;
  landmark?: string | null;
  postalCode?: string | null;
  isDefault?: boolean | null;
  customAddressType?: string | null;
  stateStr?: string | null;
  countryStr?: string | null;
  isIndianAddress?: boolean | null;
  note?: string | null;
  directions?: string | null;
  contactMech?: IContactMech | null;
  state?: IGeo | null;
  pincode?: IGeo | null;
  country?: IGeo | null;
  geoPoint?: IGeoPoint | null;
}

export type NewPostalAddress = Omit<IPostalAddress, 'id'> & { id: null };
