import { IStatus } from 'app/entities/status/status.model';

export interface IStatusValidChange {
  id: number;
  transitionName?: string | null;
  status?: IStatus | null;
  statusTo?: IStatus | null;
}

export type NewStatusValidChange = Omit<IStatusValidChange, 'id'> & { id: null };
