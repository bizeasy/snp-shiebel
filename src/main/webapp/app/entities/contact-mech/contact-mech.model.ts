import { IContactMechType } from 'app/entities/contact-mech-type/contact-mech-type.model';

export interface IContactMech {
  id: number;
  infoString?: string | null;
  contactMechType?: IContactMechType | null;
}

export type NewContactMech = Omit<IContactMech, 'id'> & { id: null };
