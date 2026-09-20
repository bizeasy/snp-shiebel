import dayjs from 'dayjs/esm';

import { IAttachment } from 'app/entities/attachment/attachment.model';
import { SharePermission } from 'app/entities/enumerations/share-permission.model';
import { IFileFolder } from 'app/entities/file-folder/file-folder.model';
import { IParty } from 'app/entities/party/party.model';

export interface IFileShare {
  id: number;
  permission?: keyof typeof SharePermission | null;
  sharedWithEmail?: string | null;
  sharedDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  accessToken?: string | null;
  isRevoked?: boolean | null;
  folder?: IFileFolder | null;
  attachment?: IAttachment | null;
  sharedWith?: IParty | null;
  sharedBy?: IParty | null;
}

export type NewFileShare = Omit<IFileShare, 'id'> & { id: null };
