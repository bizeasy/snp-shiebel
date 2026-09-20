import dayjs from 'dayjs/esm';

import { IContentType } from 'app/entities/content-type/content-type.model';
import { IFileFolder } from 'app/entities/file-folder/file-folder.model';
import { IParty } from 'app/entities/party/party.model';

export interface IAttachment {
  id: number;
  name?: string | null;
  fileAttachment?: string | null;
  fileAttachmentContentType?: string | null;
  attachmentUrl?: string | null;
  mimeType?: string | null;
  fileSize?: number | null;
  uploadedDate?: dayjs.Dayjs | null;
  isTrashed?: boolean | null;
  checksum?: string | null;
  contentType?: IContentType | null;
  folder?: IFileFolder | null;
  owner?: IParty | null;
}

export type NewAttachment = Omit<IAttachment, 'id'> & { id: null };
