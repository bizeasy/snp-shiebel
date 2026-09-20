import dayjs from 'dayjs/esm';

import { IUom } from 'app/entities/uom/uom.model';

export interface IUomConversion {
  id: number;
  name?: string | null;
  conversionFactor?: number | null;
  fromDate?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
  fromUom?: IUom | null;
  toUom?: IUom | null;
}

export type NewUomConversion = Omit<IUomConversion, 'id'> & { id: null };
