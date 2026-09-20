import dayjs from 'dayjs/esm';

import { IParty } from 'app/entities/party/party.model';

export interface IFileFolder {
  id: number;
  name?: string | null;
  path?: string | null;
  isSystemFolder?: boolean | null;
  isTrashed?: boolean | null;
  createdDate?: dayjs.Dayjs | null;
  parent?: IFileFolder | null;
  owner?: IParty | null;
}

export type NewFileFolder = Omit<IFileFolder, 'id'> & { id: null };
