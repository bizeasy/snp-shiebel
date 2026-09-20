import dayjs from 'dayjs/esm';

export interface ICustomTimePeriod {
  id: number;
  name?: string | null;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
}

export type NewCustomTimePeriod = Omit<ICustomTimePeriod, 'id'> & { id: null };
