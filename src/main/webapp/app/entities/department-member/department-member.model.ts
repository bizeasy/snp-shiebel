import dayjs from 'dayjs/esm';

import { IDepartment } from 'app/entities/department/department.model';
import { IParty } from 'app/entities/party/party.model';
import { IRoleType } from 'app/entities/role-type/role-type.model';

export interface IDepartmentMember {
  id: number;
  fromDate?: dayjs.Dayjs | null;
  thruDate?: dayjs.Dayjs | null;
  isPrimary?: boolean | null;
  department?: IDepartment | null;
  party?: IParty | null;
  roleType?: IRoleType | null;
}

export type NewDepartmentMember = Omit<IDepartmentMember, 'id'> & { id: null };
