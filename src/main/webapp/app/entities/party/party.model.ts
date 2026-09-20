import dayjs from 'dayjs/esm';

import { Gender } from 'app/entities/enumerations/gender.model';
import { IPartyType } from 'app/entities/party-type/party-type.model';
import { IPostalAddress } from 'app/entities/postal-address/postal-address.model';
import { IStatus } from 'app/entities/status/status.model';
import { IUser } from 'app/entities/user/user.model';

export interface IParty {
  id: number;
  isOrganisation?: boolean | null;
  organisationName?: string | null;
  organisationShortName?: string | null;
  salutation?: string | null;
  firstName?: string | null;
  middleName?: string | null;
  lastName?: string | null;
  gender?: keyof typeof Gender | null;
  birthDate?: dayjs.Dayjs | null;
  primaryPhone?: string | null;
  primaryEmail?: string | null;
  isTemporaryPassword?: boolean | null;
  logoImageUrl?: string | null;
  profileImageUrl?: string | null;
  notes?: string | null;
  postalAddress?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  partyType?: IPartyType | null;
  status?: IStatus | null;
  address?: IPostalAddress | null;
}

export type NewParty = Omit<IParty, 'id'> & { id: null };
