import dayjs from 'dayjs/esm';

export interface ILot {
  id: number;
  creationDate?: dayjs.Dayjs | null;
  quantity?: number | null;
  expirationDate?: dayjs.Dayjs | null;
  retestDate?: dayjs.Dayjs | null;
}

export type NewLot = Omit<ILot, 'id'> & { id: null };
